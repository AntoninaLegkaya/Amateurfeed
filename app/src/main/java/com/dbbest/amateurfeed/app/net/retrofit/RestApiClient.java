package com.dbbest.amateurfeed.app.net.retrofit;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.request.LikeModel;
import com.dbbest.amateurfeed.app.net.request.LoginRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.request.ResetRequestPasswordModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsPreviewResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.model.AbuseModel;
import com.dbbest.amateurfeed.model.AzureServiceSettings;
import com.dbbest.amateurfeed.model.FeedCommentModel;
import com.dbbest.amateurfeed.model.NewsCreateModel;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserProfileModel;
import com.dbbest.amateurfeed.utils.ActionUtils;
import com.dbbest.amateurfeed.utils.Utils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Header;

import static android.R.attr.id;

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
            Log.i(Utils.TAG_LOG, "Url Request: " + call.request().url());
            Log.i(Utils.TAG_LOG, "Header Request: " + call.request().headers().toString());
            if (call.request().body() != null) {
                Log.i(Utils.TAG_LOG, "Body Request: " + call.request().body().toString());
            }
            Response<ResponseWrapper<T>> response = call.execute();

            if (!response.isSuccessful()) {
                Log.i(Utils.TAG_LOG, "Response code: " + response.code() + " message: " + response.message() + "successful: " + response.isSuccessful());
                if (response.errorBody() != null) {
                    Log.i(Utils.TAG_LOG, "Error body: " + response.errorBody().string());
                }
            }

            if (response.body() != null && !response.body().isSuccessful()) {
                Log.i(Utils.TAG_LOG, "Response body code: " + response.body().code() + " message: " + response.body().message() + "successful: " + response.body().isSuccessful());
                if (response.body().code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    String action = ActionUtils.ACTION_UNAUTHORIZED;
                    if (response.body().message() != null && response.body().message().equals(USER_BLOCKED_MSG_RESPONSE)) {
                        action = ActionUtils.ACTION_USER_BLOCKED;
                    }
                    Intent intent = new Intent(action);
                    LocalBroadcastManager.getInstance(App.instance())
                            .sendBroadcast(intent);
                }
                if (response.body().code() == HttpURLConnection.HTTP_SERVER_ERROR) {

                    Log.i(Utils.TAG_LOG, "Error code: " + response.body().code() + "message: Server Error");

                }
            }

            return response.body();
        } catch (Exception e) {
            Log.e(Utils.TAG_LOG, "network error: " + e);
            return NetworkUtil.handleError(e);
        }
    }


    public ResponseWrapper<LoginResponseModel> registration(RegistrationRequestModel request) {
        return executeCall(mApiService.register(request));
    }

    public ResponseWrapper<LoginResponseModel> registrationFacebook(RegistrationFaceBookRequestModel request) {
        return executeCall(mApiService.registerFacebook(request));
    }

    public ResponseWrapper<LoginResponseModel> login(LoginRequestModel request) {
        return executeCall(mApiService.login(request));
    }

    public ResponseWrapper<Object> forgotPassword(ResetRequestPasswordModel request) {
        return executeCall(mApiService.forgotPassword(request));
    }

    public ResponseWrapper<ArrayList<NewsPreviewResponseModel>> getNews(String token, int offset, int count) {
        return executeCall(mApiService.getSpecifiedNews(token, offset, count));
    }

    public ResponseWrapper<Object> isLike(String token, long id, LikeModel model) {
        return executeCall(mApiService.isLikeNews(token, model, id));
    }

    public ResponseWrapper<Object> postAbuse(String token, AbuseModel model) {

        return executeCall(mApiService.postAbuse(token, model));
    }

    public ResponseWrapper<ArrayList<TagModel>> checkTagName(String token, String tagName) {
        return executeCall(mApiService.checkTags(token, tagName));
    }

    public ResponseWrapper<NewsResponseModel> editNews(String token, NewsUpdateModel updateModel, int id) {
        return executeCall(mApiService.editSpecifiedNews(token, updateModel, id));
    }

    public ResponseWrapper<AzureServiceSettings> getAzureInfo(String token) {
        return executeCall(mApiService.getAzureInfo(token));
    }

    public ResponseWrapper<Object> postComment(String token, FeedCommentModel commentModel) {
        return executeCall(mApiService.postComment(token, commentModel));
    }

    public ResponseWrapper<NewsResponseModel> addNewNews(String token, NewsCreateModel createModel) {
        return executeCall(mApiService.addNewNews(token, createModel));
    }

    public ResponseWrapper<UserProfileModel> getUserInfo(String token) {
        return executeCall(mApiService.getSpecifiedUserInfo(token));
    }
}
