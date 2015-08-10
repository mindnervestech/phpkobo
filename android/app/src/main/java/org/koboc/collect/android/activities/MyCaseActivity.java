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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.adapters.CaseListAdapter;
import org.koboc.collect.android.application.Collect;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.provider.InstanceProvider;

import java.util.List;

public class MyCaseActivity extends Activity{
    private ListView listView;
    private CaseListAdapter adapter;
    private  CaseRecord caseRecord;
    private List<CaseRecord> caseRecords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_case_layout);

        listView= (ListView) findViewById(R.id.caseList);

        long cnt=CaseRecord.count(CaseRecord.class,null,null);
        System.out.println("count:::"+cnt);



        caseRecord=new CaseRecord();
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");
        System.out.println("size:::::::::::"+caseRecords.size());

        for (CaseRecord item:caseRecords){
            System.out.println("caseId::::"+item.caseId);
            System.out.println("long::::"+item.longitude);
            System.out.println("latt::::"+item.latitude);
            System.out.println("addrress::::"+item.address);
            System.out.println("status::::"+item.status);
            System.out.println("date cre::::"+item.dateCreated);
            System.out.println("date mod::::"+item.dateModified);

        }

        adapter=new CaseListAdapter(getApplicationContext(),caseRecords);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Collect.getInstance().getActivityLogger().logAction(this, "fillBlankForm", "click");
                Collect.getInstance().setCaseId(caseRecords.get(i).caseId+"");

                InstanceProvider instanceProvider=new InstanceProvider();
                instanceProvider.checkInstance(caseRecords.get(i).caseId);

                if(instanceProvider.checkInstance(caseRecords.get(i).caseId) == 0){
                    Intent intent = new Intent(getApplicationContext(), FormChooserList.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), InstanceChooserList.class);
                    startActivity(intent);
                }

                System.out.println("total cases::"+caseRecords.size());



            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");
        adapter=new CaseListAdapter(getApplicationContext(),caseRecords);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record where status != ?","complete");
        adapter=new CaseListAdapter(getApplicationContext(),caseRecords);
        listView.setAdapter(adapter);

    }
}
