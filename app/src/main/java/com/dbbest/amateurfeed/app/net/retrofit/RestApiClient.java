package com.dbbest.amateurfeed.app.net.retrofit;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.request.LoginRequest;
import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequest;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;
import com.dbbest.amateurfeed.app.net.request.ResetRequest;
import com.dbbest.amateurfeed.app.net.response.RegistrationResponse;
import com.dbbest.amateurfeed.app.net.response.ResetResponse;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.utils.ActionUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by antonina on 20.01.17.
 */

public class RestApiClient {

    private static final String USER_BLOCKED_MSG_RESPONSE = "User is blocked";
    private RestApiServices mApiService;

    public RestApiClient(RestApiServices restApiService) {
        mApiService = restApiService;
    }


    private <T> ResponseWrapper<T> executeCall(Call<ResponseWrapper<T>> call) {
        try {
            Response<ResponseWrapper<T>> response = call.execute();
            if (!response.isSuccessful()) {
//                ("Response code: %d, message: %s, successful: %b", response.code(), response.message(), response.isSuccessful());
                if (response.errorBody() != null) {
//                   ("Error body: %s", response.errorBody().string());
                }
            }

            if (response.body() != null && !response.body().isSuccessful()) {
//                ("Response body code: %d, message: %s, success: %b", response.body().code(), response.body().message(), response.body().isSuccessful());
                if (response.body().code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    String action = ActionUtils.ACTION_UNAUTHORIZED;
                    if (response.body().message() != null && response.body().message().equals(USER_BLOCKED_MSG_RESPONSE)) {
                        action = ActionUtils.ACTION_USER_BLOCKED;
                    }
                    Intent intent = new Intent(action);
                    LocalBroadcastManager.getInstance(App.instance())
                            .sendBroadcast(intent);
                }
            }

            return response.body();
        } catch (Exception e) {
//           (e, "network error");
            return NetworkUtil.handleError(e);
        }
    }


    public ResponseWrapper<RegistrationResponse> registration(RegistrationRequest request) {
        return executeCall(mApiService.register(request));
    }

    public ResponseWrapper<RegistrationResponse> registrationFacebook(RegistrationFaceBookRequest request) {
        return executeCall(mApiService.registerFacebook(request));
    }

    public ResponseWrapper<RegistrationResponse> login(LoginRequest request) {
        return executeCall(mApiService.login(request));
    }

    public ResponseWrapper<ResetResponse> forgotPassword(ResetRequest request) {
        return executeCall(mApiService.forgotPassword(request));
    }

}
