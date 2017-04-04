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
import com.dbbest.amateurfeed.data.FeedProvider;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.TagEntry;
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
  private NewsRequestModel newsRequestModel;

  public UpdateNewsCommand(int offset, int count) {
    AuthToken authToken = new AuthToken();
    newsRequestModel = new NewsRequestModel(offset, count, authToken.bearer());
  }

  private UpdateNewsCommand(Parcel in) {
    super(in);
    newsRequestModel = in.readParcelable(NewsRequestModel.class.getClassLoader());
  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();

    ResponseWrapper<ArrayList<NewsPreviewResponseModel>> response = apiClient
        .getNews(newsRequestModel.getAccessToken(), newsRequestModel.getOffset(),
            newsRequestModel.getCount());
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        ArrayList<NewsPreviewResponseModel> data = response.data();
        int i = 0;
        for (NewsPreviewResponseModel preview : data) {
          long id;
          String title;
          String text;
          int likes;
          boolean isLike;
          String author;
          String authorImage;
          String createDate;
          String image;
          boolean isMy;
          ArrayList<TagModel> tagsList;
          int _idTag;
          String mNameTag;
          ArrayList<UserFeedCommentModel> mComments;
          id = preview.getId();
          title = preview.getTitle();
          text = preview.getText();
          tagsList = preview.getTags();
          likes = preview.getLikes();
          isLike = preview.isLike();
          author = preview.getAuthor();
          authorImage = preview.getAuthorImage();
          createDate = preview.getCreateDate();
          image = preview.getImage();
          isMy = preview.isMy();
          mComments = preview.getComments();
          getCommentModel(mComments);

          putTagsInfo(id, tagsList);

          ContentValues previewValues = new ContentValues();
          previewValues.put(PreviewEntry._ID, id);
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

          int rowReturn = App.instance().getContentResolver()
              .update(PreviewEntry.CONTENT_URI, previewValues,
                  FeedProvider.sPreviewSelection, new String[]{String.valueOf(id)});
          if (rowReturn == 0) {
            Log.i(TAG, "This preview row not exist! try to insert it");
            App.instance().getContentResolver()
                .insert(PreviewEntry.CONTENT_URI, previewValues);
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

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(newsRequestModel, flags);
  }

  private void putTagsInfo(long id, ArrayList<TagModel> tagsList) {
    int _idTag;
    String mNameTag;
    for (TagModel tag : tagsList) {
      ContentValues tagValues = new ContentValues();
      _idTag = tag.getId();
      mNameTag = tag.getName();
      tagValues.put(TagEntry.COLUMN_TAG_ID, _idTag);
      tagValues.put(TagEntry.COLUMN_NAME, mNameTag);
      tagValues.put(TagEntry.COLUMN_PREVIEW_ID, id);
      int rowUpdate = App.instance().getContentResolver()
          .update(TagEntry.CONTENT_URI, tagValues,
              FeedProvider.sMultipleTagSelection,
              new String[]{String.valueOf(_idTag), String.valueOf(id)});
      if (rowUpdate == 0) {
        Log.i(TAG,
            "This tag not exist! try to insert it [tagId, preview_id, nameTag]: " + "["
                + _idTag + " , " + id + " , "
                + mNameTag + "]");
        App.instance().getContentResolver()
            .insert(TagEntry.CONTENT_URI, tagValues);
      }
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

        Uri uriCreatorId = CreatorEntry.buildCreatorUriById(creator_id);

        putCreatorInfo(creator_id, mCreatorName, mImage, creatorValues, mIsAdmin, uriCreatorId);
        createCommentDate = mComment.getCreateDate();
        childrenComment = mComment.getChildren();

        ContentValues commentValues = new ContentValues();
        commentValues.put(CommentEntry._ID, _idComment);
        commentValues.put(CommentEntry.COLUMN_POST_ID, post_id);
        commentValues.put(CommentEntry.COLUMN_BODY, body);
        commentValues.put(CommentEntry.COLUMN_PARENT_COMMENT_ID, parentCommentId);
        commentValues.put(CommentEntry.COLUMN_CREATOR_KEY, mCreator.getId());
        commentValues.put(CommentEntry.COLUMN_CREATE_DATE, createCommentDate);

        int i = App.instance().getContentResolver()
            .update(CommentEntry.CONTENT_URI, commentValues,
                FeedProvider.sCommentSelection, new String[]{String.valueOf(_idComment)});
        if (i == 0) {
          Log.i(TAG, "This comment row not exist! try to insert it");
          App.instance().getContentResolver()
              .insert(CommentEntry.CONTENT_URI, commentValues);
        }

        childArray.add(childrenComment);

      }

      for (ArrayList<UserFeedCommentModel> feedCommentModels : childArray) {
        getCommentModel(feedCommentModels);
      }
    }
  }

  private void putCreatorInfo(int creator_id, String mCreatorName, String mImage, ContentValues creatorValues, int mIsAdmin, Uri uriCreatorId) {
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
      int i = App.instance().getContentResolver()
          .update(CreatorEntry.CONTENT_URI, creatorValues,
              FeedProvider.sCreatorSelection, new String[]{String.valueOf(creator_id)});

      if (i == 0) {
        Log.i(TAG, "This creator row not exist! try to insert it");
        App.instance().getContentResolver()
            .insert(CreatorEntry.CONTENT_URI, creatorValues);
      }
    }
  }

}