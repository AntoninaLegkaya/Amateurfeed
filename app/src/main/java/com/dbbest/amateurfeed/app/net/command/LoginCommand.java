package com.dbbest.amateurfeed.app.net.command;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.LoginRequest;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;
import com.dbbest.amateurfeed.app.net.response.RegistrationResponse;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

/**
 * Created by antonina on 19.01.17.
 */

public class LoginCommand extends Command {


    private final LoginRequest mLoginRequest;

    public LoginCommand(String email, String password, String deviceId, String osType, String deviceToken) {
        mLoginRequest = new LoginRequest(email, password, deviceId, osType, deviceToken);
    }

    private LoginCommand(Parcel in) {
        super(in);
        mLoginRequest = in.readParcelable(RegistrationRequest.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mLoginRequest, flags);
    }

    @Override
    public void execute() {
        //TODO RestApiClient
        RestApiClient apiClient = App.getFactory().restClient();
        ResponseWrapper<RegistrationResponse> response = apiClient.login(mLoginRequest);
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {

                RegistrationResponse data = response.data();
                AuthToken authToken = new AuthToken();
                authToken.update(data.getAccessToken());


            }
            notifySuccess(Bundle.EMPTY);

        } else {
//            "Login response is null!"
            notifyError(Bundle.EMPTY);
        }
    }

    public static final Parcelable.Creator<LoginCommand> CREATOR = new Parcelable.Creator<LoginCommand>() {
        @Override
        public LoginCommand createFromParcel(Parcel source) {
            return new LoginCommand(source);
        }

        @Override
        public LoginCommand[] newArray(int size) {
            return new LoginCommand[size];
        }
    };
}
