package com.bloodlink.app.api;

import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    
    @POST("auth/login")
    Call<ResponseBody> loginUser(@Body Map<String, String> loginData);

    @POST("auth/register")
    Call<ResponseBody> registerUser(@Body Map<String, Object> userData);

    @GET("requests")
    Call<ResponseBody> getBloodRequests();
    
    @GET("donors/nearby")
    Call<ResponseBody> getNearbyDonors();
}
