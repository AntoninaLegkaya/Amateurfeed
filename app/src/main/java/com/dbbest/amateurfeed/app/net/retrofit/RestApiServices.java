package com.dbbest.amateurfeed.app.net.retrofit;

import com.dbbest.amateurfeed.app.net.request.LoginRequest;
import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequest;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;
import com.dbbest.amateurfeed.app.net.response.RegistrationResponse;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by antonina on 20.01.17.
 */

public interface RestApiServices {

    @POST("/api/account/registration")
    Call<ResponseWrapper<RegistrationResponse>> register(@Body RegistrationRequest request);

    @POST("/api/account/facebook")
    Call<ResponseWrapper<RegistrationResponse>> registerFacebook(@Body RegistrationFaceBookRequest request);


    @POST("/api/account/login")
    Call<ResponseWrapper<RegistrationResponse>> login(@Body LoginRequest request);
}
