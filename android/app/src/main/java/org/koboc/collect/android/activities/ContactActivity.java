package org.koboc.collect.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.model.EmergencyContactVM;
import org.koboc.collect.android.model.SectorVM;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class ContactActivity extends Activity {

    private String BASE_URL;
    private MyApi myApi;
    private List<EmergencyContactVM> contactVMs;
    private  TableLayout tl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        BASE_URL = getApplicationContext().getString(R.string.default_java_server_url); //"http://192.168.2.60:8080";//
        tl = (TableLayout) findViewById(R.id.tableLayout1);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        myApi = restAdapter.create(MyApi.class);

        AuthUser authUser = AuthUser.findLoggedInUser();

        contactVMs=new ArrayList<EmergencyContactVM>();

        myApi.getClusters(authUser.getUserId(), new Callback<List<SectorVM>>() {
            @Override
            public void success(List<SectorVM> sectorVMList, Response response) {

                for(SectorVM vm : sectorVMList) {

                    List<EmergencyContactVM> vms1  = new ArrayList<EmergencyContactVM>();
                    vms1.addAll(vm.getInc());

                    for(EmergencyContactVM contactVM : vms1){
                        contactVMs.add(contactVM);
                    }
                }

                //System.out.println("contact size::::::::"+contactVMs.size());

                        if(contactVMs.size() > 0)
                            fillTable();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }

    private void fillTable(){
        TableRow.LayoutParams r1 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.FILL_PARENT, 10.0f);
        RelativeLayout.LayoutParams r2 = new RelativeLayout.LayoutParams(60,60);
        r2.addRule(RelativeLayout.CENTER_IN_PARENT);
        TableRow.LayoutParams r = new TableRow.LayoutParams(25,25, 10.0f);

        TableRow tr_head1 = new TableRow(this);
        //tr_head.setId(i);
        tr_head1.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        TextView label_date1 = new TextView(this);
        // label_date.setId(20);
        label_date1.setText("Contact Name");
        label_date1.setTextColor(Color.BLACK);
        label_date1.setTextSize(15);
        label_date1.setLayoutParams(r1);
        label_date1.setBackgroundResource(R.drawable.table_cells_border);
        label_date1.setPadding(10, 10, 10, 10);
        tr_head1.addView(label_date1);// add the column to the table row here

        TextView label_weight_kg1 = new TextView(this);
        // label_weight_kg.setId(21);// define id that must be unique
        label_weight_kg1.setText("Phone Number"); // set the text for the header
        label_weight_kg1.setBackgroundResource(R.drawable.table_cells_border);
        label_weight_kg1.setTextColor(Color.BLACK); // set the color
        label_weight_kg1.setTextSize(15);
        label_weight_kg1.setLayoutParams(r1);
        label_weight_kg1.setPadding(10, 10, 10, 10); // set the padding (if required)
        tr_head1.addView(label_weight_kg1); // add the column to the table row here

        RelativeLayout imageLayout = new RelativeLayout(this);
        imageLayout.setLayoutParams(r1);
        imageLayout.setBackgroundResource(R.drawable.table_cells_border);
        tr_head1.addView(imageLayout);

        tl.addView(tr_head1, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        int i=0;
        for(EmergencyContactVM contactVM : contactVMs){
            TableRow tr_head = new TableRow(this);
            //tr_head.setId(i);
            tr_head.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));



            TextView label_date = new TextView(this);
            label_date.setLayoutParams(r1);
            // label_date.setId(20);
            label_date.setText(contactVM.getName());
            label_date.setTextColor(Color.BLACK);
            label_date.setPadding(10, 10, 10, 10);
            label_date.setTextSize(15);
            label_date.setBackgroundResource(R.drawable.table_cells_border);
            tr_head.addView(label_date);// add the column to the table row here

            final TextView label_weight_kg = new TextView(this);
            label_weight_kg.setLayoutParams(r1);
            // label_weight_kg.setId(21);// define id that must be unique
            label_weight_kg.setText(contactVM.getContact()); // set the text for the header
            label_weight_kg.setTextColor(Color.BLACK); // set the color
            label_weight_kg.setTextSize(15);
            label_weight_kg.setBackgroundResource(R.drawable.table_cells_border);
            label_weight_kg.setPadding(10, 10, 10, 10); // set the padding (if required)
            tr_head.addView(label_weight_kg); // add the column to the table row here


            RelativeLayout imageLayout1 = new RelativeLayout(this);
            imageLayout1.setLayoutParams(r1);
            imageLayout1.setBackgroundResource(R.drawable.table_cells_border);
            ImageView callImage2 = new ImageView(this);
            callImage2.setLayoutParams(r2);

            callImage2.setImageResource(R.drawable.call_image);
            imageLayout1.addView(callImage2);
            tr_head.addView(imageLayout1);


            imageLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //System.out.println("Number::::"+label_weight_kg.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + label_weight_kg.getText().toString()));
                    startActivity(intent);

                }
            });

            tl.addView(tr_head, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
        }

        contactVMs.clear();

    }
}