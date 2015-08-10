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

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.koboc.collect.android.R;

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

    TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        // create the TabHost that will contain the Tabs
        tabHost = (TabHost) findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Fourth tab");
        TabHost.TabSpec tab5 = tabHost.newTabSpec("Fifth tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator(getIndicator("Alert"));
        tab2.setIndicator(getIndicator("Map"));
        tab3.setIndicator(getIndicator("My Forms"));
        tab4.setIndicator(getIndicator("Open Cases"));
        tab5.setIndicator(getIndicator("Completed"));


        //tab1.setIndicator("My Loc");
        //tab2.setIndicator("Alert");
        //tab3.setIndicator("My Forms");
        tab1.setContent(new Intent(this, AlertActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


        tab2.setContent(new Intent(this, NearstLocation.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


        tab3.setContent(new Intent(this, MainMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        tab4.setContent(new Intent(this, MyCaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        tab5.setContent(new Intent(this, SubmittedCaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        //tabHost.addTab(tab3);
        tabHost.addTab(tab4);
        tabHost.addTab(tab5);
        ((TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setTextColor(Color.parseColor("#d14b8f"));

        tabHost.setOnTabChangedListener(this);

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
        // TODO Auto-generated method stub
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            ((TextView) tabHost.getTabWidget().getChildAt(i)).setTextColor(Color.BLACK);
        }

        ((TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setTextColor(Color.parseColor("#d14b8f"));
    }

    @Override
    public void onBackPressed() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit app ?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TabsActivity.super.onBackPressed();
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
