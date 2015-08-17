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

import org.apache.http.auth.AUTH;
import org.koboc.collect.android.R;
import org.koboc.collect.android.application.MyApi;
import org.koboc.collect.android.database.AuthUser;
import org.koboc.collect.android.database.CaseRecord;
import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.model.UserVM;
import org.koboc.collect.android.preferences.PreferencesActivity;

import java.util.List;

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

        BASE_URL = getApplicationContext().getString(R.string.default_phython_server_url);



        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient()).build();
        myApi = restAdapter.create(MyApi.class);
        
        //AuthUser.deleteAll(AuthUser.class);
        AuthUser authUser = AuthUser.findLoggedInUser();
        if(authUser != null) {
            Intent intent=new Intent(LoginActivity.this,SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }


        //TODO: Logout need to be implemented
        //TODO: What will happen if you internet connection
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", userEditText.getText().toString(), passwordEditText.getText().toString()).getBytes(), Base64.NO_WRAP);
                System.out.println("auth:::"+basicAuth);
                myApi.Login(basicAuth,new Callback<UserVM>() {
                    @Override
                    public void success(UserVM userVM, Response response) {
                        AuthUser user = new AuthUser();
                        user.setApi_token(userVM.getApi_token());
                        user.setRole(userVM.getGroups().get(0));
                        user.setUserId(userVM.getId());
                        user.setLogged("true");
                        user.setUsername(userEditText.getText().toString());
                        user.setPassword(passwordEditText.getText().toString());
                        //TODO: Password kobo , need to be from string.xml
                        String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", user.getRole() + "_form", "kobo").getBytes(), Base64.NO_WRAP);

                        user.setFormListUser(basicAuth);
                        AuthUser authUser = AuthUser.findLoggedInUser();
                        if(authUser != null){
                            if(!authUser.getRole().equalsIgnoreCase(user.getRole()) ||
                                    !authUser.getUsername().equalsIgnoreCase(user.getUsername())) {
                                // if prev logged in users role or username has changed the delete all instance of
                                // form and case from DB on current device.
                                //TODO : implement above , refer to delete form section in OOB
                                CaseRecord.deleteAll(CaseRecord.class);
                                /* Dont delete this , it may be used in future
                                String basicAuthWithToken = "Basic " + Base64.encodeToString(String.format("%s:%s", user.getUsername(), user.getApi_token()).getBytes(), Base64.NO_WRAP);
                                myApi.getCase(basicAuthWithToken,new Callback<List<CaseResponseVM>>() {
                                    @Override
                                    public void success(List<CaseResponseVM> caseResponseVMs, Response response) {
                                    }
                                    @Override
                                    public void failure(RetrofitError error) {
                                    }
                                });*/
                            }
                        }
                        AuthUser.deleteAll(AuthUser.class);

                         preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(PreferencesActivity.KEY_FORMLIST_URL, "/" + user.getRole() + "_form/formList");
                        editor.commit();

                        user.save();
                        Intent intent=new Intent(getApplicationContext(),SplashScreenActivity.class);
                        startActivity(intent);
                        System.out.println("url:::" + response.getUrl());

                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //TODO : Handle null pointer here
                        System.out.println("url:::"+error.getResponse().getUrl());
                            error.printStackTrace();
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
