package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.ui.fragments.ItemDetailFragment;

import java.util.ArrayList;

import static android.R.attr.tag;

public class EditNewsCommand extends Command {

    private TagModel mTagModel;
    private ArrayList<TagModel> mTagModels;
    private String mTitle;
    private String mText;
    private String mImage;
    private NewsUpdateModel mNewsUpdateModel;
    private int mId;

    public EditNewsCommand(TagModel tagModel, ArrayList<TagModel> tagModels, String title, String text, String image, int id) {
        mTagModel = tagModel;
        mTagModels = tagModels;
        mTitle = title;
        mText = text;
        mImage = image;
        mId = id;
        mNewsUpdateModel = new NewsUpdateModel(mTitle, mText, mImage, mTagModels);


    }

    public EditNewsCommand(Parcel in) {
        mTitle = in.readString();
        mText = in.readString();
        this.mTagModels = new ArrayList<TagModel>();
        in.readList(this.mTagModels, TagModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeString(mTitle);
        dest.writeString(mText);
        dest.writeString(mImage);
        dest.writeList(mTagModels);

    }

    @Override
    public void execute() {

        RestApiClient apiClient = App.getApiFactory().restClient();
        AuthToken authToken = new AuthToken();


        ResponseWrapper<NewsResponseModel> response = apiClient.editNews(mNewsUpdateModel, mId);
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {
                NewsResponseModel data = response.data();

                Log.i(ItemDetailFragment.DETAIL_FRAGMENT, "Response edit News Command: " + data.getId());

                notifySuccess(Bundle.EMPTY);

            } else {


                notifyError(Bundle.EMPTY);

            }

        } else {


            notifyError(Bundle.EMPTY);

        }

    }


    public static final Parcelable.Creator<EditNewsCommand> CREATOR = new Parcelable.Creator<EditNewsCommand>() {
        @Override
        public EditNewsCommand createFromParcel(Parcel source) {
            return new EditNewsCommand(source);
        }

        @Override
        public EditNewsCommand[] newArray(int size) {
            return new EditNewsCommand[size];
        }
    };
}
