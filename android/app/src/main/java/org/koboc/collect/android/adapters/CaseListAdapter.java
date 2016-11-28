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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.koboc.collect.android.R;
import org.koboc.collect.android.activities.MyCaseActivity;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.provider.FormsProvider;
import org.koboc.collect.android.provider.InstanceProvider;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class CaseListAdapter extends BaseAdapter {

	private static final String DATABASE_NAME = "instances.db";
	private static final String DATABASE_NAME1 = "forms.db";
	SQLiteDatabase db;
    private LayoutInflater inflater;
    private Context mContext;
    private List<CaseRecord> mItems = new ArrayList<CaseRecord>();
    private RelativeLayout relativeLayout;
	private MyApi myApi;
	private String BASE_URL;

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
		Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
        relativeLayout= (RelativeLayout) convertView.findViewById(R.id.mainlayout1);
        TextView textView1= (TextView) convertView.findViewById(R.id.dateText);
        TextView textView2= (TextView) convertView.findViewById(R.id.addressText);
        final CaseRecord item=mItems.get(position);

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
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy/mm/dd");
        try {
            date1 = sdf.parse(item.dateCreated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = new Date();
        long diff = date2.getTime() - date1.getTime();
        /*if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 5) {

		//	TODO : Remove red color from conseller

            relativeLayout.setBackgroundResource(R.drawable.rect_border_community_red);
        }*/

		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog(item.caseId);
			}
		});

        return convertView;
    }

	private void showDialog(final Long caseId) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage("Do you want to delete the case and related forms ?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
						isCaseExist(caseId);

				}
				}).setNegativeButton("No",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	private void isCaseExist(final Long id){


		BASE_URL = mContext.getString(R.string.default_java_server_url);


		AuthUser authUser = AuthUser.findLoggedInUser();
		String token = authUser.getApi_token();
		String username = authUser.getUsername();
		String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", username, token).getBytes(), Base64.NO_WRAP);

		InstanceProvider.DatabaseHelper databaseHelper = new InstanceProvider.DatabaseHelper(DATABASE_NAME);
		db = databaseHelper.getWritableDatabase();

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
				.setClient(new OkClient()).build();
		myApi = restAdapter.create(MyApi.class);


		myApi.isCaseDeleted(basicAuth,id,new Callback<Boolean>() {
			@Override
			public void success(Boolean caseResponseVMs, Response response) {
				//System.out.println("success :::: "+caseResponseVMs);

				if(caseResponseVMs) {
					List<String> instances = new ArrayList<String>();
					Cursor cursor = db.rawQuery("SELECT * FROM instances where caseId = " + id, null);
					//System.out.println("cursor ::: " + cursor.getCount());

					while (cursor.moveToNext()) {
						//System.out.println("path :::: " + cursor.getString(4));
						instances.add(cursor.getString(4));
					}
					if(cursor != null) {
						cursor.close();
					}
					for (String s : instances) {
						String path = s.substring(0, s.lastIndexOf('/'));
						File file = new File(path);
						//Boolean aBoolean = file.delete();
						//System.out.println("deleteDirectory :::: " + s);
						//System.out.println("deleteDirectory :::: " + file);
						deleteDirectory(file);
					}
					db.execSQL("delete from instances where caseId = " + id);
					CaseRecord.deleteAll(CaseRecord.class, "case_id = ?", id + "");
					((MyCaseActivity) mContext).onResume();

				}else{
					Toast.makeText(mContext, "Sorry. You can'delete this case.", Toast.LENGTH_LONG).show();
				}


			}

			@Override
			public void failure(RetrofitError error) {

				error.printStackTrace();

			}
		});


	}

	public static boolean deleteDirectory(File path) {
		if( path.exists() ) {
			//System.out.println("deleteDirectory :::: ");
			File[] files = path.listFiles();
			//System.out.println("deleteDirectory files:::: "+files.length);
			if (files == null) {
				return true;
			}
			for(int i=0; i<files.length; i++) {
			//	System.out.println("isDirectory :::: "+files[i].isDirectory());
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return( path.delete() );
	}


}
