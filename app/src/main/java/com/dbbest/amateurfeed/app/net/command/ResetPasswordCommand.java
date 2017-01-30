package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.ResetRequestPasswordModel;
import com.dbbest.amateurfeed.app.net.response.ResetResponse;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;

/**
 * Created by antonina on 19.01.17.
 */

public class ResetPasswordCommand extends Command {
    private final ResetRequestPasswordModel mResetRequest;

    public ResetPasswordCommand(String email) {
        mResetRequest = new ResetRequestPasswordModel(email);
    }

    protected ResetPasswordCommand(Parcel in) {

        super(in);
        mResetRequest = in.readParcelable(ResetRequestPasswordModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mResetRequest, flags);
    }

    @Override
    public void execute() {

        RestApiClient apiClient = App.getApiFactory().restClient();
        ResponseWrapper<Object> response = apiClient.forgotPassword(mResetRequest);
        if (response != null) {
            if (response.isSuccessful()) {

                notifySuccess(Bundle.EMPTY);


            } else {
//                ("Forgot password response is null");
                notifyError(Bundle.EMPTY);
            }
        } else {
            //TODO Reset response is null!");
        }

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
