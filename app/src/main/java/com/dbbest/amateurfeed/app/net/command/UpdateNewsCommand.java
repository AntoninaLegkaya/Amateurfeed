package com.dbbest.amateurfeed.app.net.command;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.NewsPreviewResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedProvider;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.NewsRequestModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.dbbest.amateurfeed.model.UserFeedCreator;
import java.util.ArrayList;

public class UpdateNewsCommand extends Command {

  public static final Parcelable.Creator<UpdateNewsCommand> CREATOR = new Parcelable.Creator<UpdateNewsCommand>() {
    @Override
    public UpdateNewsCommand createFromParcel(Parcel source) {
      return new UpdateNewsCommand(source);
    }

    @Override
    public UpdateNewsCommand[] newArray(int size) {
      return new UpdateNewsCommand[size];
    }
  };
  private final String TAG = UpdateNewsCommand.class.getName();
  private NewsRequestModel mNewsRequestModel;

  public UpdateNewsCommand(int offset, int count) {
    AuthToken authToken = new AuthToken();
    mNewsRequestModel = new NewsRequestModel(offset, count, authToken.bearer());
  }


  private UpdateNewsCommand(Parcel in) {
    super(in);
    mNewsRequestModel = in.readParcelable(NewsRequestModel.class.getClassLoader());
  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();

    ResponseWrapper<ArrayList<NewsPreviewResponseModel>> response = apiClient
        .getNews(mNewsRequestModel.getAccessToken(), mNewsRequestModel.getOffset(),
            mNewsRequestModel.getCount());

    if (response != null) {

      if (response.isSuccessful() && response.data() != null) {
        ArrayList<NewsPreviewResponseModel> data = response.data();

        int i = 0;
        for (NewsPreviewResponseModel preview : data) {

          long _id;
          String mTitle;
          String mText;

          int mLikes;
          boolean mIsLike;
          String mAuthor;
          String mAuthorImage;
          String mCreateDate;
          String mImage;
          boolean mIsMy;

          ArrayList<TagModel> mTags;
          int _idTag;
          String mNameTag;

          ArrayList<UserFeedCommentModel> mComments;

          _id = preview.getId();
          mTitle = preview.getTitle();
          mText = preview.getText();
          mTags = preview.getTags();
          mLikes = preview.getLikes();
          mIsLike = preview.isLike();
          mAuthor = preview.getAuthor();
          mAuthorImage = preview.getAuthorImage();
          mCreateDate = preview.getCreateDate();
          mImage = preview.getImage();
          mIsMy = preview.isMy();

          mComments = preview.getComments();

          getCommentModel(mComments);

          // Insert the tags news information into the database

          for (TagModel tag : mTags) {
            ContentValues tagValues = new ContentValues();
            _idTag = tag.getId();
            mNameTag = tag.getName();
            tagValues.put(FeedContract.TagEntry.COLUMN_TAG_ID, _idTag);
            tagValues.put(FeedContract.TagEntry.COLUMN_NAME, mNameTag);
            tagValues.put(FeedContract.TagEntry.COLUMN_PREVIEW_ID, _id);
            try {
              App.instance().getContentResolver()
                  .insert(FeedContract.TagEntry.CONTENT_URI, tagValues);
            } catch (Exception e) {
              Log.e(TAG, "This tag row already exist! try to update it");
              App.instance().getContentResolver()
                  .update(FeedContract.TagEntry.CONTENT_URI, tagValues, FeedProvider.sTagSelection,
                      new String[]{String.valueOf(_id)});
            }


          }

          ContentValues previewValues = new ContentValues();

          previewValues.put(FeedContract.PreviewEntry._ID, _id);
          previewValues.put(FeedContract.PreviewEntry.COLUMN_TITLE, mTitle);
          previewValues.put(FeedContract.PreviewEntry.COLUMN_TEXT, mText);
          previewValues.put(FeedContract.PreviewEntry.COLUMN_LIKES, mLikes);
          previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_LIKE, mIsLike ? 1 : 0);
          previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR, mAuthor);
          if (mAuthorImage == null) {
            previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE, "");
          } else {
            previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE, mAuthorImage);
          }
          previewValues.put(FeedContract.PreviewEntry.COLUMN_CREATE_DATE, mCreateDate);
          if (mImage == null) {
            previewValues.put(FeedContract.PreviewEntry.COLUMN_IMAGE, "");
          } else {
            previewValues.put(FeedContract.PreviewEntry.COLUMN_IMAGE, mImage);
          }
          previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_MY, mIsMy ? 1 : 0);

          try {
            App.instance().getContentResolver()
                .insert(FeedContract.PreviewEntry.CONTENT_URI, previewValues);
          } catch (Exception e) {
            Log.e(TAG, "This preview row already exist! try to update it");
            App.instance().getContentResolver()
                .update(FeedContract.PreviewEntry.CONTENT_URI, previewValues,
                    FeedProvider.sPreviewSelection, new String[]{String.valueOf(_id)});
          }

        }

        notifySuccess(Bundle.EMPTY);
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }

  }

  private void getCommentModel(ArrayList<UserFeedCommentModel> mComments) {
    ArrayList<ArrayList<UserFeedCommentModel>> childArray = new ArrayList<>();
    int _idComment;
    int post_id;
    String body;
    int parentCommentId;
    UserFeedCreator mCreator;
    String createCommentDate;
    ArrayList<UserFeedCommentModel> childrenComment;

    int creator_id;
    String mCreatorName;
    String mImage;
    if (mComments != null) {

      for (UserFeedCommentModel mComment : mComments) {

        ContentValues creatorValues = new ContentValues();

        _idComment = mComment.getId();
        post_id = mComment.getPostId();
        body = mComment.getBody();
        parentCommentId = mComment.getParentCommentId();

        mCreator = mComment.getCreator();
        creator_id = mCreator.getId();
        mCreatorName = mCreator.getName();
        int mIsAdmin = (mCreator.isAdmin() ? 1 : 0);
        mImage = mCreator.getImage();

        Uri uriCreatorId = FeedContract.CreatorEntry.buildCreatorUriById(creator_id);

        Cursor cursor = App.instance().getContentResolver().query(
            uriCreatorId,
            null,
            null,
            null,
            null
        );
        if (!cursor.moveToFirst()) {

          creatorValues.put(FeedContract.CreatorEntry._ID, creator_id);
          creatorValues.put(FeedContract.CreatorEntry.COLUMN_NAME, mCreatorName);
          creatorValues.put(FeedContract.CreatorEntry.COLUMN_IS_ADMIN, mIsAdmin);
          creatorValues.put(FeedContract.CreatorEntry.COLUMN_IMAGE, mImage);
          try {
            App.instance().getContentResolver()
                .insert(FeedContract.CreatorEntry.CONTENT_URI, creatorValues);
          } catch (Exception e) {
            Log.e(TAG, "This creator row already exist! try to update it");
            App.instance().getContentResolver()
                .update(FeedContract.CreatorEntry.CONTENT_URI, creatorValues,
                    FeedProvider.sCreatorSelection, new String[]{String.valueOf(creator_id)});
          }
        }
        createCommentDate = mComment.getCreateDate();
        childrenComment = mComment.getChildren();

        ContentValues commentValues = new ContentValues();
        commentValues.put(FeedContract.CommentEntry._ID, _idComment);
        commentValues.put(FeedContract.CommentEntry.COLUMN_POST_ID, post_id);
        commentValues.put(FeedContract.CommentEntry.COLUMN_BODY, body);
        commentValues.put(FeedContract.CommentEntry.COLUMN_PARENT_COMMENT_ID, parentCommentId);
        commentValues.put(FeedContract.CommentEntry.COLUMN_CREATOR_KEY, mCreator.getId());
        commentValues.put(FeedContract.CommentEntry.COLUMN_CREATE_DATE, createCommentDate);

        try {
          App.instance().getContentResolver()
              .insert(FeedContract.CommentEntry.CONTENT_URI, commentValues);
        } catch (Exception e) {
          Log.e(TAG, "This comment row already exist! try to update it");
          App.instance().getContentResolver()
              .update(FeedContract.CommentEntry.CONTENT_URI, commentValues,
                  FeedProvider.sCommentSelection, new String[]{String.valueOf(_idComment)});
        }

        childArray.add(childrenComment);

      }

      for (ArrayList<UserFeedCommentModel> feedCommentModels : childArray) {
        getCommentModel(feedCommentModels);
      }
    }
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mNewsRequestModel, flags);
  }

}