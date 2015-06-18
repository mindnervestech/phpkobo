package org.koboc.collect.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseVM;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class AlertActivity extends Activity {
	
	Button btnShowLocation;
	
	// GPSTracker class
	GPSTracker gps;
    private MyApi myApi;
    private String BASE_URL,addressText;
    private double latitude;
    private double longitude;
    private TextView successText;
    RelativeLayout relativeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnShowLocation.setBackgroundColor(Color.RED);

        BASE_URL = "http://192.168.2.60:8080";//getApplicationContext().getString(R.string.base_url);

       /* RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient()).build();
        myApi = restAdapter.create(MyApi.class);*/

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        myApi = restAdapter.create(MyApi.class);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {		
				// create class object

                gps = new GPSTracker(AlertActivity.this);

                // Setting click event lister for the find button

				// check if GPS enabled		
		        if(gps.canGetLocation()){
		        	
		        	 latitude = gps.getLatitude();
		        	 longitude = gps.getLongitude();
                   // btnShowLocation.setBackgroundColor(Color.GREEN);
                     relativeLayout = (RelativeLayout)findViewById(R.id.messageLayout);
                    TextView message = (TextView)findViewById(R.id.message);
                    relativeLayout.setVisibility(View.VISIBLE);
		        	 successText= (TextView) findViewById(R.id.successText);
		        	// \n is for new line
                    Geocoder geocoder = new Geocoder(AlertActivity.this, Locale.getDefault());

                    successText.setVisibility(View.VISIBLE);

                    List<Address> addresses  = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude,longitude,1);
                        System.out.println("size:::"+addresses.size());
                        Address returnedAddress = addresses.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                        for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        }
                        addressText=strReturnedAddress.toString();
                       System.out.println("address to::::"+strReturnedAddress.toString());
                        System.out.println("long:::"+longitude+"latti"+latitude);
                        /*String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String zip = addresses.get(0).getPostalCode();*/
                        message.setText(strReturnedAddress.toString()); //"Your Location "+address+", "+city+", "+zip+ " has been captured");
                    } catch (IOException e) {
                        message.setText( "Your Location latitude: "+latitude+" and longitude: "+longitude+" has been captured");
                        e.printStackTrace();
                    }
		        }else{
		        	// can't get location
		        	// GPS or Network is not enabled
		        	// Ask user to enable GPS/network in settings
		        	gps.showSettingsAlert();
		        }

                /*CaseRecord caseRecord=new CaseRecord(longitude+"",latitude+"",addressText,"100","new");
                caseRecord.save();

                CaseRecord caseRecord1=new CaseRecord(longitude+"",latitude+"",addressText,"101","draft");
                caseRecord1.save();

                CaseRecord caseRecord2=new CaseRecord(longitude+"",latitude+"",addressText,"102","new");
                caseRecord2.save();

                CaseRecord caseRecord3=new CaseRecord(longitude+"",latitude+"",addressText,"103","draft");
                caseRecord3.save();*/

                successText.setText("Your Case is successfully posted for  ");

                successText.postDelayed(new Runnable() {
                    public void run() {
                        successText.setVisibility(View.INVISIBLE);
                        relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }, 3000);

                long cnt=CaseRecord.count(CaseRecord.class,null,null);
                System.out.println("alert count:::"+cnt);

/*
            myApi.submit(longitude+"",latitude+"",addressText,"100",new Callback<UserVM>() {
                @Override
                public void success(UserVM userVM, Response response) {

                    CaseRecord caseRecord=new CaseRecord(longitude+"",latitude+"",addressText,"100","new");
                    caseRecord.save();
                    
                    
                    long cnt=CaseRecord.count(CaseRecord.class,null,null);
                    System.out.println("count:::"+cnt);

                    List<CaseRecord> caseRecords=caseRecord.findWithQuery(CaseRecord.class,"SELECT * FROM Case_Record");

                    for (CaseRecord item:caseRecords){
                        System.out.println("item::::"+item.caseId);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });*/


                String token = prefs.getString("token",null);
                System.out.println("token::" +token);
                String basicAuth = "Basic "+Base64.encodeToString(String.format("%s:%s", "kobo", token).getBytes(), Base64.NO_WRAP);
                System.out.println("auth"+basicAuth);

              /*  Date date=new Date();
                Dte date1=new Date();
                SimpleDateFormat simpleFormat = (SimpleDateFormat) DateFormat.getDateInstance();
                simpleFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    date1 = simpleFormat.parse("2013-07-29T18:00:00-04:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                System.out.println("date::" + date1);*/

                CaseVM caseVM=new CaseVM("2015-06-06T08:07:07","2015-06-06T08:07:07","this is note",longitude,latitude);


                //myApi.getCase(basicAuth,caseVM,new Callback<CaseResponseVM>(){
                  //  @Override
                    //public void success(CaseResponseVM caseVM1, Response response) {
                        System.out.println("API called:::");
    //                    System.out.println("id:::"+caseVM1.id);
                        CaseRecord caseRecord=new CaseRecord(longitude,latitude,addressText,777/*caseVM1.id*/,"new",0);
                        caseRecord.save();
                    //}

//                    @Override
  //                  public void failure(RetrofitError error) {
  //                          error.printStackTrace();
    //                }
//                });

			}
		});
    }

    private void turnGPSOnOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
            //Toast.makeText(this, "Your GPS is Enabled",Toast.LENGTH_SHORT).show();
        }
    }

}