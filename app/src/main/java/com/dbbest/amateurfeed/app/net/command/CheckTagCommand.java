package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.LoginRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsPreviewResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.CurrentUser;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.ui.fragments.ItemDetailFragment;
import com.dbbest.amateurfeed.utils.Utils;

import java.util.ArrayList;

import static android.R.attr.data;

public class CheckTagCommand extends Command {
    private String mTagName;


    public CheckTagCommand(String tag) {
        mTagName = tag;
    }

    private CheckTagCommand(Parcel in) {
        super(in);
        mTagName = in.readString();
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeString(mTagName);
    }

    @Override
    public void execute() {


        RestApiClient apiClient = App.getApiFactory().restClient();
        AuthToken authToken = new AuthToken();
        ResponseWrapper<ArrayList<TagModel>> response = apiClient.checkTagName(authToken.bearer(), mTagName);
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {
                ArrayList<TagModel> data = response.data();

                for (TagModel tag : data) {
                    Log.i(ItemDetailFragment.DETAIL_FRAGMENT, "Get Check tag: " + tag.getName());

                }
                notifySuccess(Bundle.EMPTY);
            } else {


                notifyError(Bundle.EMPTY);

            }

        } else {


            notifyError(Bundle.EMPTY);

        }


    }

    public static final Parcelable.Creator<CheckTagCommand> CREATOR = new Parcelable.Creator<CheckTagCommand>() {
        @Override
        public CheckTagCommand createFromParcel(Parcel source) {
            return new CheckTagCommand(source);
        }

        @Override
        public CheckTagCommand[] newArray(int size) {
            return new CheckTagCommand[size];
        }
    };
