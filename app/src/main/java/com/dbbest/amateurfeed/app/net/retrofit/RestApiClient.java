package com.dbbest.amateurfeed.app.net.retrofit;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.request.ChangePasswordRequestModel;
import com.dbbest.amateurfeed.app.net.request.LikeModel;
import com.dbbest.amateurfeed.app.net.request.LoginRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.request.ResetRequestPasswordModel;
import com.dbbest.amateurfeed.app.net.request.UpdateProfileRequestModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsPreviewResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.model.AbuseModel;
import com.dbbest.amateurfeed.model.AzureServiceSettings;
import com.dbbest.amateurfeed.model.DeviceInfoModel;
import com.dbbest.amateurfeed.model.Dictionary;
import com.dbbest.amateurfeed.model.FeedCommentModel;
import com.dbbest.amateurfeed.model.NewsCreateModel;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserNewsModel;
import com.dbbest.amateurfeed.model.UserProfileModel;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Response;


public class RestApiClient {

  public static final String ACTION_UNAUTHORIZED = "ACTION_UNAUTHORIZED";
  public static final String ACTION_USER_BLOCKED = "ACTION_USER_BLOCKED";
  private static final String USER_BLOCKED_MSG_RESPONSE = "User is blocked";
  private RestApiServices mApiService;

  public RestApiClient(RestApiServices restApiService) {
    mApiService = restApiService;
  }

  public ResponseWrapper<LoginResponseModel> registration(RegistrationRequestModel request) {
    return executeCall(mApiService.register(request));
  }

  public ResponseWrapper<LoginResponseModel> registrationFacebook(
      RegistrationFaceBookRequestModel request) {
    return executeCall(mApiService.registerFacebook(request));
  }

  public ResponseWrapper<LoginResponseModel> login(LoginRequestModel request) {
    return executeCall(mApiService.login(request));
  }

  public ResponseWrapper<Object> forgotPassword(ResetRequestPasswordModel request) {
    return executeCall(mApiService.forgotPassword(request));
  }

  public ResponseWrapper<ArrayList<NewsPreviewResponseModel>> getNews(String token, int offset,
      int count) {
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

  public ResponseWrapper<NewsResponseModel> editNews(String token, NewsUpdateModel updateModel,
      int id) {
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

  public ResponseWrapper<Dictionary> searchNews(String searchParam) {
    return executeCall(mApiService.getMapUsersNews(searchParam));
  }

  public ResponseWrapper<ArrayList<UserNewsModel>> getMyNews(String token) {
    return executeCall(mApiService.getSpecifiedNewsByUser(token));
  }

  public ResponseWrapper<Object> updateUserProfile(String token,
      UpdateProfileRequestModel updateProfileRequestModel) {
    return executeCall(mApiService.putSpecifiedUserInfo(token, updateProfileRequestModel));
  }

  public ResponseWrapper<Object> logout(String token) {
    return executeCall(mApiService.logout(token));
  }

  public ResponseWrapper<Object> changePassword(String token,
      ChangePasswordRequestModel requestModel) {
    return executeCall(mApiService.changePassword(token, requestModel));
  }

  public ResponseWrapper<Object> updateDeviceInfo(String token, DeviceInfoModel requestModel) {
    return executeCall(mApiService.deviceInfo(token, requestModel));
  }

  private <T> ResponseWrapper<T> executeCall(Call<ResponseWrapper<T>> call) {
    try {
      Response<ResponseWrapper<T>> response = call.execute();
      if (!response.isSuccessful()) {
        if (response.errorBody() != null) {
        }
      }
      if (response.body() != null && !response.body().isSuccessful()) {
        if (response.body().code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
          String action = ACTION_UNAUTHORIZED;
          if (response.body().message() != null && response.body().message()
              .equals(USER_BLOCKED_MSG_RESPONSE)) {
            action = ACTION_USER_BLOCKED;
          }
          Intent intent = new Intent(action);
          LocalBroadcastManager.getInstance(App.instance())
              .sendBroadcast(intent);
        }

      }

      return response.body();
    } catch (IOException e) {
      return NetworkUtil.handleError(e);
    }
  }
}
