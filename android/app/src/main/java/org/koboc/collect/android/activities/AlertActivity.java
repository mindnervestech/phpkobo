package org.koboc.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.adapters.ClusterAdapter;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.model.CaseVM;
import org.koboc.collect.android.model.ClustersVM;
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
	
	Button btnShowLocation;
	
	// GPSTracker class
	GPSTracker gps;
    private MyApi myApi;
    private String BASE_URL,addressText,caseId;
    private double latitude;
    private double longitude;
    private TextView successText;
    private Long id;
    RelativeLayout relativeLayout;
    private Spinner clusterSpinner;
    private List<ClustersVM> clustersVMs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnShowLocation.setBackgroundColor(Color.RED);
        clusterSpinner= (Spinner) findViewById(R.id.clusterSpinner);

        BASE_URL = "http://192.168.2.70:8080";//getApplicationContext().getString(R.string.base_url); //"http://192.168.2.60:8080";//


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        myApi = restAdapter.create(MyApi.class);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        clustersVMs=new ArrayList<ClustersVM>();

        id = System.currentTimeMillis() / 1000;  //Sangini Code+Date+Sector No.+Cluster No


       System.out.println("basic:::::::::::::"+"Basic " + Base64.encodeToString(String.format("%s:%s", "kobo", "kobo").getBytes(), Base64.NO_WRAP));





        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object

                gps = new GPSTracker(AlertActivity.this);

                // Setting click event lister for the find button

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    relativeLayout = (RelativeLayout) findViewById(R.id.messageLayout);
                    TextView message = (TextView) findViewById(R.id.message);
                    relativeLayout.setVisibility(View.VISIBLE);
                    successText = (TextView) findViewById(R.id.successText);
                    // \n is for new line
                    Geocoder geocoder = new Geocoder(AlertActivity.this, Locale.getDefault());
                    successText.setVisibility(View.VISIBLE);

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        Address returnedAddress = addresses.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        }
                        addressText = strReturnedAddress.toString();
                        message.setText(strReturnedAddress.toString()); //"Your Location "+address+", "+city+", "+zip+ " has been captured");
                        successText.setText("Your Case is successfully posted for  ");

                        successText.postDelayed(new Runnable() {
                            public void run() {
                                successText.setVisibility(View.INVISIBLE);
                                relativeLayout.setVisibility(View.INVISIBLE);
                            }
                        }, 3000);

                        String token = prefs.getString("token", null);
                        System.out.println("token::" + token);
                        String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", "kobo", token).getBytes(), Base64.NO_WRAP);
                        System.out.println("auth" + basicAuth);


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        final String createdDate = sdf.format(new Date());
                        CaseVM caseVM = new CaseVM(id, createdDate, createdDate, addressText, longitude, latitude);

                        myApi.getCase(basicAuth, caseVM, new Callback<CaseResponseVM>() {
                            @Override
                            public void success(CaseResponseVM caseVM1, Response response) {
                                CaseRecord caseRecord = new CaseRecord(longitude, latitude, addressText, caseVM1.id, "new", createdDate, createdDate);
                                caseRecord.save();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                            }
                        });


                    } catch (IOException e) {
                        message.setText("Your Location latitude: " + latitude + " and longitude: " + longitude + " has been captured");
                        e.printStackTrace();
                    }

                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    buildAlertMessageNoGps();
                }
            }
        });

        myApi.getClusters(6, new Callback<List<SectorVM>>() {
            @Override
            public void success(List<SectorVM> sectorVMList, Response response) {
                ClustersVM clustersVM=new ClustersVM();
                clustersVM.setName("Choose your Location ");
                clustersVMs.add(clustersVM);

                for(SectorVM vm : sectorVMList) {

                    List<ClustersVM> vms = new ArrayList<ClustersVM>();
                    vms.addAll(vm.getClustervm());

                    for(ClustersVM cluster : vms ) {
                        cluster.setSector_name(vm.getName());
                        clustersVMs.add(cluster);
                    }
                }

                final ClusterAdapter clusterAdapter=new ClusterAdapter(getApplicationContext(),clustersVMs);
                clusterSpinner.setAdapter(clusterAdapter);

                clusterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yy");
                        final String createdDate = sdf.format(new Date());

                        ClustersVM clustersVM1=clusterAdapter.getItem(i);
                        caseId="kobo" + "-" + createdDate+ "-" + clustersVM1.getSector_name() + "-" + clustersVM1.getName() ;
                        System.out.println("display case id::::::"+caseId);
                        System.out.println("sector name::::::::"+clustersVM1.getSector_name());

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

}