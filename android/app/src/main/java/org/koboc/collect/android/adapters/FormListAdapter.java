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
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.koboc.collect.android.R;

public class FormListAdapter extends CursorAdapter {
    Context mContext;
    Cursor mCursor;
    LayoutInflater inflater;


    public FormListAdapter(Context context, Cursor c) {
        super(context, c);
        inflater = LayoutInflater.from(context);
        this.mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return inflater.inflate(R.layout.two_item, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1 = (TextView)view.findViewById(R.id.text1);
        TextView tv2 = (TextView)view.findViewById(R.id.text2);


        tv1.setText(mCursor.getString(1));
        System.out.println("cursor:::"+mCursor.getString(1));
        tv2.setText("akshay");
    }
}
