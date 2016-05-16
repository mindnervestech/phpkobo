package org.koboc.collect.android.application;

import org.koboc.collect.android.model.CaseResponseVM;
import org.koboc.collect.android.model.CaseVM;
import org.koboc.collect.android.model.SectorVM;
import org.koboc.collect.android.model.UserVM;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by User on 01-06-2015.
 */
public interface MyApi {
    @POST("/api/v1/login")
    public void Login(@Header("Authorization") String authorization, Callback<UserVM> callback);

   // @POST("/api/v1/submit")
   // public void submit(@Query("Long")String longtitude,@Query("Latt")String latitude,@Query("Address")String address,@Query("Id")String userId, Callback<UserVM> callback);

    @POST("/webapp/case")
    public void postCase(@Header("Authorization") String authorization,@Body CaseVM caseVM, Callback<CaseResponseVM> callback);

    @GET("/webapp/case")
    public void getCase(@Header("Authorization") String authorization,Callback<List<CaseResponseVM>> callback);

    @POST("/webapp/getClusterOfSectors/{SID}")
    public void getClusters(@Path("SID")long id,Callback<List<SectorVM>> callback);

	@GET("/webapp/isCaseDeleted")
	public void isCaseDeleted(@Header("Authorization") String authorization,@Query("id")Long id,Callback<Boolean> callback);



}
