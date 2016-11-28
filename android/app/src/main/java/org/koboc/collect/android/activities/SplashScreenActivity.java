/*
 * Copyright (C) 2011 University of Washington
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

package org.koboc.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.koboc.collect.android.R;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.preferences.PreferencesActivity;
import org.opendatakit.httpclientandroidlib.auth.AUTH;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SplashScreenActivity extends Activity {

    private static final int mSplashTimeout = 3000; // milliseconds
    private static final boolean EXIT = true;

    private int mImageMaxWidth;
    private AlertDialog mAlertDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //System.out.println("in splash  AuthUser:::::::::::::::"+ AuthUser.listAll(AuthUser.class).size());
        //System.out.println("in splash  CaseRecord:::::::::::::::"+ CaseRecord.listAll(CaseRecord.class).size());

        /*CaseRecord record = new CaseRecord();
        record.caseId = 1446195311;
        record.dateCreated = "1446195636211";
        record.status = "submitted";
        record.save();

        CaseRecord record1 = new CaseRecord();
        record1.caseId = 1446195652;
        record1.dateCreated = "1446196372318";
        record1.status = "submitted";
        record1.save();

        CaseRecord record2 = new CaseRecord();
        record2.caseId = 1446198763;
        record2.dateCreated = "1446200260971";
        record2.status = "submitted";
        record2.save();

        CaseRecord record3 = new CaseRecord();
        record3.caseId = 1446200478;
        record3.status = "complete";
        record3.dateCreated = "1446200905762";
        record3.save();

        CaseRecord record4 = new CaseRecord();
        record4.caseId = 1446115960;
        record4.displayId = "fail 1";
        record4.dateCreated = "1446202220088";
        record4.status = "submissionFailed";
        record4.save();

        CaseRecord record5 = new CaseRecord();
        record5.caseId = 1446196513;
        record5.dateCreated = "1446208552914";
        record5.status = "submissionFailed";
        record5.save();

        CaseRecord record6 = new CaseRecord();
        record6.caseId = 1446209172;
        record6.dateCreated = "1446209834273";
        record6.status = "complete";
        record6.save();

        CaseRecord record7 = new CaseRecord();
        record7.caseId = 1446631999;
        record7.dateCreated = "1446632615288";
        record7.status = "complete";
        record7.save();

        CaseRecord record8 = new CaseRecord();
        record8.caseId = 1446232730;
        record8.dateCreated = "1446633101934";
        record8.status = "complete";
        record8.save();

        CaseRecord record9 = new CaseRecord();
        record9.caseId = 1446705801;
        record9.status = "complete";
        record9.dateCreated = "1446706440540";
        record9.save();

        CaseRecord record10 = new CaseRecord();
        record10.caseId = 1446707214;
        record10.dateCreated = "1446708428262";
        record10.status = "complete";
        record10.save();

        CaseRecord record11 = new CaseRecord();
        record11.caseId = 1447826336;
        record11.dateCreated="1447828101959";
        record11.status = "complete";
        record11.save();

        CaseRecord record12 = new CaseRecord();
        record12.caseId = 1448699876;
        record12.dateCreated = "1448700208398";
        record12.status = "complete";
        record12.save();

        CaseRecord record13 = new CaseRecord();
        record13.caseId = 1448885436;
        record13.dateCreated = "1448886011043";
        record13.status = "complete";
        record13.save();

        CaseRecord record14 = new CaseRecord();
        record14.caseId = 1448886108;
        record14.dateCreated = "1448886378888";
        record14.status = "complete";
        record14.save();

        CaseRecord record15 = new CaseRecord();
        record15.caseId = 1448886644;
        record15.dateCreated = "1448887102776";
        record15.status = "complete";
        record15.save();

        CaseRecord record16 = new CaseRecord();
        record16.caseId = 1448887148;
        record16.dateCreated = "1448887509327";
        record16.status = "complete";
        record16.save();

        CaseRecord record17 = new CaseRecord();
        record17.caseId = 1448887790;
        record17.dateCreated = "1448888067496";
        record17.status = "complete";
        record17.save();

        CaseRecord record18 = new CaseRecord();
        record18.caseId = 1448888270;
        record18.dateCreated = "1448888704256";
        record18.status = "complete";
        record18.save();

        CaseRecord record19 = new CaseRecord();
        record19.caseId = 1448888732;
        record19.dateCreated = "1448888974368";
        record19.status = "complete";
        record19.save();

        CaseRecord record20 = new CaseRecord();
        record20.caseId = 1448889000;
        record20.dateCreated = "1448889281132";
        record20.status = "complete";
        record20.save();

        CaseRecord record22 = new CaseRecord();
        record22.caseId = 1448889542;
        record22.dateCreated = "1449122065421";
        record22.status = "complete";
        record22.save();

        CaseRecord record23 = new CaseRecord();
        record23.caseId = 1446707851;
        record23.dateCreated = "1449123340835";
        record23.status = "complete";
        record23.save();

        CaseRecord record24 = new CaseRecord();
        record24.caseId = 1449652711;
        record24.dateCreated = "1449653034782";
        record24.status = "complete";
        record24.save();
*/
        // must be at the beginning of any activity that can be called from an external intent
        try {
            Collect.createODKDirs();
        } catch (RuntimeException e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }

        mImageMaxWidth = getWindowManager().getDefaultDisplay().getWidth();

        // this splash screen should be a blank slate
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        // get the shared preferences object
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = mSharedPreferences.edit();


        editor.putString(PreferencesActivity.KEY_SERVER_URL,getApplicationContext().getString(R.string.default_phython_server_url));
        editor.commit();


        // get the package info object with version number
        PackageInfo packageInfo = null;
        try {
            packageInfo =
                getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        boolean firstRun = mSharedPreferences.getBoolean(PreferencesActivity.KEY_FIRST_RUN, true);
        boolean showSplash =
            mSharedPreferences.getBoolean(PreferencesActivity.KEY_SHOW_SPLASH, false);
        String splashPath =
            mSharedPreferences.getString(PreferencesActivity.KEY_SPLASH_PATH,"splash");
              //  getString(R.string.default_splash_path));

        firstRun = true;  //have to change

        // if you've increased version code, then update the version number and set firstRun to true
        if (mSharedPreferences.getLong(PreferencesActivity.KEY_LAST_VERSION, 0) < packageInfo.versionCode) {
            editor.putLong(PreferencesActivity.KEY_LAST_VERSION, packageInfo.versionCode);
            editor.commit();

            firstRun = true;
        }

        // do all the first run things
        if (firstRun) {
            editor.putBoolean(PreferencesActivity.KEY_FIRST_RUN, false);
            editor.commit();
            startSplashScreen(splashPath);
        } else {
            endSplashScreen();
        }

    }


    private void endSplashScreen() {

        // launch new activity and close splash screen
        startActivity(new Intent(SplashScreenActivity.this, TabsActivity.class));
        finish();
    }


    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        Bitmap b = null;
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int scale = 1;
            if (o.outHeight > mImageMaxWidth || o.outWidth > mImageMaxWidth) {
                scale =
                    (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(mImageMaxWidth
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
        }
        return b;
    }


    private void startSplashScreen(String path) {

        // add items to the splash screen here. makes things less distracting.
        ImageView iv = (ImageView) findViewById(R.id.splash);
        LinearLayout ll = (LinearLayout) findViewById(R.id.splash_default);

        File f = new File(path);
        if (f.exists()) {
            iv.setImageBitmap(decodeFile(f));
            ll.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
        }

        // create a thread that counts up to the timeout
        Thread t = new Thread() {
            int count = 0;


            @Override
            public void run() {
                try {
                    super.run();
                    while (count < mSplashTimeout) {
                        sleep(100);
                        count += 100;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endSplashScreen();
                }
            }
        };
        t.start();
    }


    private void createErrorDialog(String errorMsg, final boolean shouldExit) {
	    Collect.getInstance().getActivityLogger().logAction(this, "createErrorDialog", "show");
        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.setIcon(android.R.drawable.ic_dialog_info);
        mAlertDialog.setMessage(errorMsg);
        DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case DialogInterface.BUTTON1:
                	    Collect.getInstance().getActivityLogger().logAction(this, "createErrorDialog", "OK");
                        if (shouldExit) {
                            finish();
                        }
                        break;
                }
            }
        };
        mAlertDialog.setCancelable(false);
        mAlertDialog.setButton(getString(R.string.ok), errorListener);
        mAlertDialog.show();
    }
	
    @Override
    protected void onStart() {
    	super.onStart();
		Collect.getInstance().getActivityLogger().logOnStart(this); 
    }
    
    @Override
    protected void onStop() {
		Collect.getInstance().getActivityLogger().logOnStop(this); 
    	super.onStop();
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit app ?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashScreenActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
