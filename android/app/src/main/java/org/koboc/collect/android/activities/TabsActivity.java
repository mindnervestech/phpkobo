/*
 * Copyright (C) 2012 University of Washington
 * Copyright (C) 2007 The Android Open Source Project
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
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.koboc.collect.android.R;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Modified from the FingerPaint example found in The Android Open Source
 * Project.
 *
 * @author BehrAtherton@gmail.com
 */
public class TabsActivity extends TabActivity implements TabHost.OnTabChangeListener {
    /**
     * Called when the activity is first created.
     */


    private static final int PASSWORD_DIALOG = 1;
    TabHost tabHost;
    protected View mDrawerView;
    private TextView userName,userRole,contactText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        // create the TabHost that will contain the Tabs
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        userName = (TextView) findViewById(R.id.var_user_name);
        userRole = (TextView) findViewById(R.id.var_user_role);
        contactText = (TextView) findViewById(R.id.nav_contact);

        AuthUser checkUser = AuthUser.findLoggedInUser();
        String getUserRole = checkUser.getRole();

        userName.setText(checkUser.getFirst_name());
        userRole.setText(checkUser.getRole());

        if (getUserRole.contains("consultant")) {
            contactText.setVisibility(View.GONE);
        }

        mDrawerView = findViewById(R.id.nav_drawer);

        if (!getUserRole.contains("consultant")) {
            TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
            tab1.setIndicator(getIndicator(getApplicationContext().getString(R.string.alert)));
            tab1.setContent(new Intent(this, AlertActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            tabHost.addTab(tab1);
        } else {

            TabHost.TabSpec tab6 = tabHost.newTabSpec("Five Tab");
            tab6.setIndicator(getIndicator(getApplicationContext().getString(R.string.preforms)));
            tab6.setContent(new Intent(this, PreCompleteActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            tabHost.addTab(tab6);

            TabHost.TabSpec tab7 = tabHost.newTabSpec("Six Tab");
            tab7.setIndicator(getIndicator(getApplicationContext().getString(R.string.create)));
            tab7.setContent(new Intent(this, CreateCaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            tabHost.addTab(tab7);
        }


        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Fourth tab");
        TabHost.TabSpec tab5 = tabHost.newTabSpec("Fifth tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected

        tab2.setIndicator(getIndicator(getApplicationContext().getString(R.string.map)));
        tab3.setIndicator(getIndicator(getApplicationContext().getString(R.string.map)));
        tab4.setIndicator(getIndicator(getApplicationContext().getString(R.string.open_cases)));
        tab5.setIndicator(getIndicator(getApplicationContext().getString(R.string.complete_cases)));

        //tab1.setIndicator("My Loc");
        //tab2.setIndicator("Alert");
        //tab3.setIndicator("My Forms");

        tab2.setContent(new Intent(this, NearstLocation.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        tab3.setContent(new Intent(this, MainMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        tab4.setContent(new Intent(this, MyCaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        tab5.setContent(new Intent(this, SubmittedCaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        /** Add the tabs  to the TabHost to display. */

        tabHost.addTab(tab2);
        //tabHost.addTab(tab3);
        tabHost.addTab(tab4);
        tabHost.addTab(tab5);

        ((TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setTextColor(Color.parseColor("#d14b8f"));

        tabHost.setOnTabChangedListener(this);

        if (getUserRole.contains("consultant")) {
            tabHost.setCurrentTab(1);
        }

        // tabHost.getTabWidget().getChildAt(0).getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
            tabHost.getTabWidget().getChildAt(i).setMinimumWidth(100);


    }

    private TextView getIndicator(String tabName){
        TextView tv = new TextView(this);
        tv.setText(tabName);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.parseColor("#ffffff"));
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(15);

        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, (float) 1.0));
        tv.setBackgroundResource(R.drawable.tab_indicator);
        return tv;
    }

    public void onTabChanged(String tabId) {
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            ((TextView) tabHost.getTabWidget().getChildAt(i)).setTextColor(Color.BLACK);
        }

        ((TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setTextColor(Color.parseColor("#d14b8f"));
    }

    public void navMyForms(View v){
        Intent intent = new Intent(TabsActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void navContact(View v){
        Intent intent2 = new Intent(TabsActivity.this, ContactActivity.class);
        startActivity(intent2);
    }

    public void navLogout(View v){
        //AuthUser.deleteAll(AuthUser.class);
        AuthUser authUser1 = AuthUser.findLoggedInUser();
        authUser1.setLogged("false");
        authUser1.save();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

	public void navBackUp(View v){
			File exportDir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "odk", "");
			if (!exportDir.exists())
			{
				exportDir.mkdirs();
			}
			File file = new File(exportDir, "AllCases.csv");

			try
			{
				file.createNewFile();
				CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

				for(CaseRecord record : CaseRecord.listAll(CaseRecord.class)){
					String arrStr[] ={record.longitude+"",record.latitude+"", record.address,record.displayId,record.caseId+"",record.uid+"",record.status,record.dateCreated,record.dateModified,record.isSent+""};
					csvWrite.writeNext(arrStr);
				}
				csvWrite.close();

				Toast.makeText(getApplicationContext(),"Backup completed successfully !",Toast.LENGTH_LONG).show();
			}

			catch(Exception sqlEx)
			{
				Toast.makeText(getApplicationContext(),"Backup failed.Try again.",Toast.LENGTH_LONG).show();
				Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
			}

	}

	public void navRestore(View v) {

		File exportDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "odk", "");
		File file = new File(exportDir, "AllCases.csv");

		try {
			CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file)));
			String[] strings = csvReader.readNext();

			CaseRecord record = new CaseRecord();

			record.longitude = Double.parseDouble(strings[0]);
			record.latitude = Double.parseDouble(strings[1]);
			record.address = strings[2];
			record.displayId = strings[3];
			record.caseId = Long.parseLong(strings[4]);
			record.uid = Long.parseLong(strings[5]);
			record.status = strings[6];
			record.dateCreated = strings[7];
			record.dateModified = strings[8];
			record.isSent = Boolean.parseBoolean(strings[9]);

			record.save();

			Toast.makeText(getApplicationContext(),"Backup restored successfully !",Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),"Backup restored failed, Try again !",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}


	}


}
