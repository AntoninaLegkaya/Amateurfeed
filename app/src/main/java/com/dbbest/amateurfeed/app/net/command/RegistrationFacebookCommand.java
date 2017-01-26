package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

/**
 * Created by antonina on 19.01.17.
 */

public class RegistrationFacebookCommand extends Command {

    private final static String KEY_IS_COMPLETED_PROFILE = "profile_completed";

    private final RegistrationFaceBookRequestModel mRegistrationFacebookRequest;

    public RegistrationFacebookCommand(String code, double longitude, double latitude) {
        mRegistrationFacebookRequest = new RegistrationFaceBookRequestModel(code, longitude, latitude);
    }

    private RegistrationFacebookCommand(Parcel in) {
        super(in);
        mRegistrationFacebookRequest = in.readParcelable(RegistrationRequestModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mRegistrationFacebookRequest, flags);
    }

    @Override
    public void execute() {
        RestApiClient apiClient = App.getApiFactory().restClient();
        ResponseWrapper<LoginResponseModel> response = apiClient.registrationFacebook(mRegistrationFacebookRequest);
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {
                LoginResponseModel data = response.data();
                AuthToken authToken = new AuthToken();
                authToken.update(data.getAccessToken());


            } else {
                Bundle bundle = getStatusBundle(response);
                notifyError(bundle);
            }
        } else {
            //TODO Exception
        }

    }

    public static boolean grabIsProfileCompleted(@NonNull Bundle bundle) {
        return bundle.getBoolean(KEY_IS_COMPLETED_PROFILE, false);
    }

    public static final Creator<RegistrationFacebookCommand> CREATOR = new Creator<RegistrationFacebookCommand>() {
        @Override
        public RegistrationFacebookCommand createFromParcel(Parcel source) {
            return new RegistrationFacebookCommand(source);
        }

        @Override
        public RegistrationFacebookCommand[] newArray(int size) {
            return new RegistrationFacebookCommand[size];
        }
    };
}