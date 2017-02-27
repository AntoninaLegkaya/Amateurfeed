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
import static android.R.attr.tag;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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


        //TODO RestApiClient
        RestApiClient apiClient = App.getApiFactory().restClient();
        AuthToken authToken = new AuthToken();


        ResponseWrapper<ArrayList<TagModel>> response = apiClient.checkTagName(authToken.bearer(), mTagName);
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {
                ArrayList<TagModel> data = response.data();
                Bundle bundle = new Bundle();
                boolean flag = false;
                for (TagModel tag : data) {


                    if (mTagName.equals(tag.getName())) {
                        flag = true;
                        bundle.putParcelable("tagModel", new TagModel(tag.getId(), tag.getName()));
                        Log.i(ItemDetailFragment.DETAIL_FRAGMENT, "I Get from Server [Tag]: " + tag.getName());
                    }


                }
//                bundle.putParcelableArrayList("tags", data);
                if (flag) {
                    notifySuccess(bundle);
                } else {
                    notifyError(bundle);
                }

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
}