package com.dbbest.amateurfeed.app.net.command;

import android.os.Parcel;
import android.os.Parcelable;

import com.dbbest.amateurfeed.app.net.request.RegistrationRequest;
import com.dbbest.amateurfeed.app.net.request.ResetRequest;

/**
 * Created by antonina on 19.01.17.
 */

public class ResetPasswordCommand extends Command {
    private final ResetRequest mResetRequest;

    public ResetPasswordCommand(String email) {
        mResetRequest = new ResetRequest(email);
    }

    protected ResetPasswordCommand(Parcel in) {

        super(in);
        mResetRequest = in.readParcelable(ResetRequest.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mResetRequest, flags);
    }

    @Override
    public void execute() {

        //        RestApiClient apiClient = App.graph().restClient();
//        ResponseWrapper<ResetResponse> response = apiClient.registration(mResetRequest);
//        if (response != null) {
//            if (response.isSuccessful() && response.data() != null) {
//
//            } else {
//            }
//        } else {
//            //TODO Reset response is null!");
//        }

    }

    public static final Parcelable.Creator<ResetPasswordCommand> CREATOR = new Parcelable.Creator<ResetPasswordCommand>() {
        @Override
        public ResetPasswordCommand createFromParcel(Parcel source) {
            return new ResetPasswordCommand(source);
        }

        @Override
        public ResetPasswordCommand[] newArray(int size) {
            return new ResetPasswordCommand[size];
        }
    };
}
