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
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.koboc.collect.android.R;
import org.koboc.collect.android.adapters.CaseListAdapter;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.provider.FormsProvider;
import org.koboc.collect.android.provider.InstanceProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class MyCaseActivity extends Activity{
    private ListView listView;
    private CaseListAdapter adapter;
    private  CaseRecord caseRecord;
    private static final String DATABASE_NAME = "instances.db";
    private static final String DATABASE_NAME1 = "forms.db";
    SQLiteDatabase db;
    Cursor cursor1;
    private MyApi myApi;
    private String BASE_URL;
    private AuthUser user;
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

        user = AuthUser.findLoggedInUser();
        if(user.getRole().contains("consultant")){
           // getConsultantCase();
        }


        long cnt=CaseRecord.count(CaseRecord.class,null,null);
        System.out.println("count:::"+cnt);

        caseRecord=new CaseRecord();
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"incomplete\",\"new\")");
        System.out.println("total records ::::"+caseRecords.size());


        for(CaseRecord item : caseRecords){
            System.out.println("status::::::::"+item.status);

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

            /*if(cursor.getCount() == cursor1.getCount()) {
                while (cursor.moveToNext()) {
                    System.out.println("cursor:::::::::" + cursor.getString(7));
                    if (cursor.getString(7).equals("complete")) {
                        isComplete = true;
                    }
                }
            }*/

            /*if(isComplete){
                System.out.println("flag:::::::::::::::");
                List<CaseRecord> list=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where Case_Id = ?",item.caseId+"");
                CaseRecord record = new CaseRecord();
                record = list.get(0);
                System.out.println("flag id:::::::::::::::"+record.caseId);
                record.status = "complete";
                record.save();
            }*/
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

            for (CaseRecord item : caseRecords) {
                //Instance Table Query
                Boolean isComplete = false;
                Cursor cursor = db.rawQuery("SELECT * FROM instances where caseId = " + item.caseId, null);

                if (cursor.getCount() == 0) {
                    List<CaseRecord> list = caseRecord.findWithQuery(CaseRecord.class, "SELECT * FROM Case_Record where Case_Id = ?", item.caseId + "");
                    CaseRecord record = new CaseRecord();
                    record = list.get(0);
                    record.status = "new";
                    record.save();
                }

                /*if (cursor.getCount() == cursor1.getCount()) {
                    while (cursor.moveToNext()) {
                        if (cursor.getString(7).equals("complete")) {
                            isComplete = true;
                        }
                    }
                }
*/
               /* if (isComplete) {
                    List<CaseRecord> list = caseRecord.findWithQuery(CaseRecord.class, "SELECT * FROM Case_Record where Case_Id = ?", item.caseId + "");
                    CaseRecord record = new CaseRecord();
                    record = list.get(0);
                    record.status = "complete";
                    record.save();
                }*/
            }


            System.out.println("onResume :::");
            caseRecords = caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status in (\"incomplete\",\"new\")");
            adapter = new CaseListAdapter(getApplicationContext(), caseRecords);
            listView.setAdapter(adapter);
            System.out.println("total records ::::" + caseRecords.size());

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getApplicationContext().getString(R.string.exitappmsg))
                .setCancelable(false)
                .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MyCaseActivity.super.onBackPressed();
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

    public void getConsultantCase(){

        BASE_URL = getApplicationContext().getString(R.string.default_java_server_url);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient()).build();
        myApi = restAdapter.create(MyApi.class);

        AuthUser authUser = AuthUser.findLoggedInUser();
        String token = authUser.getApi_token();
        String username = authUser.getUsername();
        System.out.println("authUser::" + authUser.getUsername());
        System.out.println("token::" + token);
        String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", username, token).getBytes(), Base64.NO_WRAP);

        System.out.println("in consultant auth getConsultantCase  :::"+basicAuth);

        myApi.getCase(basicAuth, new Callback<List<CaseResponseVM>>() {
            @Override
            public void success(List<CaseResponseVM> caseVMList, Response response) {

                System.out.println("refresh case size::::::"+caseVMList.size());

                CaseRecord.deleteAll(CaseRecord.class);
                for(CaseResponseVM crVm : caseVMList){
                    CaseRecord cr = new CaseRecord();
                    cr.caseId = crVm.id;
                    cr.status = crVm.status;
                    cr.latitude = crVm.latitude;
                    cr.longitude = crVm.longitude;

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
                }
               // caseRecords.clear();
                caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                //TODO : Handle null pointer here
                Toast.makeText(getApplicationContext(), "Unable to connect server ... ", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}
