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
import org.koboc.collect.android.adapters.PreCompleteListAdapter;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.provider.FormsProvider;
import org.koboc.collect.android.provider.InstanceProvider;
import org.koboc.collect.android.utilities.DatabaseUtility;

import java.util.List;


public class PreCompleteActivity extends Activity{
    private  FormsProvider.DatabaseHelper databaseHelperForm = new FormsProvider.DatabaseHelper("forms.db");
    private  InstanceProvider.DatabaseHelper databaseHelperInstance  = new InstanceProvider.DatabaseHelper("instances.db");
    private  Cursor formsCursor,instanceCursor1;
    private  SQLiteDatabase database;
    private boolean preFlag=false,postFlag=false;
    private List<CaseRecord> preCompleteList;
    private PreCompleteListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_case_layout);
        listView = (ListView) findViewById(R.id.caseList);

        database = databaseHelperInstance.getWritableDatabase();

        CaseRecord caseRecord = new CaseRecord();

		//uncomment for new version
		List<CaseRecord> list = caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ? and uid = ?","complete", AuthUser.findLoggedInUser().getUserId()+"");
        //List<CaseRecord> list = caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");

        //System.out.println("pre list sized  :: "+list.size());
        int i=0;
		int  Post_formCount = DatabaseUtility.getPost_formCount();
        for(CaseRecord record : list){

            if(!record.status.equals("presubmitted"))

            if (isAllComplete(record.caseId+""))
            {
              //  List<CaseRecord> caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ?",record.caseId+"");

				//uncomment for new version
                List<CaseRecord> caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ? and uid = ?",record.caseId+"",AuthUser.findLoggedInUser().getUserId()+"");
                //System.out.println("Case record size::::"+caseRecords.size());
                //System.out.println("Precomplete set .. "+caseRecords.size());
                CaseRecord caseRecord1 = caseRecords.get(0);
                caseRecord1.status = "precomplete";
                caseRecord1.save();
            }





/*
            if(DatabaseUtility.getPre_formCount() == DatabaseUtility.getPre_InstanceCount(record.caseId+"")){
                Cursor cursor = DatabaseUtility.getPre_Instances(record.caseId+"");
                while(cursor.moveToNext()){
                    System.out.println("i +++++++"+i);
                    i++;
                    if(cursor.getString(7).equals("complete") && cursor.getString(1).contains("Pre_")){
                       System.out.println("true::");
                        preFlag = true;
                    }else{
                        System.out.println("false::");
                        preFlag = false;
                    }
                }
                if(isAllComplete(Collect)){
                    List<CaseRecord> caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ?",record.caseId+"");
                    System.out.println("Case record size::::"+caseRecords.size());
                    System.out.println("Precomplete set .. "+caseRecords.size());
                    CaseRecord caseRecord1 = caseRecords.get(0);
                    caseRecord1.status = "precomplete";
                    caseRecord1.save();
                }
            }*/


            //post forms
            //System.out.println("post form count:::"+DatabaseUtility.getPre_formCount());
            //System.out.println("post instance count:::"+DatabaseUtility.getPre_InstanceCount(record.caseId + ""));
            if(Post_formCount == DatabaseUtility.getPost_InstanceCount(record.caseId+"")){
                Cursor cursor = DatabaseUtility.getPost_Instances(record.caseId+"");
                try {
					while (cursor.moveToNext()) {
						if (cursor.getString(7).equals("complete")) {
							postFlag = true;
						}
					}
				} finally {
					if(cursor != null) {
						cursor.close();
					}
				}
			}

        }

       // preCompleteList = caseRecord.findWithQuery(CaseRecord.class,"Select * from Case_Record where status in (\"precomplete\")");
		//uncomment for new version
        preCompleteList = caseRecord.findWithQuery(CaseRecord.class,"Select * from Case_Record where status in (\"precomplete\") and uid = ?",AuthUser.findLoggedInUser().getUserId()+"");
        //System.out.println("preCompleteList::::"+preCompleteList.size());
        adapter = new PreCompleteListAdapter(PreCompleteActivity.this,preCompleteList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Collect.getInstance().getActivityLogger().logAction(this, "fillBlankForm", "click");
                Collect.getInstance().setCaseId(preCompleteList.get(i).caseId+"");

                Intent intent = new Intent(getApplicationContext(), InstanceChooserList.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getApplicationContext().getString(R.string.exitappmsg))
                .setCancelable(false)
                .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PreCompleteActivity.super.onBackPressed();
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

    private boolean isAllComplete(String id){

			Cursor cursor = DatabaseUtility.getPre_Instances(id);
		try {
			//System.out.println("isAllComplete call...");
			if (cursor.getCount() == 0) {
				return false;
			} else if (DatabaseUtility.getPre_formCount() == DatabaseUtility.getPre_InstanceCount(id)) {
				while (cursor.moveToNext()) {
					if (!cursor.getString(7).equals("complete")) {
						return false;
					}
				}
			} else {
				return false;
			}
			return true;
		}finally {
			if(cursor != null) {
				cursor.close();
			}
		}
    }
}
