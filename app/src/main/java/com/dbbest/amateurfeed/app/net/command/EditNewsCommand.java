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
import com.dbbest.amateurfeed.model.NewsRequestModel;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class EditNewsCommand extends Command {

    private List<TagModel> mTagModels = new ArrayList<>();
    private String mTitle;
    private String mText;
    private String mImage;
    private NewsUpdateModel mNewsUpdateModel;
    private int mId;

    public EditNewsCommand(List<TagModel> tagModels, String title, String text, String image, int id) {
        mId = id;
        mNewsUpdateModel = new NewsUpdateModel(tagModels, title, text, image);


    }

    public EditNewsCommand(Parcel in) {
        super(in);
        mId = in.readInt();
        mNewsUpdateModel = in.readParcelable(NewsUpdateModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeInt(mId);
        dest.writeParcelable(mNewsUpdateModel, flags);

    }

    @Override
    public void execute() {

        RestApiClient apiClient = App.getApiFactory().restClient();
        AuthToken authToken = new AuthToken();

        Log.i(EditItemDetailFragment.DETAIL_FRAGMENT, "Edit News [_ID]: " + mId + "\n" +
                "Title: " + mNewsUpdateModel.getTitle() + '\n' +
                "Description: " + mNewsUpdateModel.getText() + '\n' +
                "Image: " + mNewsUpdateModel.getImage());
        for (TagModel model : mNewsUpdateModel.getTags()) {
            Log.i(EditItemDetailFragment.DETAIL_FRAGMENT, "Tag: " + model.getName() + '\n');
        }


        ResponseWrapper<NewsResponseModel> response = apiClient.editNews(authToken.bearer(), mNewsUpdateModel, mId);
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {
                NewsResponseModel data = response.data();

                Log.i(EditItemDetailFragment.DETAIL_FRAGMENT, "Updated  Item By ID:  " + data.getId());
                Bundle bundle = new Bundle();
                bundle.putParcelable("model", mNewsUpdateModel);
                notifySuccess(bundle);

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
