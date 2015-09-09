package org.koboc.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.koboc.collect.android.R;
import org.koboc.collect.android.adapters.ClusterAdapter;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.model.CaseVM;
import org.koboc.collect.android.model.ClustersVM;
import org.koboc.collect.android.model.EmergencyContactVM;
import org.koboc.collect.android.model.SectorVM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class AlertActivity extends Activity {
    private Button btnShowLocation;
	// GPSTracker class
    private GPSTracker gps;
    private MyApi myApi;
    private String BASE_URL,addressText,caseId;
    private double latitude;
    private double longitude;
    private TextView successText,message;
    private Long id;
    private RelativeLayout relativeLayout;
    private Spinner clusterSpinner;
    private List<ClustersVM> clustersVMs;
    private List<EmergencyContactVM> contactVMs;
    private String defaultSting;
    private double lon,lat;
    private  TableLayout tl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnShowLocation.setBackgroundColor(Color.RED);
        clusterSpinner= (Spinner) findViewById(R.id.clusterSpinner);
        tl = (TableLayout) findViewById(R.id.tableLayout1);

        BASE_URL = getApplicationContext().getString(R.string.default_java_server_url); //"http://192.168.2.60:8080";//

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        myApi = restAdapter.create(MyApi.class);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        clustersVMs=new ArrayList<ClustersVM>();
        contactVMs=new ArrayList<EmergencyContactVM>();
        id = System.currentTimeMillis() / 1000;  //Sangini Code+Date+Sector No.+Cluster No
        // show location button click event

        // create class object
        relativeLayout = (RelativeLayout) findViewById(R.id.messageLayout);
        message = (TextView) findViewById(R.id.message);

        successText = (TextView) findViewById(R.id.successText);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                gps = new GPSTracker(AlertActivity.this);
               if(defaultSting.equals(getApplicationContext().getString(R.string.choose_location))){
                   getLocationByGPS();
               }else{
                   registerCase();
               }
            }
        });

        AuthUser authUser = AuthUser.findLoggedInUser();
        if(authUser == null ) {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        myApi.getClusters(authUser.getUserId(), new Callback<List<SectorVM>>() {
            @Override
            public void success(List<SectorVM> sectorVMList, Response response) {
                ClustersVM clustersVM=new ClustersVM();
                clustersVM.setName(getApplicationContext().getString(R.string.choose_location));
                clustersVMs.add(clustersVM);

                for(SectorVM vm : sectorVMList) {

                    List<ClustersVM> vms = new ArrayList<ClustersVM>();
                    vms.addAll(vm.getClustervm());

                    List<EmergencyContactVM> vms1  = new ArrayList<EmergencyContactVM>();
                    vms1.addAll(vm.getInc());

                    for(ClustersVM cluster : vms ) {
                        cluster.setSector_name(vm.getName());
                        clustersVMs.add(cluster);
                    }

                    for(EmergencyContactVM contactVM : vms1){
                        contactVMs.add(contactVM);
                    }
                }

                final ClusterAdapter clusterAdapter=new ClusterAdapter(getApplicationContext(),clustersVMs);
                clusterSpinner.setAdapter(clusterAdapter);

                clusterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yy");
                        final String createdDate = sdf.format(new Date());
                        defaultSting = clusterAdapter.getItem(i).getName();

                        ClustersVM clustersVM1=clusterAdapter.getItem(i);
                        longitude = clustersVM1.getLongtitude();
                        latitude = clustersVM1.getLatitude();
                        addressText = clustersVM1.getName();

                        if(contactVMs.size() > 0)
                            fillTable();

                        //TODO: kobo need to be actual username
                        caseId=AuthUser.findLoggedInUser().getUsername() + "-" + createdDate+ "-" + clustersVM1.getSector_name() + "-" + clustersVM1.getName() ;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                    error.printStackTrace();
            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have to enable GPS to create CASE.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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


        TextView contact_title1 = new TextView(this);
        // label_date.setId(20);
        contact_title1.setText("Contact Name");
        contact_title1.setTextSize(15);
        contact_title1.setTextColor(Color.BLACK);
        contact_title1.setBackgroundResource(R.drawable.table_cells_border);
        contact_title1.setPadding(10, 10, 10, 10);
        tr_head1.addView(contact_title1);// add the column to the table row here

        TextView number_title1 = new TextView(this);
        // label_weight_kg.setId(21);// define id that must be unique
        number_title1.setText("Phone Number"); // set the text for the header
        number_title1.setBackgroundResource(R.drawable.table_cells_border);
        number_title1.setTextColor(Color.BLACK); // set the color
        number_title1.setTextSize(15);
        number_title1.setPadding(10, 10, 10, 10); // set the padding (if required)
        tr_head1.addView(number_title1); // add the column to the table row here

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


            TextView contact_name = new TextView(this);
            // label_date.setId(20);
            contact_name.setText(contactVM.getName());
            contact_name.setTextColor(Color.BLACK);
            contact_name.setTextSize(15);
            contact_name.setPadding(10, 10, 10, 10);
            contact_name.setBackgroundResource(R.drawable.table_cells_border);
            tr_head.addView(contact_name);// add the column to the table row here

            final TextView phone_number = new TextView(this);
            // label_weight_kg.setId(21);// define id that must be unique
            phone_number.setText(contactVM.getContact()); // set the text for the header
            phone_number.setTextSize(15);
            phone_number.setTextColor(Color.BLACK); // set the color
            phone_number.setBackgroundResource(R.drawable.table_cells_border);
            phone_number.setPadding(10, 10, 10, 10); // set the padding (if required)
            tr_head.addView(phone_number); // add the column to the table row here

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
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_number.getText().toString()));
                    startActivity(intent);

                }
            });

            tl.addView(tr_head, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }

        contactVMs.clear();

    }

    private void getLocationByGPS() {
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

           registerCase();

        }else{
            buildAlertMessageNoGps();
        }
    }

    private void registerCase(){


        Geocoder geocoder = new Geocoder(AlertActivity.this, Locale.getDefault());

        List<Address> addresses = null;
        System.out.println("longitude::::::"+longitude);
        System.out.println("latitude::::::"+latitude);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address returnedAddress = addresses.get(0);
            final StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
            }
            addressText = strReturnedAddress.toString();
        } catch (IOException e) {
            message.setText("Your Location latitude: " + latitude + " and longitude: " + longitude + " has been captured");
            e.printStackTrace();
        }


        AuthUser authUser = AuthUser.findLoggedInUser();
        String token = authUser.getApi_token();
        String username = authUser.getUsername();
        String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", username, token).getBytes(), Base64.NO_WRAP);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final String createdDate = sdf.format(new Date());
        CaseVM caseVM = new CaseVM(id,caseId, createdDate, createdDate, addressText, longitude, latitude);

        myApi.postCase(basicAuth, caseVM, new Callback<CaseResponseVM>() {
            @Override
            public void success(CaseResponseVM caseVM1, Response response) {
                successText.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                message.setText(addressText); //"Your Location "+address+", "+city+", "+zip+ " has been captured");
                successText.setText("Your Case is successfully posted for  ");
                successText.postDelayed(new Runnable() {
                    public void run() {
                successText.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }, 3000);


                if (caseVM1 == null) {

                    Toast.makeText(getApplicationContext(), "Not able to log case", Toast.LENGTH_SHORT);
                } else {
                    CaseRecord caseRecord = new CaseRecord(caseId,longitude, latitude, addressText, caseVM1.id, "new", createdDate, createdDate,false);
                    caseRecord.save();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Not able to log case", Toast.LENGTH_SHORT);
                error.printStackTrace();
            }
        });


    }

}