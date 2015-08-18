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
import org.koboc.collect.android.model.InstanceVM;

import java.util.ArrayList;
import java.util.List;

public class InstanceListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<InstanceVM> mItems = new ArrayList<InstanceVM>();
    private RelativeLayout relativeLayout;


    public InstanceListAdapter(Context context, List<InstanceVM> items) {
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
            convertView = inflater.inflate(R.layout.two_item, null);
        TextView title = (TextView) convertView.findViewById(R.id.text1);
        TextView subTitle = (TextView) convertView.findViewById(R.id.text2);

        InstanceVM vm = mItems.get(position);

        title.setText(vm.getForm_id());
        subTitle.setText(vm.getSubTitle());

        System.out.println("adapter::::::::::"+mItems.size());

        return convertView;
    }
}
