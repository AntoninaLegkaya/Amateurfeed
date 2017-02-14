package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.LikeModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.IdModel;
import com.dbbest.amateurfeed.utils.Utils;

/**
 * Created by antonina on 10.02.17.
 */

public class SetLikeCommand extends Command {
    private LikeModel mLikeRequestModel;
    private final long mId;
    private boolean mIsLike;

    private SetLikeCommand(Parcel in) {
        super(in);
        mLikeRequestModel = in.readParcelable(LikeModel.class.getClassLoader());
        mId = in.readInt();
    }

    public SetLikeCommand(long id, boolean isLike) {
        mId = id;
        mIsLike = isLike;
        mLikeRequestModel = new LikeModel(mIsLike);
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mLikeRequestModel, flags);
        dest.writeLong(mId);
    }


    @Override
    public void execute() {

        Log.i(Utils.TAG_LOG, "Execute Set Like command");
        Log.i(Utils.TAG_LOG, "Like command isLike: " + mLikeRequestModel.isLike());
        Log.i(Utils.TAG_LOG, "Like command id: " + mId);

        AuthToken authToken = new AuthToken();
        RestApiClient apiClient = App.getApiFactory().restClient();
        Log.i(Utils.TAG_LOG, "Like command (long) id: " +mId);
        ResponseWrapper<Object> response = apiClient.isLike(authToken.bearer(),mId, mLikeRequestModel);
        if (response != null) {
            if (response.isSuccessful()) {
                Log.i(Utils.TAG_LOG, "Response message: " + response.message());
                Log.i(Utils.TAG_LOG, "Response message: isSuccessful: " + response.isSuccessful());
                Log.i(Utils.TAG_LOG, "Response message code: " + response.code());



                notifySuccess(Bundle.EMPTY);

            } else {
                notifyError(Bundle.EMPTY);
            }


        } else {
            notifyError(Bundle.EMPTY);
        }

    }


    public static final Parcelable.Creator<SetLikeCommand> CREATOR = new Parcelable.Creator<SetLikeCommand>() {
        @Override
        public SetLikeCommand createFromParcel(Parcel source) {
            return new SetLikeCommand(source);
        }

        @Override
        public SetLikeCommand[] newArray(int size) {
            return new SetLikeCommand[size];
        }
    };
}
