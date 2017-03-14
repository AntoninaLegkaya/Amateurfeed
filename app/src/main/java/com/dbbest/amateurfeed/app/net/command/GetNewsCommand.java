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
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.NewsRequestModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.dbbest.amateurfeed.model.UserFeedCreator;
import java.util.ArrayList;
import java.util.Vector;


public class GetNewsCommand extends Command {

  public static final Parcelable.Creator<GetNewsCommand> CREATOR = new Parcelable.Creator<GetNewsCommand>() {
    @Override
    public GetNewsCommand createFromParcel(Parcel source) {
      return new GetNewsCommand(source);
    }

    @Override
    public GetNewsCommand[] newArray(int size) {
      return new GetNewsCommand[size];
    }
  };
  private String TAG = GetNewsCommand.class.getName();
  private NewsRequestModel mNewsRequestModel;

  public GetNewsCommand(int offset, int count) {
    AuthToken authToken = new AuthToken();
    mNewsRequestModel = new NewsRequestModel(offset, count, authToken.bearer());
  }


  private GetNewsCommand(Parcel in) {
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
        // Insert the new news information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(data.size());

        int i = 0;
        for (NewsPreviewResponseModel preview : data) {
          // These are the values that will be collected.

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

          Vector<ContentValues> cVTagsVector = new Vector<ContentValues>(mTags.size());

          for (TagModel tag : mTags) {
            ContentValues tagValues = new ContentValues();
            _idTag = tag.getId();
            mNameTag = tag.getName();
            tagValues.put(FeedContract.TagEntry.COLUMN_TAG_ID, _idTag);
            tagValues.put(FeedContract.TagEntry.COLUMN_NAME, mNameTag);
            tagValues.put(FeedContract.TagEntry.COLUMN_PREVIEW_ID, _id);

            cVTagsVector.add(tagValues);

          }

          // add  Tags to database
          if (cVTagsVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
            cVTagsVector.toArray(cvArray);
            App.instance().getContentResolver()
                .bulkInsert(FeedContract.TagEntry.CONTENT_URI, cvArray);


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

          cVVector.add(previewValues);
          i = i + 1;
        }

        // add to  preview table
        if (cVVector.size() > 0) {
          ContentValues[] cvArray = new ContentValues[cVVector.size()];
          cVVector.toArray(cvArray);
          App.instance().getContentResolver()
              .bulkInsert(FeedContract.PreviewEntry.CONTENT_URI, cvArray);
        }
        Log.i(TAG, "Get News Complete. " + cVVector.size() + " Inserted");
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
      Vector<ContentValues> cVCommentVector = new Vector<ContentValues>(mComments.size());
      Vector<ContentValues> cVCreatorVector = new Vector<ContentValues>(mComments.size());

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

          cVCreatorVector.add(creatorValues);

          if (cVCreatorVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVCreatorVector.size()];
            cVCreatorVector.toArray(cvArray);
            App.instance().getContentResolver()
                .bulkInsert(FeedContract.CreatorEntry.CONTENT_URI, cvArray);

            cVCreatorVector.clear();
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
        cVCommentVector.add(commentValues);

        childArray.add(childrenComment);

      }

      if (cVCommentVector.size() > 0) {
        ContentValues[] cvArray = new ContentValues[cVCommentVector.size()];
        cVCommentVector.toArray(cvArray);
        App.instance().getContentResolver()
            .bulkInsert(FeedContract.CommentEntry.CONTENT_URI, cvArray);

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
