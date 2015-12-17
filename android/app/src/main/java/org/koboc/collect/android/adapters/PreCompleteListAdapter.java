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

package org.koboc.collect.android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.activities.FormEntryActivity;
import org.koboc.collect.android.activities.InstanceUploaderActivity;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.provider.InstanceProvider;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreCompleteListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<CaseRecord> mItems = new ArrayList<CaseRecord>();
    private RelativeLayout relativeLayout;
    private SQLiteDatabase db;


    public PreCompleteListAdapter(Context context, List<CaseRecord> items) {
        mContext = context;
        mItems=items;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }


    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       // if (convertView == null)
            convertView = inflater.inflate(R.layout.pre_case_layout, null);
        TextView textView= (TextView) convertView.findViewById(R.id.caseIdText);
        relativeLayout= (RelativeLayout) convertView.findViewById(R.id.mainlayout1);
        TextView textView1= (TextView) convertView.findViewById(R.id.dateText);
        TextView textView2= (TextView) convertView.findViewById(R.id.addressText);
        Button uploadButton = (Button) convertView.findViewById(R.id.upload_button);
        final CaseRecord item=mItems.get(position);

        System.out.println("adapter::::::::::"+mItems.size());

        textView.setText(item.displayId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("'Created on' EEE, MMM dd, yyyy 'at' HH:mm");
        Date d = null;
        try {
            d = sdf.parse(item.dateCreated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = output.format(d);

        textView1.setText(formattedTime);

        textView2.setText(item.address);

        System.out.println("status::::::::::"+item.status);

        if(item.status.equals("incomplete") || item.status.equals("precomplete") || item.status.equals("presubmitted")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_yellow);
        }

        if(item.status.equals("complete")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_green);
        }

        if(item.status.equals("new")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_blue);
        }
        Date date1 = new Date();
        System.out.println("date::::::::"+item.dateCreated);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy/mm/dd");
        try {
            System.out.println("date 1::::::::"+item.dateCreated);
            date1 = sdf.parse(item.dateCreated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = new Date();
        long diff = date2.getTime() - date1.getTime();
        System.out.println("day difference::::::::::"+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 5) {
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_red);
        }

        if(item.status.equals("presubmitted")){
            uploadButton.setVisibility(View.GONE);
        }

        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper("instances.db");
        db = databaseHelper.getWritableDatabase();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collect.getInstance().setCaseId(item.caseId+"");
                Collect.getInstance().formType = "precomplete";
                Cursor cursor = db.rawQuery("SELECT * FROM instances where caseId = " +item.caseId+ " and status in (\"complete\",\"submissionFailed\")", null);

                while(cursor.moveToNext()){
                    System.out.println("instance id::::::::"+Long.parseLong(cursor.getString(0)));
                    if(cursor.getString(1).contains("Pre_"))
                         upload(Long.parseLong(cursor.getString(0)));
                }

            }
        });

        return convertView;
    }

    private void upload(Long id){
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        WifiManager wifi = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);

        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }

        long[] instanceId=new long[1];
        //instanceId[0]=Long.parseLong(getIntent().getData().getPathSegments().get(1));
        instanceId[0]=id;
        if(!mobileDataEnabled && !wifi.isWifiEnabled()){
            buildAlertMessageNoInternet();
        }else{

            Intent i = new Intent(mContext, InstanceUploaderActivity.class);
            i.putExtra(FormEntryActivity.KEY_INSTANCES, instanceId);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);

                    /*saveDataToDisk(EXIT, instanceComplete.isChecked(), saveAs
                            .getText().toString());*/
        }
    }
    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("You have to enable Internet Connection to upload form.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        mContext.startActivity(
                                new Intent(Settings.ACTION_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
