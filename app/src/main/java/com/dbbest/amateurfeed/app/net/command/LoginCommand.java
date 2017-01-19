package com.dbbest.amateurfeed.app.net.command;


import android.os.Parcel;
import android.os.Parcelable;

import com.dbbest.amateurfeed.app.net.request.LoginRequest;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;

/**
 * Created by antonina on 19.01.17.
 */

public class LoginCommand extends Command {


    private final LoginRequest mLoginRequest;

    public LoginCommand(String email, String password, double longitude, double latitude) {
        mLoginRequest = new LoginRequest(email, password, longitude, latitude);
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
//       ResponseWrapper<RegistrationResponse> response=apiClient.login(mLoginRequest)

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
