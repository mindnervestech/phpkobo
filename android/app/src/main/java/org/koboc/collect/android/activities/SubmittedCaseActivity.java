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

package org.koboc.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.adapters.UploadCaseListAdapter;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.provider.InstanceProvider;
import org.koboc.collect.android.utilities.DatabaseUtility;

import java.util.List;

public class SubmittedCaseActivity extends Activity{
    private ListView listView;
    private UploadCaseListAdapter adapter;
    private CaseRecord caseRecord;
    private List<CaseRecord> caseRecords;
    private static final String DATABASE_NAME = "instances.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_case_layout);

        listView= (ListView) findViewById(R.id.caseList);

        long cnt=CaseRecord.count(CaseRecord.class,null,null);
        System.out.println("count:::"+cnt);

        caseRecord=new CaseRecord();
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"complete\",\"presubmitted\",\"postcomplete\",\"postsubmitted\")");
        System.out.println("total records ::::"+caseRecords.size());

        //Database helper instance
        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT * FROM instances " , null);

        while(cursor1.moveToNext()){
            if(cursor1.getString(7).equals("submitted")){
                for (CaseRecord item:caseRecords){

                    if (cursor1.getLong(9) == item.caseId) {
                        item.isSent = true;
                        item.save();
                    }


                    if(AuthUser.findLoggedInUser().getRole().contains("consulatnt")) {
                        System.out.println("consultant::::::::::::::::::");
                        if (DatabaseUtility.getPost_formCount() == DatabaseUtility.getPost_InstanceCount(item.caseId + "")) {
                            Cursor cursor2 = DatabaseUtility.getPost_Instances(item.caseId+"");
                            if (cursor2.getString(7).equals("submitted") && cursor2.getString(1).contains("Post_")) {
                                System.out.println("postsubmitted set ::");
                                item.status = "postsubmitted";
                                item.isSent = true;
                                item.save();
                            } else if (cursor2.getString(7).equals("incomplete") && cursor2.getString(1).contains("Post_")) {
                                System.out.println("presubmitted set ::");
                                item.status = "presubmitted";
                                item.isSent = false;
                                item.save();
                            } else {
                                System.out.println("postcomplete set ::");
                                item.status = "postcomplete";
                                item.isSent = false;
                                item.save();
                            }
                        }
                    }



                }
            }
        }





        adapter=new UploadCaseListAdapter(SubmittedCaseActivity.this,caseRecords);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Collect.getInstance().getActivityLogger().logAction(this, "fillBlankForm", "click");
                Collect.getInstance().setCaseId(caseRecords.get(i).caseId+"");
                    //Intent intent = new Intent(getApplicationContext(), CompleteInstanceChooserList.class);
                    Intent intent = new Intent(getApplicationContext(), InstanceChooserList.class);
                    startActivity(intent);
                }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart ::::");

        //Database helper instance
        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT * FROM instances " , null);

       /* while(cursor1.moveToNext()){
            if(cursor1.getString(7).equals("submitted")){
                for (CaseRecord item:caseRecords){
                    if(cursor1.getLong(9) == item.caseId) {
                        item.isSent = true;
                        item.save();
                    }

                }
            }
        }*/


        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"presubmitted\",\"submitted\",\"complete\",\"postcomplete\",\"postsubmitted\")");
        adapter=new UploadCaseListAdapter(SubmittedCaseActivity.this,caseRecords);
        System.out.println("total records ::::"+caseRecords.size());
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Database helper instance
        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT * FROM instances " , null);

        /*while(cursor1.moveToNext()){
            if(cursor1.getString(7).equals("submitted")){
                for (CaseRecord item:caseRecords){
                    if(cursor1.getLong(9) == item.caseId) {
                        item.isSent = true;
                        item.save();
                    }

                }
            }
        }*/
        System.out.println("onResume ::::");
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"presubmitted\",\"submitted\",\"complete\",\"postcomplete\",\"postsubmitted\")");
        adapter=new UploadCaseListAdapter(SubmittedCaseActivity.this,caseRecords);
        listView.setAdapter(adapter);
        System.out.println("total records ::::"+caseRecords.size());

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getApplicationContext().getString(R.string.exitappmsg))
                .setCancelable(false)
                .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SubmittedCaseActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(getApplicationContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
