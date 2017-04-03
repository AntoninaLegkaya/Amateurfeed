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
import com.dbbest.amateurfeed.data.CommentEntry;
import com.dbbest.amateurfeed.data.CreatorEntry;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.TagEntry;
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
        Vector<ContentValues> cVVector = new Vector<ContentValues>(data.size());
        int i = 0;
        for (NewsPreviewResponseModel preview : data) {
          // These are the values that will be collected.
          long _id;
          String title;
          String text;
          int likes;
          boolean isLike;
          String author;
          String authorImage;
          String createDate;
          String image;
          boolean isMy;
          ArrayList<TagModel> mTags;
          int _idTag;
          String mNameTag;

          ArrayList<UserFeedCommentModel> mComments;

          _id = preview.getId();
          title = preview.getTitle();
          text = preview.getText();
          mTags = preview.getTags();
          likes = preview.getLikes();
          isLike = preview.isLike();
          author = preview.getAuthor();
          authorImage = preview.getAuthorImage();
          createDate = preview.getCreateDate();
          image = preview.getImage();
          isMy = preview.isMy();

          mComments = preview.getComments();

          getCommentModel(mComments);

          putTagsInfo(_id, mTags);

          ContentValues previewValues = new ContentValues();

          previewValues.put(PreviewEntry._ID, _id);
          previewValues.put(PreviewEntry.COLUMN_TITLE, title);
          previewValues.put(PreviewEntry.COLUMN_TEXT, text);
          previewValues.put(PreviewEntry.COLUMN_LIKES, likes);
          previewValues.put(PreviewEntry.COLUMN_IS_LIKE, isLike ? 1 : 0);
          previewValues.put(PreviewEntry.COLUMN_AUTHOR, author);
          if (authorImage == null) {
            previewValues.put(PreviewEntry.COLUMN_AUTHOR_IMAGE, "");
          } else {
            previewValues.put(PreviewEntry.COLUMN_AUTHOR_IMAGE, authorImage);
          }
          previewValues.put(PreviewEntry.COLUMN_CREATE_DATE, createDate);
          if (image == null) {
            previewValues.put(PreviewEntry.COLUMN_IMAGE, "");
          } else {
            previewValues.put(PreviewEntry.COLUMN_IMAGE, image);
          }
          previewValues.put(PreviewEntry.COLUMN_IS_MY, isMy ? 1 : 0);

          cVVector.add(previewValues);
          i = i + 1;
        }

        if (cVVector.size() > 0) {
          ContentValues[] cvArray = new ContentValues[cVVector.size()];
          cVVector.toArray(cvArray);
          App.instance().getContentResolver()
              .bulkInsert(PreviewEntry.CONTENT_URI, cvArray);
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

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mNewsRequestModel, flags);
  }

  private void putTagsInfo(long _id, ArrayList<TagModel> mTags) {
    int _idTag;
    String mNameTag;
    Vector<ContentValues> cVTagsVector = new Vector<>(mTags.size());
    for (TagModel tag : mTags) {
      ContentValues tagValues = new ContentValues();
      _idTag = tag.getId();
      mNameTag = tag.getName();
      tagValues.put(TagEntry.COLUMN_TAG_ID, _idTag);
      tagValues.put(TagEntry.COLUMN_NAME, mNameTag);
      tagValues.put(TagEntry.COLUMN_PREVIEW_ID, _id);
      cVTagsVector.add(tagValues);
    }
    if (cVTagsVector.size() > 0) {
      ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
      cVTagsVector.toArray(cvArray);
      App.instance().getContentResolver()
          .bulkInsert(TagEntry.CONTENT_URI, cvArray);
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
      Vector<ContentValues> cVCommentVector = new Vector<>(mComments.size());
      Vector<ContentValues> cVCreatorVector = new Vector<>(mComments.size());

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

        Uri uriCreatorId = CreatorEntry.buildCreatorUriById(creator_id);

        putCreatorInfo(creator_id, mCreatorName, mImage, cVCreatorVector, creatorValues, mIsAdmin, uriCreatorId);

        createCommentDate = mComment.getCreateDate();
        childrenComment = mComment.getChildren();

        ContentValues commentValues = new ContentValues();
        commentValues.put(CommentEntry._ID, _idComment);
        commentValues.put(CommentEntry.COLUMN_POST_ID, post_id);
        commentValues.put(CommentEntry.COLUMN_BODY, body);
        commentValues.put(CommentEntry.COLUMN_PARENT_COMMENT_ID, parentCommentId);
        commentValues.put(CommentEntry.COLUMN_CREATOR_KEY, mCreator.getId());
        commentValues.put(CommentEntry.COLUMN_CREATE_DATE, createCommentDate);
        cVCommentVector.add(commentValues);

        childArray.add(childrenComment);

      }

      if (cVCommentVector.size() > 0) {
        ContentValues[] cvArray = new ContentValues[cVCommentVector.size()];
        cVCommentVector.toArray(cvArray);
        App.instance().getContentResolver()
            .bulkInsert(CommentEntry.CONTENT_URI, cvArray);

      }

      for (ArrayList<UserFeedCommentModel> feedCommentModels : childArray) {
        getCommentModel(feedCommentModels);
      }
    }
  }

  private void putCreatorInfo(int creator_id, String mCreatorName, String mImage, Vector<ContentValues> cVCreatorVector, ContentValues creatorValues,
      int mIsAdmin, Uri uriCreatorId) {
    Cursor cursor = App.instance().getContentResolver().query(
        uriCreatorId,
        null,
        null,
        null,
        null
    );
    if (!cursor.moveToFirst()) {

      creatorValues.put(CreatorEntry._ID, creator_id);
      creatorValues.put(CreatorEntry.COLUMN_NAME, mCreatorName);
      creatorValues.put(CreatorEntry.COLUMN_IS_ADMIN, mIsAdmin);
      creatorValues.put(CreatorEntry.COLUMN_IMAGE, mImage);

      cVCreatorVector.add(creatorValues);

      if (cVCreatorVector.size() > 0) {
        ContentValues[] cvArray = new ContentValues[cVCreatorVector.size()];
        cVCreatorVector.toArray(cvArray);
        App.instance().getContentResolver()
            .bulkInsert(CreatorEntry.CONTENT_URI, cvArray);

        cVCreatorVector.clear();
        cursor.close();
      }
    }
  }


}
