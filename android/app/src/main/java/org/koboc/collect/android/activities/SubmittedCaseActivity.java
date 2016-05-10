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
import android.util.Log;
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
    public ListView listView;
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

        caseRecord=new CaseRecord();
		//Uncomment foe new versionm
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"complete\",\"presubmitted\",\"postcomplete\",\"postsubmitted\") and uid = "+AuthUser.findLoggedInUser().getUserId());
        //caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"complete\",\"presubmitted\",\"postcomplete\",\"postsubmitted\")");

        //Database helper instance
        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT * FROM instances " , null);

        while(cursor1.moveToNext()){
            if(cursor1.getString(7).equals("submitted")){
                for (CaseRecord item:caseRecords){
                    System.out.println("Checking ::::: "+item.status);
                    System.out.println("Checking  item Id ::::: "+cursor1.getLong(9));
                    System.out.println("Checking Id ::::: "+cursor1.getLong(9));
                    if (cursor1.getLong(9) == item.caseId) {
                        System.out.println("Changed ::::: ");
                        item.isSent = true;
                        item.save();
                    }

                    if(AuthUser.findLoggedInUser().getRole().contains("consulatnt")) {
                        if (DatabaseUtility.getPost_formCount() == DatabaseUtility.getPost_InstanceCount(item.caseId + "")) {
                            Cursor cursor2 = DatabaseUtility.getPost_Instances(item.caseId+"");
                            if (cursor2.getString(7).equals("submitted") && cursor2.getString(1).contains("Post_")) {
                                item.status = "postsubmitted";
                                item.isSent = true;
                                item.save();
                            } else if (cursor2.getString(7).equals("incomplete") && cursor2.getString(1).contains("Post_")) {
                                item.status = "presubmitted";
                                item.isSent = false;
                                item.save();
                            } else {
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

                Log.d("SubmmitedCaseActivity", "instance count for case Id :" + caseRecords.get(i).caseId);
                Log.d("SubmmitedCaseActivity","instance Status for case Id :"+caseRecords.get(i).status);

                    //Intent intent = new Intent(getApplicationContext(), CompleteInstanceChooserList.class);
                    Intent intent = new Intent(getApplicationContext(), InstanceChooserList.class);
                    startActivity(intent);
                }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
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


        //caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"presubmitted\",\"submitted\",\"complete\",\"postcomplete\",\"postsubmitted\")");
		//Uncomment foe new versionm
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"presubmitted\",\"submitted\",\"complete\",\"postcomplete\",\"postsubmitted\") and uid = "+AuthUser.findLoggedInUser().getUserId());
        adapter=new UploadCaseListAdapter(SubmittedCaseActivity.this,caseRecords);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Database helper instance
        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT * FROM instances " , null);

        CaseRecord recordTest = new CaseRecord();
        //System.out.println("Testing ::::::::: "+recordTest.findWithQuery(CaseRecord.class,"Select * from Case_Record where case_Id = "+Collect.getInstance().getCaseId() ).size());
        //System.out.println("Status:::::::::"+recordTest.findWithQuery(CaseRecord.class,"Select * from Case_Record where case_Id = "+Collect.getInstance().getCaseId() ).get(0).status);
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

      // caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"presubmitted\",\"submitted\",\"complete\",\"postcomplete\",\"postsubmitted\")");
		//Uncomment foe new versionm
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"presubmitted\",\"submitted\",\"complete\",\"postcomplete\",\"postsubmitted\") and uid = "+AuthUser.findLoggedInUser().getUserId());
        adapter=new UploadCaseListAdapter(SubmittedCaseActivity.this,caseRecords);
        listView.setAdapter(adapter);

    }

	public void resume(){

		InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);

		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		Cursor cursor1 = database.rawQuery("SELECT * FROM instances " , null);

		CaseRecord recordTest = new CaseRecord();
		//System.out.println("Testing ::::::::: "+recordTest.findWithQuery(CaseRecord.class,"Select * from Case_Record where case_Id = "+Collect.getInstance().getCaseId() ).size());
		//System.out.println("Status:::::::::"+recordTest.findWithQuery(CaseRecord.class,"Select * from Case_Record where case_Id = "+Collect.getInstance().getCaseId() ).get(0).status);
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
		caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"presubmitted\",\"submitted\",\"complete\",\"postcomplete\",\"postsubmitted\")");
		adapter=new UploadCaseListAdapter(SubmittedCaseActivity.this,caseRecords);
		listView.setAdapter(adapter);

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
