package org.koboc.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.koboc.collect.android.R;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.model.UserVM;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class LoginActivity extends Activity {
    private EditText userEditText,passwordEditText;
    private Button submit;
    private MyApi myApi;
    private String BASE_URL;
    private SharedPreferences preferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userEditText= (EditText) findViewById(R.id.userEditText);
        passwordEditText= (EditText) findViewById(R.id.passwordEditText);
        submit= (Button) findViewById(R.id.submitButton);

        BASE_URL = getApplicationContext().getString(R.string.default_server_url);



        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient()).build();
        myApi = restAdapter.create(MyApi.class);

         preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(preferences.getString("token",null)!=null){
            Intent intent=new Intent(LoginActivity.this,SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", userEditText.getText().toString(), passwordEditText.getText().toString()).getBytes(), Base64.NO_WRAP);
                System.out.println("auth:::"+basicAuth);
                myApi.Login(basicAuth,new Callback<UserVM>() {
                    @Override
                    public void success(UserVM userVM, Response response) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token",userVM.getApi_token());
                        editor.commit();
                        System.out.println("url:::"+response.getUrl());
                        Intent intent=new Intent(LoginActivity.this,SplashScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("url:::"+error.getResponse().getUrl());
                            //error.printStackTrace();
                    }
                });



            }
        });

    }
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit app ?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            LoginActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    }
