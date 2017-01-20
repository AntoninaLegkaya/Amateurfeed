package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;
import com.dbbest.amateurfeed.app.net.response.RegistrationResponse;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

/**
 * Created by antonina on 19.01.17.
 */

public class RegistrationCommand extends Command {

    private final RegistrationRequest mRegistrationRequest;


    public RegistrationCommand(String email, String fullName, String phone, String address, String password, String deviceId, String osType, String deviceToken) {
        mRegistrationRequest = new RegistrationRequest(email,  fullName,  phone,  address,  password,  deviceId,  osType,  deviceToken);
    }

    protected RegistrationCommand(Parcel in) {
        super(in);
        mRegistrationRequest = in.readParcelable(RegistrationRequest.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mRegistrationRequest, flags);
    }
    @Override
    public void execute() {
        //TODO RestApiClient
        RestApiClient apiClient = App.getApiFactory().restClient();
        ResponseWrapper<RegistrationResponse> response = apiClient.registration(mRegistrationRequest);
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

    public static final Parcelable.Creator<RegistrationCommand> CREATOR = new Parcelable.Creator<RegistrationCommand>() {
        @Override
        public RegistrationCommand createFromParcel(Parcel source) {
            return new RegistrationCommand(source);
        }

        @Override
        public RegistrationCommand[] newArray(int size) {
            return new RegistrationCommand[size];
        }
    };
}
