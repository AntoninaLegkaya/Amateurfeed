package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AbuseModel;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.utils.Utils;

public class DeleteNewsCommand extends Command {
    private AbuseModel mAbuseModel;


    public DeleteNewsCommand(Parcel in) {
        super(in);
        mAbuseModel = in.readParcelable(AbuseModel.class.getClassLoader());

    }

    public DeleteNewsCommand(int id, String comment) {

        mAbuseModel = new AbuseModel(id, comment);
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mAbuseModel, flags);
    }

    @Override
    public void execute() {

        Log.i(Utils.TAG_LOG, "Execute Delete News command");

        AuthToken authToken = new AuthToken();
        RestApiClient apiClient = App.getApiFactory().restClient();
        ResponseWrapper<Object> response = apiClient.postAbuse(authToken.bearer(), mAbuseModel);
        if (response != null) {
            if (response.isSuccessful()) {

                notifySuccess(Bundle.EMPTY);


            } else {

                notifyError(Bundle.EMPTY);
            }


        } else {
            notifyError(Bundle.EMPTY);
        }

    }


    public static final Parcelable.Creator<DeleteNewsCommand> CREATOR = new Parcelable.Creator<DeleteNewsCommand>() {
        @Override
        public DeleteNewsCommand createFromParcel(Parcel source) {
            return new DeleteNewsCommand(source);
        }

        @Override
        public DeleteNewsCommand[] newArray(int size) {
            return new DeleteNewsCommand[size];
        }
    };
}
