package org.koboc.collect.android.application;

import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.model.CaseVM;
import org.koboc.collect.android.model.UserVM;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by User on 01-06-2015.
 */
public interface MyApi {
    @POST("/api/v1/login")
    public void Login(@Header("Authorization") String authorization, Callback<UserVM> callback);

   // @POST("/api/v1/submit")
   // public void submit(@Query("Long")String longtitude,@Query("Latt")String latitude,@Query("Address")String address,@Query("Id")String userId, Callback<UserVM> callback);

    @POST("/webapp/case")
    public void getCase(@Header("Authorization") String authorization,@Body CaseVM caseVM, Callback<CaseResponseVM> callback);




}
