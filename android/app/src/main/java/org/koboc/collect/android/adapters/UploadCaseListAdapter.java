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

import android.app.Activity;
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
import android.widget.Toast;

import org.koboc.collect.android.R;
import org.koboc.collect.android.activities.FormEntryActivity;
import org.koboc.collect.android.activities.InstanceUploaderActivity;
import org.koboc.collect.android.activities.SubmittedCaseActivity;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.provider.InstanceProvider;
import org.koboc.collect.android.utilities.DatabaseUtility;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadCaseListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity mContext;
    private List<CaseRecord> mItems = new ArrayList<CaseRecord>();
    private RelativeLayout relativeLayout;
    private SQLiteDatabase db;
    private TextView textView,textView1,textView2;
    private Button uploadButton,delete_button;

    public UploadCaseListAdapter(Activity context, List<CaseRecord> items) {
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

     //   if (convertView == null)
        convertView = inflater.inflate(R.layout.upload_case_list_item, null);
        textView= (TextView) convertView.findViewById(R.id.caseIdText);
        relativeLayout= (RelativeLayout) convertView.findViewById(R.id.mainlayout1);
        textView1= (TextView) convertView.findViewById(R.id.dateText);
        textView2= (TextView) convertView.findViewById(R.id.addressText);
        uploadButton = (Button) convertView.findViewById(R.id.upload_button);
		delete_button = (Button) convertView.findViewById(R.id.delete_button);
        final CaseRecord item=mItems.get(position);

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

        if(item.status.equals("incomplete")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_yellow);
        }

        if(item.status.equals("complete") || item.status.equals("presubmitted") || item.status.equals("postcomplete") || item.status.equals("postsubmitted") || item.status.equals("submitted")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_green);
        }

        if(item.status.equals("new")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_blue);
        }

        if(isPostUploaded(item.caseId+"")){
            uploadButton.setVisibility(View.GONE);
			delete_button.setVisibility(View.VISIBLE);
        }



        if(item.status.equals("postsubmitted") || item.status.equals("submitted") && !item.status.equals("presubmitted") && !item.status.equals("postcomplete") && !item.status.equals("complete")){
            uploadButton.setVisibility(View.GONE);
			delete_button.setVisibility(View.VISIBLE);
        }

        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper("instances.db");
        db = databaseHelper.getWritableDatabase();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collect.getInstance().setCaseId(item.caseId+"");

                if (AuthUser.findLoggedInUser().getRole().equals("consultant")) {
                    //if(DatabaseUtility.getPost_InstanceCount(item.caseId+"") == DatabaseUtility.getPost_formCount()){
                    System.out.println("logged user is consultant :::::::");
                    Cursor cursor  = DatabaseUtility.getPost_InstancesForUpload(item.caseId+"");
                    System.out.println("Post instance :::::::"+cursor.getCount());
                    Collect.getInstance().formType = "presubmit";
                    if(cursor.getCount() == 0){
                        Toast.makeText(mContext,"Please fill all the Forms",Toast.LENGTH_LONG).show();
                    }

                    while(cursor.moveToNext()) {
                        System.out.println("isAllComplete(item.caseId) :::::::"+isAllComplete(item.caseId+""));
                        if (isAllComplete(item.caseId + "")) {
                            System.out.println("isAllComplete:::::::::::");
                            System.out.println("getString(1) :::::::::::"+cursor.getString(1));
                            System.out.println("getString(9):::::::::::"+cursor.getString(9));
                            System.out.println("item case id :::::::::::"+item.caseId);
                            if (cursor.getString(1).contains("Post_") && cursor.getString(9).equals(item.caseId))
                                System.out.println("near to upload ::::::"+cursor.getString(0));
                                upload(Long.parseLong(cursor.getString(0)));

                        } else {
                            Toast.makeText(mContext, "Please fill all the Forms", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    //}
                } else {
                    Cursor cursor = db.rawQuery("SELECT * FROM instances where caseId = " + item.caseId, null);
                    while (cursor.moveToNext()) {
                        if (cursor.getString(7).equals("complete") || cursor.getString(7).equals("submissionFailed")) {
                            upload(Long.parseLong(cursor.getString(0)));
                        }

                    }

                }
            }
        });

		delete_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				CaseRecord record = new CaseRecord();
				List<CaseRecord> list = record.findWithQuery(CaseRecord.class, "Select * from Case_Record", null);
				System.out.println("before size :::: "+list.size());

				for(CaseRecord record1 : list){
					System.out.println("item :::: "+record1.caseId);
				}

				CaseRecord.deleteAll(CaseRecord.class, "case_id = ?", item.caseId+"");

				List<CaseRecord> list1 = record.findWithQuery(CaseRecord.class, "Select * from Case_Record", null);
				System.out.println("after size :::: "+list1.size());

				for(CaseRecord record1 : list1){
					System.out.println("item 1:::: "+record1.caseId);
				}

				((SubmittedCaseActivity)mContext).resume();

			}
		});

        convertView.setTag(mItems);
        uploadButton.setTag(item);
        delete_button.setTag(item);
        return convertView;
    }

    private boolean isAllComplete(String id){
        Cursor cursor = DatabaseUtility.getPost_Instances(id);
        if(cursor.getCount() == 0){
            return false;
        }else if(DatabaseUtility.getPost_formCount() == DatabaseUtility.getPost_InstanceCount(id)){
        while(cursor.moveToNext()) {
            if (cursor.getString(7).equals("incomplete")) {
                return false;
            }
         }
        }else{
            return false;
        }
        return true;
    }

    private boolean isPostUploaded(String id){
        Cursor cursor = DatabaseUtility.getPost_Instances(id);
        if(cursor.getCount() == 0){
            return false;
        }else if(DatabaseUtility.getPost_formCount() == DatabaseUtility.getPost_InstanceCount(id)){
            while(cursor.moveToNext()) {
                if (!cursor.getString(7).equals("submitted")) {
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }
    private void upload(Long id){
        System.out.println("in upload ::::::");
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
