package com.dbbest.amateurfeed.app.net.retrofit;

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
import com.dbbest.amateurfeed.model.UnreadMessageCounterModel;
import com.dbbest.amateurfeed.model.UserNewsModel;
import com.dbbest.amateurfeed.model.UserProfileModel;
import com.dbbest.amateurfeed.model.UserSettingsModel;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiServices {


    /* <---- Account----> */

  @POST("account/registration")
  Call<ResponseWrapper<LoginResponseModel>> register(@Body RegistrationRequestModel request);

  @POST("account/facebook")
  Call<ResponseWrapper<LoginResponseModel>> registerFacebook(
      @Body RegistrationFaceBookRequestModel request);

  @POST("account/login")
  Call<ResponseWrapper<LoginResponseModel>> login(@Body LoginRequestModel request);

  @POST("account/forgot")
  Call<ResponseWrapper<Object>> forgotPassword(@Body ResetRequestPasswordModel request);

  @POST("account/changePassword")
  Call<ResponseWrapper<Object>> changePassword(@Header("Authorization") String token,
      @Body ChangePasswordRequestModel request);

  @POST("account/device-info")
  Call<Object> deviceInfo(@Header("Authorization") String token, @Body DeviceInfoModel infoModel);

  @GET("account/logout")
  Call<ResponseWrapper<Object>> logout(@Header("Authorization") String token);

  @PUT("account/unread-messages/count")
  Call<Object> putUnreadMessagesCount(@Body UnreadMessageCounterModel counterModel);


    /*    <---- News----> */

  @GET("news")
  Call<ResponseWrapper<ArrayList<NewsPreviewResponseModel>>> getSpecifiedNews(
      @Header("Authorization") String token, @Query("offset") int offset,
      @Query("count") int count);

  @POST("news")
  Call<ResponseWrapper<NewsResponseModel>> addNewNews(@Header("Authorization") String token,
      @Body NewsCreateModel createModel);

  @POST("news/abuse")
  Call<ResponseWrapper<Object>> postAbuse(@Header("Authorization") String token,
      @Body AbuseModel abuseModel);

  @PUT("news/{id}")
  Call<ResponseWrapper<NewsResponseModel>> editSpecifiedNews(@Header("Authorization") String token,
      @Body NewsUpdateModel updateModel, @Path("id") int id);

  @GET("news/my")
  Call<ResponseWrapper<ArrayList<UserNewsModel>>> getSpecifiedNewsByUser(@Header("Authorization") String token);

  @PUT("news/{id}/like")
  Call<ResponseWrapper<Object>> isLikeNews(@Header("Authorization") String token,
      @Body LikeModel model, @Path("id") long id);

  @POST("news/add-comment")
  Call<ResponseWrapper<Object>> postComment(@Header("Authorization") String token,
      @Body FeedCommentModel commentModel);

  @GET("search")
  Call<ResponseWrapper<Dictionary>> getMapUsersNews(@Query("searchParam") String searchParam);


    /*    <---- Tag----> */

  @GET("tags")
  Call<ResponseWrapper<ArrayList<TagModel>>> checkTags(@Header("Authorization") String token,
      @Query("keyword") String keyword);


    /*    <---- Upload----> */

  @POST("upload")
  Call<ResponseWrapper<ArrayList<String>>> upload(@Body Object o);


    /*    <---- User----> */

  @GET("user")
  Call<ResponseWrapper<UserProfileModel>> getSpecifiedUserInfo(
      @Header("Authorization") String token);

  @PUT("user")
  Call<ResponseWrapper<Object>> putSpecifiedUserInfo(@Header("Authorization") String token,
      @Body UpdateProfileRequestModel updateProfileRequestModel);

  @PUT("user/update-settings")
  Call<Object> updateSettings(@Header("Authorization") String token,
      @Body UserSettingsModel isSettingsUpdateModel);


  /*    <---- Settings----> */
  @GET("settings/azure")
  Call<ResponseWrapper<AzureServiceSettings>> getAzureInfo(@Header("Authorization") String token);


}
