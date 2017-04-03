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
  private Retrofit retrofit;
  private RestApiClient restApiClient;
  private Gson gson;


  public Retrofit retrofit() {
    if (retrofit == null) {

      HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
      httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

      retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.API_SERVER_URL)
          .client(new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
              .addInterceptor(new StethoInterceptor()).build())
          .addConverterFactory(GsonConverterFactory.create(gson()))
          .build();
    }
    return retrofit;
  }

  public RestApiClient restClient() {
    if (restApiClient == null) {
      RestApiServices service = retrofit().create(RestApiServices.class);
      restApiClient = new RestApiClient(service);
    }
    return restApiClient;
  }

  private Gson gson() {
    if (gson == null) {
      gson = new GsonBuilder()
          .setDateFormat(DATE_FORMAT)
          .create();
    }

    return gson;
  }
}
