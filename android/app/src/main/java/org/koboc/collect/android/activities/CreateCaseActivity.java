package org.koboc.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.koboc.collect.android.R;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.model.CaseVM;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class CreateCaseActivity extends Activity {
     private EditText addressText;
    private TextView message,successText;
    private RelativeLayout relativeLayout;
    private Button createCaseButton;
    private Long id;
    private MyApi myApi;
    private String BASE_URL,addressString,caseId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_case);

        addressText = (EditText) findViewById(R.id.addressEditText);
        createCaseButton = (Button) findViewById(R.id.btnShowLocation);
        relativeLayout = (RelativeLayout) findViewById(R.id.messageLayout);
        message = (TextView) findViewById(R.id.message);
        successText = (TextView) findViewById(R.id.successText);

        BASE_URL = getApplicationContext().getString(R.string.default_java_server_url); //"http://192.168.2.60:8080";//

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        myApi = restAdapter.create(MyApi.class);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yy");
        final String createdDate = sdf.format(new Date());
        id = System.currentTimeMillis() / 1000;
        caseId=AuthUser.findLoggedInUser().getUsername() + "-" + createdDate + "-" + addressText.getText().toString();

        createCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCase();
            }
        });

    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getApplicationContext().getString(R.string.exitappmsg))
                    .setCancelable(false)
                    .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CreateCaseActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(getApplicationContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

    }

    private void showCustomToast(String message, int duration) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.toast_view, null);

        // set the text in the view
        TextView tv = (TextView) view.findViewById(R.id.message);
        tv.setText(message);

        Toast t = new Toast(this);
        t.setView(view);
        t.setDuration(duration);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
    }

    private void registerCase(){

        addressString = addressText.getText().toString();


        AuthUser authUser = AuthUser.findLoggedInUser();
        String token = authUser.getApi_token();
        String username = authUser.getUsername();
        String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", username, token).getBytes(), Base64.NO_WRAP);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final String createdDate = sdf.format(new Date());
        CaseVM caseVM = new CaseVM(id,caseId, createdDate, createdDate, addressString,"External", 0, 0);

        //System.out.println("sent data ::: "+createdDate);

        myApi.postCase(basicAuth, caseVM, new Callback<CaseResponseVM>() {
            @Override
            public void success(CaseResponseVM caseVM1, Response response) {

                successText.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                message.setText(addressString); //"Your Location "+address+", "+city+", "+zip+ " has been captured");
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
                    CaseRecord caseRecord = new CaseRecord(caseId,0, 0, addressString, caseVM1.id, "external", createdDate, createdDate,false);
					//Uncomment foe new versionm
					caseRecord.uid = AuthUser.findLoggedInUser().getUserId();
					caseRecord.save();
                    showCustomToast(getApplicationContext().getString(R.string.successcase), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showCustomToast(getApplicationContext().getString(R.string.failurecase), Toast.LENGTH_LONG);
                //error.printStackTrace();
            }
        });


    }

}