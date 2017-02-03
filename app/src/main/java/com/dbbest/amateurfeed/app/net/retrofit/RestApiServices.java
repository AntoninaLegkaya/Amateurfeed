package com.dbbest.amateurfeed.app.net.retrofit;

import android.content.Intent;

import com.dbbest.amateurfeed.app.net.request.ChangePasswordRequestModel;
import com.dbbest.amateurfeed.app.net.request.LoginRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.request.ResetRequestPasswordModel;
import com.dbbest.amateurfeed.app.net.request.UpdateProfileRequestModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsPreviewResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResetResponse;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.model.AbuseModel;
import com.dbbest.amateurfeed.model.DeviceInfoModel;
import com.dbbest.amateurfeed.model.FeedCommentModel;
import com.dbbest.amateurfeed.model.NewsCreateModel;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UnreadMessageCounterModel;
import com.dbbest.amateurfeed.model.UserNewsModel;
import com.dbbest.amateurfeed.model.UserProfileModel;
import com.dbbest.amateurfeed.model.UserSettingsModel;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by antonina on 20.01.17.
 */

public interface RestApiServices {


    /* <---- Account----> */

    @POST("/api/account/registration")
    Call<ResponseWrapper<LoginResponseModel>> register(@Body RegistrationRequestModel request);

    @POST("/api/account/facebook")
    Call<ResponseWrapper<LoginResponseModel>> registerFacebook(@Body RegistrationFaceBookRequestModel request);

    @POST("/api/account/login")
    Call<ResponseWrapper<LoginResponseModel>> login(@Body LoginRequestModel request);

    @POST("/api/account/forgot")
    Call<ResponseWrapper<Object>> forgotPassword(@Body ResetRequestPasswordModel request);

    @POST("/api/account/changePassword")
    Call<ResponseWrapper<Object>> changePassword(@Header("Authorization") String token, @Body ChangePasswordRequestModel request);

    @POST("/api/account/device-info")
    Call<Object> deviceInfo(@Header("Authorization") String token, @Body DeviceInfoModel infoModel);

    @GET("/api/account/logout")
    Call<ResponseWrapper<Object>> logout(@Header("Authorization") String token);

    @PUT("/api/account/unread-messages/count")
    Call<Object> putUnreadMessagesCount(@Body UnreadMessageCounterModel counterModel);


    /*    <---- News----> */

    @GET("/api/news")
    Call<ResponseWrapper<ArrayList<NewsPreviewResponseModel>>> getSpecifiedNews(@Query("offset") int offset, @Query("count") int count);

    @POST("/api/news")
    Call<ResponseWrapper<NewsResponseModel>> addNewNews(@Body NewsCreateModel createModel);

    @POST("/api/news/abuse")
    Call<ResponseWrapper<Object>> postAbuse(@Body AbuseModel abuseModel);

    @PUT("/api/news/{id}")
    Call<ResponseWrapper<NewsResponseModel>> editeSpecifiedNews(@Body NewsUpdateModel updateModel, @Path("id") int id);

    @GET("/api/news/my")
    Call<ResponseWrapper<ArrayList<UserNewsModel>>> getSpecifiedNewsByUser();

    @PUT("/api/news/{id}/like")
    Call<ResponseWrapper<Object>> likeNews(@Path("id") int id);

    @POST("/api/news/add-comment")
    Call<ResponseWrapper<Object>> postComment(@Body FeedCommentModel commentModel);

    @GET("/api/search")
    Call<ResponseWrapper<Dictionary<String, List>>> getMapUsersNews(@Header("Authorization") String token, @Query("searchParam") String searchParam);


    /*    <---- Tag----> */

    @GET("/api/tags?")
    Call<ResponseWrapper<ArrayList<TagModel>>> getTags(@Header("Authorization") String token,@Query("keyword") String keyword);


    /*    <---- Upload----> */

    @POST("/api/upload")
    Call<ResponseWrapper<ArrayList<String>>> upload(@Body Object o);


    /*    <---- User----> */

    @GET("/api/user")
    Call<ResponseWrapper<UserProfileModel>> getSpecifiedUserInfo(@Header("Authorization") String token);

    @PUT("/api/user")
    Call<ResponseWrapper<Object>> putSpecifiedUserInfo(@Header("Authorization") String token, @Body UpdateProfileRequestModel updateProfileRequestModel);

    @PUT("/api/user/update-settings")
    Call<Object> updateSettings(@Header("Authorization") String token, @Body UserSettingsModel isSettingsUpdateModel);
}
