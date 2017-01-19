package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequest;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;
import com.dbbest.amateurfeed.app.net.response.RegistrationResponse;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;

/**
 * Created by antonina on 19.01.17.
 */

public class RegistrationFacebookCommand extends Command {

    private final static String KEY_IS_COMPLETED_PROFILE = "profile_completed";

    private final RegistrationFaceBookRequest mRegistrationFacebookRequest;

    public RegistrationFacebookCommand(String code, double longitude, double latitude) {
        mRegistrationFacebookRequest = new RegistrationFaceBookRequest(code, longitude, latitude);
    }

    private RegistrationFacebookCommand(Parcel in) {
        super(in);
        mRegistrationFacebookRequest = in.readParcelable(RegistrationRequest.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mRegistrationFacebookRequest, flags);
    }

    @Override
    public void execute() {
        // RestApiClient apiClient = restClient();
        ResponseWrapper<RegistrationResponse> response=null;
//                = apiClient.registrationFacebook(mRegistrationFacebookRequest);
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {

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