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

import java.util.ArrayList;
import java.util.List;

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
        CaseRecord item=mItems.get(position);
        textView.setText(item.caseId+"");
        if(item.status.equals("new")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_yellow);
        }
        if(item.status.equals("draft")){
            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_blue);
        }

        return convertView;
    }
}
