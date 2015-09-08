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
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.adapters.CaseListAdapter;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.provider.FormsProvider;
import org.koboc.collect.android.provider.InstanceProvider;

import java.util.List;

public class MyCaseActivity extends Activity{
    private ListView listView;
    private CaseListAdapter adapter;
    private  CaseRecord caseRecord;
    private static final String DATABASE_NAME = "instances.db";
    private static final String DATABASE_NAME1 = "forms.db";
    SQLiteDatabase db;
    Cursor cursor1;

    private List<CaseRecord> caseRecords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_case_layout);

        listView= (ListView) findViewById(R.id.caseList);

        //Database helper instance
        InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);
        FormsProvider.DatabaseHelper databaseHelper1 = new FormsProvider.DatabaseHelper("forms.db");
        db = databaseHelper.getWritableDatabase();

        //Form Table Query
        SQLiteDatabase database = databaseHelper1.getWritableDatabase();
         cursor1 = database.rawQuery("SELECT * FROM forms" , null);


        long cnt=CaseRecord.count(CaseRecord.class,null,null);
        System.out.println("count:::"+cnt);

        caseRecord=new CaseRecord();
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");
        System.out.println("total records ::::"+caseRecords.size());


        for(CaseRecord item : caseRecords){
            //Instance Table Query
            Boolean isComplete = false;
            Cursor cursor = db.rawQuery("SELECT * FROM instances where caseId = " +item.caseId, null);

            System.out.println("total instance::::::::"+cursor.getCount());
            System.out.println("total forms::::::::"+cursor1.getCount());

            if(cursor.getCount() == 0){
                List<CaseRecord> list=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ?",item.caseId+"");
                CaseRecord record = new CaseRecord();
                record = list.get(0);
                record.status = "new";
                record.save();
            }

            if(cursor.getCount() == cursor1.getCount()) {
                while (cursor.moveToNext()) {
                    System.out.println("cursor:::::::::" + cursor.getString(7));
                    if (cursor.getString(7).equals("complete")) {
                        isComplete = true;
                    }
                }
            }

            if(isComplete){
                System.out.println("flag:::::::::::::::");
                List<CaseRecord> list=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ?",item.caseId+"");
                CaseRecord record = new CaseRecord();
                record = list.get(0);
                System.out.println("flag id:::::::::::::::"+record.caseId);
                record.status = "complete";
                record.save();
            }
        }


        System.out.println("total records ::::"+caseRecords.size());

        adapter=new CaseListAdapter(getApplicationContext(),caseRecords);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Collect.getInstance().getActivityLogger().logAction(this, "fillBlankForm", "click");
                Collect.getInstance().setCaseId(caseRecords.get(i).caseId+"");

                InstanceProvider instanceProvider=new InstanceProvider();
                int count = instanceProvider.checkInstance(caseRecords.get(i).caseId);

                System.out.println("count instance:::::::"+count);

                /*if(count == 0){
                    Intent intent = new Intent(getApplicationContext(), FormChooserList.class);
                    startActivity(intent);      // commented by Akshay to mix instance and forms
                }else {*/
                    Intent intent = new Intent(getApplicationContext(), InstanceChooserList.class);
                    startActivity(intent);
            //    }
                System.out.println("total cases::"+caseRecords.size());
                System.out.println("status cases::"+caseRecords.get(i).status);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart :::");
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");
        adapter=new CaseListAdapter(getApplicationContext(),caseRecords);
        listView.setAdapter(adapter);
        System.out.println("total records ::::"+caseRecords.size());
    }

    @Override
    public void onResume() {
        super.onResume();

        for(CaseRecord item : caseRecords){
            //Instance Table Query
            Boolean isComplete = false;
            Cursor cursor = db.rawQuery("SELECT * FROM instances where caseId = " +item.caseId, null);

            System.out.println("total instance::::::::"+cursor.getCount());
            System.out.println("total forms::::::::"+cursor1.getCount());

            if(cursor.getCount() == 0){
                List<CaseRecord> list=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ?",item.caseId+"");
                CaseRecord record = new CaseRecord();
                record = list.get(0);
                record.status = "new";
                record.save();
            }

            if(cursor.getCount() == cursor1.getCount()) {
                while (cursor.moveToNext()) {
                    System.out.println("cursor:::::::::" + cursor.getString(7));
                    if (cursor.getString(7).equals("complete")) {
                        isComplete = true;
                    }
                }
            }

            if(isComplete){
                System.out.println("flag:::::::::::::::");
                List<CaseRecord> list=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ?",item.caseId+"");
                CaseRecord record = new CaseRecord();
                record = list.get(0);
                System.out.println("flag id:::::::::::::::"+record.caseId);
                record.status = "complete";
                record.save();
            }
        }


        System.out.println("onResume :::");
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");
        adapter=new CaseListAdapter(getApplicationContext(),caseRecords);
        listView.setAdapter(adapter);
        System.out.println("total records ::::"+caseRecords.size());
    }
}
