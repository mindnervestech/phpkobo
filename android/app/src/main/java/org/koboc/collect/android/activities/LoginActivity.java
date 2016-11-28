package org.koboc.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.koboc.collect.android.R;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.model.UserVM;
import org.koboc.collect.android.preferences.PreferencesActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class LoginActivity extends Activity {
    private EditText userEditText,passwordEditText;
    private Button submit;
    private MyApi myApi;
    private String BASE_URL;
    private SharedPreferences preferences;
    private Button languageButton;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static final String DATABASE_NAME = "instances.db";
    private static final String DATABASE_NAME1 = "forms.db";
    //SQLiteDatabase db;
    //Cursor cursor1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userEditText= (EditText) findViewById(R.id.userEditText);
        passwordEditText= (EditText) findViewById(R.id.passwordEditText);
        submit= (Button) findViewById(R.id.submitButton);
        languageButton = (Button) findViewById(R.id.langButton);

        BASE_URL = getApplicationContext().getString(R.string.default_phython_server_url);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient()).build();
        myApi = restAdapter.create(MyApi.class);

        prefs = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        SharedPreferences shared = getSharedPreferences("prefs", MODE_PRIVATE);
        final Boolean channel = shared.getBoolean("hindi",false);


        if(channel){
            //System.out.println("hindi..");
            Locale myLocale = new Locale("hn");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            languageButton.setText("English");
        }else {
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            //System.out.println("english..");
            languageButton.setText("हिंदी");
        }
        
        //AuthUser.deleteAll(AuthUser.class);
        AuthUser authUser = AuthUser.findLoggedInUser();
        if(authUser != null && authUser.getLogged().equals("true")) {
			//Uncomment foe new versionm
			CaseRecord record = new CaseRecord();
			List<CaseRecord> caseRecords1 = record.findWithQuery(CaseRecord.class, "SELECT * FROM Case_Record where uid = null");
			List<CaseRecord> caseRecords2 = record.findWithQuery(CaseRecord.class, "SELECT * FROM Case_Record where uid = 0");

			//System.out.println("caseRecords1 ::::: "+caseRecords1.size());
			//System.out.println("caseRecords2 ::::: "+caseRecords2.size());


			List<CaseRecord> lists = record.findWithQuery(CaseRecord.class, "SELECT * FROM Case_Record");
			for(CaseRecord c : lists) {
				//System.out.println("userId ::::: "+c.uid);
				//System.out.println("authUser.getUserId() ::::: "+authUser.getUserId());

				if(c.uid == 0){
					//System.out.println("saved ::::: ");
					c.uid = authUser.getUserId();
				    c.save();
				}
			}

            Intent intent=new Intent(LoginActivity.this,SplashScreenActivity.class);
            startActivity(intent);
            finish();

		}

        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(channel){
                    languageButton.setText("हिंदी");
                    setLocale("en");
                }else{
                    languageButton.setText("English");
                    setLocale("hn");
                }
            }
        });

        //TODO: Logout need to be implemented
        //TODO: What will happen if you internet connection
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", userEditText.getText().toString(), passwordEditText.getText().toString()).getBytes(), Base64.NO_WRAP);
                //System.out.println("auth:::"+basicAuth);

                if(userEditText.getText().toString() != null && !userEditText.getText().toString().isEmpty() && passwordEditText.getText().toString() != null && !passwordEditText.getText().toString().isEmpty()) {
                    myApi.Login(basicAuth, new Callback<UserVM>() {
                        @Override
                        public void success(UserVM userVM, Response response) {
                            //System.out.println("url::::"+response.getUrl());
                           // System.out.println("role::::"+ userVM.getGroups().get(0));

                            AuthUser user = new AuthUser();
                            user.setApi_token(userVM.getApi_token());
                            user.setRole(userVM.getGroups().get(0));
                            user.setUserId(userVM.getId());
                            user.setFirst_name(userVM.getFirst_name());
                            user.setLast_name(userVM.getLast_name());
                            user.setLogged("true");
                            user.setUsername(userEditText.getText().toString());
                            user.setPassword(passwordEditText.getText().toString());
                            //TODO: Password kobo , need to be from string.xml
                            String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", user.getRole() + "_form", "kobo").getBytes(), Base64.NO_WRAP);

                            user.setFormListUser(basicAuth);
                            AuthUser authUser = AuthUser.findLoggedInUser();
                            if (authUser != null) {
                                if (!authUser.getRole().equalsIgnoreCase(user.getRole()) ||
                                        !authUser.getUsername().equalsIgnoreCase(user.getUsername())) {
                                    // if prev logged in users role or username has changed the delete all instance of
                                    // form and case from DB on current device.
                                    //TODO : implement above , refer to delete form section in OOB

                                    // Toast.makeText(LoginActivity.this, " in CaseRecord.deleteAll ", Toast.LENGTH_SHORT).show();
                              //      CaseRecord.deleteAll(CaseRecord.class);

                                    //delete ODK folder from SDcards
                                   /* File folder = Environment.getExternalStorageDirectory();
                                    String fileName = folder.getPath() + "/odk/forms/";
                                    System.out.println("folder path::::" + fileName);
                                    File myFile = new File(fileName);
                                    deleteDirectory(myFile);

                                    String fileName1 = folder.getPath() + "/odk/instances/";
                                    System.out.println("folder path::::" + fileName);
                                    File myFile1 = new File(fileName1);
                                    deleteDirectory(myFile1);
*/
                                    //Database helper instance
                                  /*  InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);
                                    FormsProvider.DatabaseHelper databaseHelper1 = new FormsProvider.DatabaseHelper("forms.db");
                                    db = databaseHelper.getWritableDatabase();
*/
                                    //Form Table Query
                                   /* SQLiteDatabase database = databaseHelper1.getWritableDatabase();

                                    db.execSQL("delete from instances");
                                    database.execSQL("delete from forms");*/

                                    //delete all Form from db and sdcard
                                   /* DeleteFormsTask mDeleteFormsTask = new DeleteFormsTask();
                                    String[] data = new String[]{FormsProviderAPI.FormsColumns.DISPLAY_NAME,
                                            FormsProviderAPI.FormsColumns.DISPLAY_SUBTEXT, FormsProviderAPI.FormsColumns.JR_VERSION};
                                    mDeleteFormsTask.execute(new Long[data.length]);*/

                                /* Dont delete this , it may be used in future
                                String basicAuthWithToken = "Basic " + Base64.encodeToString(String.format("%s:%s", user.getUsername(), user.getApi_token()).getBytes(), Base64.NO_WRAP);
                                myApi.getCase(basicAuthWithToken,new Callback<List<CaseResponseVM>>() {
                                    @Override
                                    public void success(List<CaseResponseVM> caseResponseVMs, Response response) {
                                    }
                                    @Override
                                    public void failure(RetrofitError error) {
                                    }
                                });*/
                                }
                            }
                       /* else {
                            if(user.getRole().contains("consultant")){
                                System.out.println("consultant called::::"+user.getRole());
                                getConsultantCase();
                            }
                        }*/
                            AuthUser.deleteAll(AuthUser.class);

                            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(PreferencesActivity.KEY_FORMLIST_URL, "/" + user.getRole() + "_form/formList");
                            editor.commit();

                            user.save();

							//Uncomment foe new versionm
							CaseRecord record = new CaseRecord();
							List<CaseRecord> lists = record.findWithQuery(CaseRecord.class, "SELECT * FROM Case_Record");
							for(CaseRecord c : lists) {
								//System.out.println("userId ::::: "+c.uid);
								//System.out.println("authUser.getUserId() ::::: "+userVM.getId());

								if(c.uid == 0){
								//	System.out.println("saved ::::: ");
									c.uid = userVM.getId();
									c.save();
								}
							}

                            if (user.getRole().contains("consultant")) {
                                getConsultantCase();
                            }

                            Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                            startActivity(intent);

                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //TODO : Handle null pointer here

                            if(error.getResponse() != null) {
                                if (error.getResponse().getStatus() == 401) {
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.wrongname), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.failuremsg), Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "No network available ", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Enter UserName And Password !",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void getConsultantCase(){

        BASE_URL = getApplicationContext().getString(R.string.default_java_server_url);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient()).build();
        myApi = restAdapter.create(MyApi.class);

        AuthUser authUser = AuthUser.findLoggedInUser();
        String token = authUser.getApi_token();
        String username = authUser.getUsername();
        final String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", username, token).getBytes(), Base64.NO_WRAP);

        myApi.getCase(basicAuth, new Callback<List<CaseResponseVM>>() {
            @Override
            public void success(List<CaseResponseVM> caseVMList, Response response) {

                //System.out.println("basic auth::"+basicAuth);
                //System.out.println("url:::::::::"+response.getUrl());


				/************** OLD CODE **************

                //CaseRecord.deleteAll(CaseRecord.class);
                for(CaseResponseVM crVm : caseVMList){
                    CaseRecord cr = new CaseRecord();
                    cr.caseId = crVm.id;
                    System.out.println("Case record of phne:::"+cr.displayId);
                    System.out.println("Case record of json:::"+crVm.caseId);
                    cr.status = crVm.status;
                    cr.latitude = crVm.latitude;
                    cr.longitude = crVm.longitude;
                    cr.displayId = crVm.caseId;
					//uncomment for new version
					cr.uid = AuthUser.findLoggedInUser().getUserId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date d1= new Date(crVm.dateCreated);
                    Date d2 = new Date(crVm.dateModified);
                    try {
                        cr.dateCreated = sdf.format(d1);
                        cr.dateModified = sdf.format(d2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cr.save();
                }*/

				CaseRecord record = new CaseRecord();
				CaseRecord caseRecord = new CaseRecord();

				List<CaseRecord> caseRecords = caseRecord.listAll(CaseRecord.class);

				//System.out.println("in Login Before ::::: "+caseRecord.listAll(CaseRecord.class).size());

				List<CaseRecord> list = record.findWithQuery(CaseRecord.class, "Select * from Case_Record", null);
				for (CaseResponseVM crVm : caseVMList) {
					List<CaseRecord> records;
					// cr = CaseRecord.findById(CaseRecord.class, crVm.id);
					records = caseRecord.findWithQuery(CaseRecord.class, "Select * from Case_Record where case_id = ?", crVm.id + "");
				//	System.out.println("is that case present ::: "+records.size());
					if (records.size() != 0) {
						continue;
						//break;
					}
					CaseRecord cr = new CaseRecord();
					cr.caseId = crVm.id;

					if (cr == null) {
						cr = new CaseRecord();
						cr.caseId = crVm.id;
					}
					cr.status = crVm.status;
					cr.latitude = crVm.latitude;
					cr.longitude = crVm.longitude;
					cr.displayId = crVm.caseId;
					//uncomment for new version
					cr.uid = AuthUser.findLoggedInUser().getUserId();

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					Date d1 = new Date(crVm.dateCreated);
					Date d2 = new Date(crVm.dateModified);
					try {
						cr.dateCreated = sdf.format(d1);
						cr.dateModified = sdf.format(d2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cr.save();
					//System.out.println("saved from api ::: ");
				}

				//System.out.println("in Login Before ::::: "+caseRecord.listAll(CaseRecord.class).size());

            }

            @Override
            public void failure(RetrofitError error) {
                //TODO : Handle null pointer here
                Toast.makeText(getApplicationContext(),"Unable to connect server ... ",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit app ?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            LoginActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }


    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();

        SharedPreferences prefs = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(lang.equals("hn")){
            editor.putBoolean("hindi", true);
        }else{
            editor.putBoolean("hindi", false);
        }
        editor.commit();

    }


}
