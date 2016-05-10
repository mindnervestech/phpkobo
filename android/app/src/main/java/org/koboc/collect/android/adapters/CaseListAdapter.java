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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.database.CaseRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CaseListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<CaseRecord> mItems = new ArrayList<CaseRecord>();
    private RelativeLayout relativeLayout;


    public CaseListAdapter(Context context,List<CaseRecord> items) {
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

      if (convertView == null)
            convertView = inflater.inflate(R.layout.case_list_item, null);
        TextView textView= (TextView) convertView.findViewById(R.id.caseIdText);
        relativeLayout= (RelativeLayout) convertView.findViewById(R.id.mainlayout1);
        TextView textView1= (TextView) convertView.findViewById(R.id.dateText);
        TextView textView2= (TextView) convertView.findViewById(R.id.addressText);
        final CaseRecord item=mItems.get(position);

        System.out.println("adapter::::::::::"+mItems.size());
        System.out.println("display id::::::::::"+item.displayId);


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

        if(item.status.equals("incomplete") || item.status.equals("precomplete")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_yellow);
        }

        if(item.status.equals("complete")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_green);
        }

        if(item.status.equalsIgnoreCase("new")){
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
        /*if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 5) {

		//	TODO : Remove red color from conseller

            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_red);
        }*/
        return convertView;
    }
}
