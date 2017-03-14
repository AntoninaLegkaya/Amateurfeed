package com.dbbest.amateurfeed.app.net.retrofit;

import com.dbbest.amateurfeed.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiFactory {

  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private Retrofit mRetrofit;
  private RestApiClient mRestApiClient;
  private Gson mGson;


  public Retrofit retrofit() {
    if (mRetrofit == null) {

      HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
      httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

      mRetrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.API_SERVER_URL)
          .client(new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
              .addInterceptor(new StethoInterceptor()).build())
          .addConverterFactory(GsonConverterFactory.create(gson()))
          .build();
    }
    return mRetrofit;
  }

  private Gson gson() {
    if (mGson == null) {
      mGson = new GsonBuilder()
          .setDateFormat(DATE_FORMAT)
          .create();
    }

    return mGson;
  }


  public RestApiClient restClient() {
    if (mRestApiClient == null) {
      RestApiServices service = retrofit().create(RestApiServices.class);
      mRestApiClient = new RestApiClient(service);
    }
    return mRestApiClient;
  }
}
