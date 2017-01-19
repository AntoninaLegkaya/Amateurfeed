package com.dbbest.amateurfeed.app.net.command;

import android.os.Parcel;
import android.os.Parcelable;

import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;

/**
 * Created by antonina on 19.01.17.
 */

public class RegistrationCommand extends Command {

    private final RegistrationRequest mRegistrationRequest;

    public RegistrationCommand(String email, String password, double longitude, double latitude) {
        mRegistrationRequest = new RegistrationRequest(email, password, longitude, latitude);
    }

    public RegistrationCommand(String email, String firstName, String lastName, String password) {
        mRegistrationRequest = new RegistrationRequest(email, firstName, lastName, password);
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
//        RestApiClient apiClient = App.graph().restClient();
//        ResponseWrapper<RegistrationResponse> response = apiClient.registration(mRegistrationRequest);
//        if (response != null) {
//            if (response.isSuccessful() && response.data() != null) {
//
//            } else {
//            }
//        } else {
//            //TODO Registration response is null!");
//        }

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
