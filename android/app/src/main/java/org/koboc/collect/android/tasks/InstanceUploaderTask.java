/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.koboc.collect.android.tasks;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.koboc.collect.android.R;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.listeners.InstanceUploaderListener;
import org.koboc.collect.android.logic.PropertyManager;
import org.koboc.collect.android.preferences.PreferencesActivity;
import org.koboc.collect.android.provider.InstanceProviderAPI;
import org.koboc.collect.android.provider.InstanceProviderAPI.InstanceColumns;
import org.koboc.collect.android.utilities.WebUtils;
import org.opendatakit.httpclientandroidlib.Header;
import org.opendatakit.httpclientandroidlib.HttpResponse;
import org.opendatakit.httpclientandroidlib.HttpStatus;
import org.opendatakit.httpclientandroidlib.client.ClientProtocolException;
import org.opendatakit.httpclientandroidlib.client.HttpClient;
import org.opendatakit.httpclientandroidlib.client.methods.HttpHead;
import org.opendatakit.httpclientandroidlib.client.methods.HttpPost;
import org.opendatakit.httpclientandroidlib.conn.ConnectTimeoutException;
import org.opendatakit.httpclientandroidlib.entity.mime.MultipartEntity;
import org.opendatakit.httpclientandroidlib.entity.mime.content.FileBody;
import org.opendatakit.httpclientandroidlib.entity.mime.content.StringBody;
import org.opendatakit.httpclientandroidlib.protocol.HttpContext;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Background task for uploading completed forms.
 *
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class InstanceUploaderTask extends AsyncTask<Long, Integer, HashMap<String, String>> {



    private static final String t = "InstanceUploaderTask";
    // it can take up to 27 seconds to spin up Aggregate
    private static final int CONNECTION_TIMEOUT = 60000;
    private static final String fail = "Error: ";

    private InstanceUploaderListener mStateListener;

	private Context mContext;

    private Uri mAuthRequestingServer;
    HashMap<String, String> mResults;


	/*public InstanceUploaderTask(Context context) {

		mContext = context;

	}*/



	/**
     * Uploads to urlString the submission identified by id with filepath of instance
     * @param urlString destination URL
     * @param id
     * @param instanceFilePath
     * @param toUpdate - Instance URL for recording status update.
     * @param httpclient - client connection
     * @param localContext - context (e.g., credentials, cookies) for client connection
     * @param uriRemap - mapping of Uris to avoid redirects on subsequent invocations
     * @return false if credentials are required and we should terminate immediately.
     */
    private boolean uploadOneSubmission(String urlString, String id, String instanceFilePath,
    			Uri toUpdate, HttpContext localContext, Map<Uri, Uri> uriRemap) {

    	Collect.getInstance().getActivityLogger().logAction(this, urlString, instanceFilePath);

        ContentValues cv = new ContentValues();
        Uri u = Uri.parse(urlString);
        HttpClient httpclient = WebUtils.createHttpClient(CONNECTION_TIMEOUT);

        boolean openRosaServer = false;
        if (uriRemap.containsKey(u)) {
            // we already issued a head request and got a response,
            // so we know the proper URL to send the submission to
            // and the proper scheme. We also know that it was an
            // OpenRosa compliant server.
            openRosaServer = true;
            u = uriRemap.get(u);

            // if https then enable preemptive basic auth...
            if ( u.getScheme().equals("https") ) {
            	WebUtils.enablePreemptiveBasicAuth(localContext, u.getHost());
            }

            Log.i(t, "Using Uri remap for submission " + id + ". Now: " + u.toString());
        } else {

            // if https then enable preemptive basic auth...
            if ( u.getScheme().equals("https") ) {
            	WebUtils.enablePreemptiveBasicAuth(localContext, u.getHost());
            }

            // we need to issue a head request
            HttpHead httpHead = WebUtils.createOpenRosaHttpHead(u);

            // prepare response
            HttpResponse response = null;
            try {
                Log.i(t, "Issuing HEAD request for " + id + " to: " + u.toString());

                // send basic auth in header

                AuthUser user = AuthUser.findLoggedInUser();
                httpHead.setHeader("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", user.getUsername(),
                        user.getPassword()).getBytes(), Base64.NO_WRAP));

                response = httpclient.execute(httpHead, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            		// clear the cookies -- should not be necessary?
            		Collect.getInstance().getCookieStore().clear();

                	WebUtils.discardEntityBytes(response);
            		// we need authentication, so stop and return what we've
                    // done so far.
                    mAuthRequestingServer = u;
                    return false;
                } else if (statusCode == 204) {
                	Header[] locations = response.getHeaders("Location");
                	WebUtils.discardEntityBytes(response);
                    if (locations != null && locations.length == 1) {
                        try {
                            String oldQueryParam = u.getEncodedQuery();
                            String newUrlStr = locations[0].getValue() + "?" + oldQueryParam;
                            Uri uNew = Uri.parse(URLDecoder.decode(newUrlStr, "utf-8"));
                            if (u.getHost().equalsIgnoreCase(uNew.getHost())) {
                                openRosaServer = true;
                                // trust the server to tell us a new location
                                // ... and possibly to use https instead.


                                uriRemap.put(u, uNew);
                                u = uNew;
                            } else {
                                // Don't follow a redirection attempt to a different host.
                                // We can't tell if this is a spoof or not.
                                mResults.put(
                                    id,
                                    fail
                                            + "Unexpected redirection attempt to a different host: "
                                            + uNew.toString());
                                cv.put(InstanceColumns.STATUS,
                                    InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                                Collect.getInstance().getContentResolver()
                                        .update(toUpdate, cv, null, null);
                                return true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
							//System.out.println("error :::::::::::: 5");
							sendMail("akshaythakar42@gmail.com", "Kobo Exception", e.toString());
                            mResults.put(id, fail + urlString + " " + e.toString());
                            cv.put(InstanceColumns.STATUS,
                                InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                            Collect.getInstance().getContentResolver()
                                    .update(toUpdate, cv, null, null);
                            return true;
                        }
                    }
                } else {
                    // may be a server that does not handle
                	WebUtils.discardEntityBytes(response);

                    Log.w(t, "Status code on Head request: " + statusCode);
                    if (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
                        mResults.put(
                            id,
                            fail
                                    + "Invalid status code on Head request.  If you have a web proxy, you may need to login to your network. ");
                        cv.put(InstanceColumns.STATUS,
                            InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                        Collect.getInstance().getContentResolver()
                                .update(toUpdate, cv, null, null);
                        return true;
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
				//System.out.println("error :::::::::::: 4");
				sendMail("akshaythakar42@gmail.com", "Kobo Exception", e.toString());
                Log.e(t, e.toString());
                WebUtils.clearHttpConnectionManager();
                mResults.put(id, fail + "Client Protocol Exception");
                cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
                return true;
            } catch (ConnectTimeoutException e) {
                e.printStackTrace();
				sendMail("akshaythakar42@gmail.com", "Kobo Exception", e.toString());
                Log.e(t, e.toString());
                WebUtils.clearHttpConnectionManager();
                mResults.put(id, fail + "Connection Timeout");
                cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
                return true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Log.e(t, e.toString());
				//System.out.println("error :::::::::::: 3");
				sendMail("akshaythakar42@gmail.com", "Kobo Exception", e.toString());
                WebUtils.clearHttpConnectionManager();
                mResults.put(id, fail + e.toString() + " :: Network Connection Failed");
                cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
                return true;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.e(t, e.toString());
                WebUtils.clearHttpConnectionManager();
				//System.out.println("error :::::::::::: 2");
				sendMail("akshaythakar42@gmail.com", "Kobo Exception", e.toString());
                mResults.put(id, fail + "Connection Timeout");
                cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(t, e.toString());
                WebUtils.clearHttpConnectionManager();

				//System.out.println("generic exception 2::::: ");


				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));

				sendMail("akshaythakar42@gmail.com", "Kobo Exception", errors.toString());

				//Collect.getInstance().sendEmail(e);

				//sendEmail(e);

                mResults.put(id, fail + "Generic Exception");
                cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
                return true;
            }
        }

        // At this point, we may have updated the uri to use https.
        // This occurs only if the Location header keeps the host name
        // the same. If it specifies a different host name, we error
        // out.
        //
        // And we may have set authentication cookies in our
        // cookiestore (referenced by localContext) that will enable
        // authenticated publication to the server.
        //
        // get instance file
        File instanceFile = new File(instanceFilePath);

        // Under normal operations, we upload the instanceFile to
        // the server.  However, during the save, there is a failure
        // window that may mark the submission as complete but leave
        // the file-to-be-uploaded with the name "submission.xml" and
        // the plaintext submission files on disk.  In this case,
        // upload the submission.xml and all the files in the directory.
        // This means the plaintext files and the encrypted files
        // will be sent to the server and the server will have to
        // figure out what to do with them.
        File submissionFile = new File(instanceFile.getParentFile(), "submission.xml");
        if ( submissionFile.exists() ) {
            Log.w(t, "submission.xml will be uploaded instead of " + instanceFile.getAbsolutePath());
        } else {
            submissionFile = instanceFile;
        }

        if (!instanceFile.exists() && !submissionFile.exists()) {
            mResults.put(id, fail + "instance XML file does not exist!");
            cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
            Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
            return true;
        }

        // find all files in parent directory
        File[] allFiles = instanceFile.getParentFile().listFiles();

        // add media files
        List<File> files = new ArrayList<File>();
        for (File f : allFiles) {
            String fileName = f.getName();

            int dotIndex = fileName.lastIndexOf(".");
            String extension = "";
            if (dotIndex != -1) {
                extension = fileName.substring(dotIndex + 1);
            }

            if (fileName.startsWith(".")) {
                // ignore invisible files
                continue;
            }
            if (fileName.equals(instanceFile.getName())) {
                continue; // the xml file has already been added
            } else if (fileName.equals(submissionFile.getName())) {
                continue; // the xml file has already been added
            } else if (openRosaServer) {
                files.add(f);
            } else if (extension.equals("jpg")) { // legacy 0.9x
                files.add(f);
            } else if (extension.equals("3gpp")) { // legacy 0.9x
                files.add(f);
            } else if (extension.equals("3gp")) { // legacy 0.9x
                files.add(f);
            } else if (extension.equals("mp4")) { // legacy 0.9x
                files.add(f);
            } else {
                Log.w(t, "unrecognized file type " + f.getName());
            }
        }

        boolean first = true;
        int j = 0;
        int lastJ;
        while (j < files.size() || first) {
        	lastJ = j;
            first = false;

            HttpPost httppost = WebUtils.createOpenRosaHttpPost(u);

            MimeTypeMap m = MimeTypeMap.getSingleton();

            long byteCount = 0L;

            // mime post
            MultipartEntity entity = new MultipartEntity();

            // add the submission file first...
            FileBody fb = new FileBody(submissionFile, "text/xml");
            entity.addPart("xml_submission_file", fb);
            Log.i(t, "added xml_submission_file: " + submissionFile.getName());
            byteCount += submissionFile.length();

            for (; j < files.size(); j++) {
                File f = files.get(j);
                String fileName = f.getName();
                int idx = fileName.lastIndexOf(".");
                String extension = "";
                if (idx != -1) {
                    extension = fileName.substring(idx + 1);
                }
                String contentType = m.getMimeTypeFromExtension(extension);

                // we will be processing every one of these, so
                // we only need to deal with the content type determination...
                if (extension.equals("xml")) {
                    fb = new FileBody(f, "text/xml");
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t, "added xml file " + f.getName());
                } else if (extension.equals("jpg")) {
                    fb = new FileBody(f, "image/jpeg");
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t, "added image file " + f.getName());
                } else if (extension.equals("3gpp")) {
                    fb = new FileBody(f, "audio/3gpp");
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t, "added audio file " + f.getName());
                } else if (extension.equals("3gp")) {
                    fb = new FileBody(f, "video/3gpp");
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t, "added video file " + f.getName());
                } else if (extension.equals("mp4")) {
                    fb = new FileBody(f, "video/mp4");
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t, "added video file " + f.getName());
                } else if (extension.equals("csv")) {
                    fb = new FileBody(f, "text/csv");
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t, "added csv file " + f.getName());
                } else if (f.getName().endsWith(".amr")) {
                    fb = new FileBody(f, "audio/amr");
                    entity.addPart(f.getName(), fb);
                    Log.i(t, "added audio file " + f.getName());
                } else if (extension.equals("xls")) {
                    fb = new FileBody(f, "application/vnd.ms-excel");
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t, "added xls file " + f.getName());
                } else if (contentType != null) {
                    fb = new FileBody(f, contentType);
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.i(t,
                        "added recognized filetype (" + contentType + ") " + f.getName());
                } else {
                    contentType = "application/octet-stream";
                    fb = new FileBody(f, contentType);
                    entity.addPart(f.getName(), fb);
                    byteCount += f.length();
                    Log.w(t, "added unrecognized file (" + contentType + ") " + f.getName());
                }

                // we've added at least one attachment to the request...
                if (j + 1 < files.size()) {
                    if ((j-lastJ+1 > 100) || (byteCount + files.get(j + 1).length() > 10000000L)) {
                        // the next file would exceed the 10MB threshold...
                        Log.i(t, "Extremely long post is being split into multiple posts");
                        try {
                            StringBody sb = new StringBody("yes", Charset.forName("UTF-8"));
                            entity.addPart("*isIncomplete*", sb);
                        } catch (Exception e) {
                            e.printStackTrace(); // never happens...
                        }
                        ++j; // advance over the last attachment added...
                        break;
                    }
                }
            }

            httppost.setEntity(entity);

            // prepare response and return uploaded
            HttpResponse response = null;
            try {
                Log.i(t, "Issuing POST request for " + id + " to: " + u.toString());

                AuthUser user = AuthUser.findLoggedInUser();

                httppost.setHeader("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", user.getUsername(),
                        user.getPassword()).getBytes(), Base64.NO_WRAP));

                response = httpclient.execute(httppost, localContext);
                int responseCode = response.getStatusLine().getStatusCode();
                WebUtils.discardEntityBytes(response);

                Log.i(t, "Response code:" + responseCode);
                // verify that the response was a 201 or 202.
                // If it wasn't, the submission has failed.
                if (responseCode != HttpStatus.SC_CREATED && responseCode != HttpStatus.SC_ACCEPTED) {
                    if (responseCode == HttpStatus.SC_OK) {
                        mResults.put(id, fail + "Network login failure? Again?");
                    } else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
                		// clear the cookies -- should not be necessary?
                    	Collect.getInstance().getCookieStore().clear();
                        mResults.put(id, fail + response.getStatusLine().getReasonPhrase()
                                + " (" + responseCode + ") at " + urlString);
                    } else {
                        mResults.put(id, fail + response.getStatusLine().getReasonPhrase()
                                + " (" + responseCode + ") at " + urlString);
                    }
                    cv.put(InstanceColumns.STATUS,
                        InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                    Collect.getInstance().getContentResolver()
                            .update(toUpdate, cv, null, null);
                    return true;
                }
            } catch (Exception e) {

				//System.out.println("generic exception 1::::: ");

				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));



				sendMail("akshaythakar42@gmail.com", "Kobo Exception", errors.toString());

				//Collect.getInstance().sendEmail(e);

				//sendEmail(e);

                e.printStackTrace();
                Log.e(t, e.toString());
                WebUtils.clearHttpConnectionManager();
                mResults.put(id, fail + "Generic Exception. " + e.toString());
                cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMISSION_FAILED);
                Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
                return true;
            }
        }

        // if it got here, it must have worked
        mResults.put(id, Collect.getInstance().getString(R.string.success));
        cv.put(InstanceColumns.STATUS, InstanceProviderAPI.STATUS_SUBMITTED);
        Collect.getInstance().getContentResolver().update(toUpdate, cv, null, null);
        return true;
    }

    // TODO: This method is like 350 lines long, down from 400.
    // still. ridiculous. make it smaller.
    @Override
    protected HashMap<String, String> doInBackground(Long... values) {
        mResults = new HashMap<String, String>();
        mAuthRequestingServer = null;



        String selection = InstanceColumns._ID + "=?";
        String[] selectionArgs = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            if (i != values.length - 1) {
                selection += " or " + InstanceColumns._ID + "=?";
            }
            selectionArgs[i] = values[i].toString();
        }

        String deviceId = new PropertyManager(Collect.getInstance().getApplicationContext())
        						.getSingularProperty(PropertyManager.OR_DEVICE_ID_PROPERTY);

        // get shared HttpContext so that authentication and cookies are retained.
        HttpContext localContext = Collect.getInstance().getHttpContext();

        Map<Uri, Uri> uriRemap = new HashMap<Uri, Uri>();
        String role = AuthUser.findLoggedInUser().getRole();

        Cursor c = null;
        try {
        	c = Collect.getInstance().getContentResolver()
                    .query(InstanceColumns.CONTENT_URI, null, selection, selectionArgs, null);

	        if (c.getCount() > 0) {
	            c.moveToPosition(-1);
	            while (c.moveToNext()) {
	                if (isCancelled()) {
	                    return mResults;
	                }
	                publishProgress(c.getPosition() + 1, c.getCount());
	                String instance = c.getString(c.getColumnIndex(InstanceColumns.INSTANCE_FILE_PATH));
                    String caseId = c.getString(c.getColumnIndex(InstanceColumns.CASE_ID)); // This line has been added by jagbir
	                String id = c.getString(c.getColumnIndex(InstanceColumns._ID));
                    String form_id = c.getString(c.getColumnIndex(InstanceColumns.JR_FORM_ID));
	                Uri toUpdate = Uri.withAppendedPath(InstanceColumns.CONTENT_URI, id);

	                int subIdx = c.getColumnIndex(InstanceColumns.SUBMISSION_URI);
	                String urlString = c.isNull(subIdx) ? null : c.getString(subIdx);
	                if (urlString == null) {
	                    SharedPreferences settings =
	                        PreferenceManager.getDefaultSharedPreferences(Collect.getInstance());
	                    urlString = settings.getString(PreferencesActivity.KEY_SERVER_URL,
	                    				Collect.getInstance().getString(R.string.default_server_url));
	                    if ( urlString.charAt(urlString.length()-1) == '/') {
	                    	urlString = urlString.substring(0, urlString.length()-1);
	                    }
	                    // NOTE: /submission must not be translated! It is the well-known path on the server.
	                    String submissionUrl =
	                        settings.getString(PreferencesActivity.KEY_SUBMISSION_URL, "/submission");
	                    if ( submissionUrl.charAt(0) != '/') {
	                    	submissionUrl = "/" + submissionUrl;
	                    }

	                    urlString = urlString + submissionUrl;
	                }

	                // add the deviceID to the request...
	                try {
                        String status = role.equalsIgnoreCase("sangini") ?
                                "Submitted":form_id.toLowerCase().contains("pre_")?"Complete":"Closed";

						//System.out.println("error :::::::::::: 1");
					//	sendMail("akshaythakar42@gmail.com", "Kobo Exception", status);



                        urlString += "?deviceID=" + URLEncoder.encode(deviceId, "UTF-8");
                        urlString += "&caseID=" + URLEncoder.encode(caseId, "UTF-8"); // This has been added by jagbir
                        urlString += "&status=" + URLEncoder.encode(status, "UTF-8");
					} catch (UnsupportedEncodingException e) {

						sendMail("akshaythakar42@gmail.com", "Kobo Exception", e.toString());
						// unreachable...
					}

	                if ( !uploadOneSubmission(urlString, id, instance, toUpdate, localContext, uriRemap) ) {
	                	return null; // get credentials...
	                }
	            }
	        }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return mResults;
    }


    @Override
    protected void onPostExecute(HashMap<String, String> value) {
        synchronized (this) {
            if (mStateListener != null) {
                if (mAuthRequestingServer != null) {
                    mStateListener.authRequest(mAuthRequestingServer, mResults);
                } else {
                    mStateListener.uploadingComplete(value);
                }
            }
        }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        synchronized (this) {
            if (mStateListener != null) {
                // update progress and total
                mStateListener.progressUpdate(values[0].intValue(), values[1].intValue());
            }
        }
    }


    public void setUploaderListener(InstanceUploaderListener sl) {
        synchronized (this) {
            mStateListener = sl;
        }
    }

	private static final String username = "akshaythakar42@gmail.com";
	private static final String password = "24akshay92";


	private void sendMail(String email, String subject, String messageBody) {
		Session session = createSessionObject();

		try {
			Message message = createMessage(email, subject, messageBody, session);
			new SendMailTask().execute(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("tutorials@tiemenschut.com", "Tiemen Schut"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
		message.setSubject(subject);
		message.setText(messageBody);
		return message;
	}

	private Session createSessionObject() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		return Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	private class SendMailTask extends AsyncTask<Message, Void, Void> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//progressDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Sending mail", true, false);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
		//	progressDialog.dismiss();
		}

		@Override
		protected Void doInBackground(Message... messages) {
			try {
				Transport.send(messages[0]);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}


}
