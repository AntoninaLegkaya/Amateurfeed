package com.dbbest.amateurfeed.app.net.retrofit;

import com.dbbest.amateurfeed.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by antonina on 20.01.17.
 */

public class ApiFactory {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static Retrofit mRetrofit;
    private RestApiClient mRestApiClient;
    private Gson mGson;


    public  Retrofit retrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_SERVER_URL)
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
