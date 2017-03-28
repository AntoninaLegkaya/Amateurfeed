package com.dbbest.amateurfeed.ui.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadResultReceiver;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadResultReceiver.Receiver;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadService;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedContract.CommentEntry;
import com.dbbest.amateurfeed.data.adapter.CommentsAdapter;
import com.dbbest.amateurfeed.data.adapter.TagAdapter;
import com.dbbest.amateurfeed.model.CommentModel;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import java.util.ArrayList;
import java.util.Vector;


public class EditItemDetailFragment extends BaseEditDetailFragment implements
    LoaderCallbacks<Cursor>, OnClickListener,
    Receiver {

  public static final String DETAIL_URI = "URI";
  public static final String DETAIL_TYPE = "TYPE_ITEM";
  private static final String PARAM_KEY = "param_key";
  private static final int DETAIL_NEWS_LOADER = 1;
  public static int RESULT_LOAD_IMAGE = 1;
  public ImageView mIconView;
  public TextView mFullNameView;
  public TextView mDateView;
  public TextView mLikesCountView;
  public TextView mCommentCountView;
  public TextView mCommentView;
  public ImageButton mLikeButton;
  public Button mCommentButton;
  public ImageButton mEditButton;
  public ImageButton mRemoveButton;
  private String TAG = EditItemDetailFragment.class.getName();
  private int mLayoutType;
  private TextView mChangeIconLink;
  private String mCurrentPhotoPath;
  private RecyclerView mHorizontalList;
  private RecyclerView mCommentList;
  private CommentsAdapter mCommentsAdapter;
  private TagAdapter mTagAdapter;
  private BlobUploadResultReceiver mReceiver;


  public EditItemDetailFragment() {
    setHasOptionsMenu(true);
  }

  public static EditItemDetailFragment newInstance(String key) {
    EditItemDetailFragment fragment = new EditItemDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PARAM_KEY, key);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {

    getLoaderManager().initLoader(DETAIL_NEWS_LOADER, null, this);
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.action_back_menu, menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    String upTitle = null;
    String upDescription = null;
    switch (item.getItemId()) {
      case android.R.id.home:
        ((Callback) getActivity()).moveToFeedFragment();
        return true;
      case R.id.action: {
        if (mLayoutType == R.layout.fragment_item_edit_my_detail) {
          String textDescription = mDescriptionView.getText().toString();
          if (textDescription != null) {
            String[] tags = Utils.getTagsPattern(textDescription);
            if (tags != null) {
              for (String tag : tags) {
                mPresenter.checkTag(tag);
              }
            }
          }
        }
        return true;
      }
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    Bundle arguments = getArguments();
    if (arguments != null) {
      mUriPreview = arguments.getParcelable(EditItemDetailFragment.DETAIL_URI);
      mLayoutType = arguments.getInt(EditItemDetailFragment.DETAIL_TYPE);
    }
    View itemView = inflater.inflate(mLayoutType, container, false);

    Toolbar toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

    mIconView = (ImageView) itemView.findViewById(R.id.list_item_icon);
    mImageView = (ImageView) itemView.findViewById(R.id.list_item_image);
    mDateView = (TextView) itemView.findViewById(R.id.list_item_date_textview);
    mFullNameView = (TextView) itemView.findViewById(R.id.list_item_name_textview);
    mTitleView = (TextView) itemView.findViewById(R.id.list_item_title);
    mLikesCountView = (TextView) itemView.findViewById(R.id.list_item_likes_count);
    mCommentCountView = (TextView) itemView.findViewById(R.id.list_item_comment_count);
    mDescriptionView = (TextView) itemView.findViewById(R.id.list_item_description);
    mCommentView = (TextView) itemView.findViewById(R.id.item_comment_text);

    mLikeButton = (ImageButton) itemView.findViewById(R.id.like_button);
    mLikeButton.setOnClickListener(this);
    mCommentButton = (Button) itemView.findViewById(R.id.add_comment_button);
    mCommentButton.setOnClickListener(this);

    mChangeIconLink = (TextView) itemView.findViewById(R.id.change_image_link);
    if (mChangeIconLink != null) {
      mChangeIconLink.setOnClickListener(this);
    }

    mEditButton = (ImageButton) itemView.findViewById(R.id.edit_button);
    if (mEditButton != null) {
      mEditButton.setOnClickListener(this);
    }

    mRemoveButton = (ImageButton) itemView.findViewById(R.id.delete_button);
    mRemoveButton.setOnClickListener(this);

    mHorizontalList = (RecyclerView) itemView.findViewById(R.id.list_tags_view);
    mHorizontalList.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    mTagAdapter = new TagAdapter(null, 0);
    mHorizontalList.setAdapter(mTagAdapter);

    mCommentList = (RecyclerView) itemView.findViewById(R.id.comment_recycler_view);
    mCommentList.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    mCommentsAdapter = new CommentsAdapter(null, 0);
    mCommentList.setAdapter(mCommentsAdapter);

    return itemView;
  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(getActivity(),
        mUriPreview,
        FeedNewsFragment.PREVIEW_COLUMNS,
        null,
        null,
        null
    );
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (loader.getId() == DETAIL_NEWS_LOADER) {
      if (data.moveToFirst()) {
        long mIdPreview = data.getLong(FeedNewsFragment.COL_FEED_ID);
        Glide.with(this)
            .load(data.getString(FeedNewsFragment.COL_AUTHOR_IMAGE))
            .error(R.drawable.art_snow)
            .crossFade()
            .into(mIconView);
        String fullName =
            data.getString(FeedNewsFragment.COL_AUTHOR);
        mFullNameView.setText(fullName);
        String description = data.getString(FeedNewsFragment.COL_TEXT);
        if (description != null) {
          mDescriptionView.setText(description);
        }
        String title =
            data.getString(FeedNewsFragment.COL_TITLE);
        mTitleView.setText(title);
        String date =
            data.getString(FeedNewsFragment.COL_CREATE_DATE);
        String day = null;
        if (date != null) {
          day = Utils.getFriendlyDayString(getActivity(), Utils.getLongFromString(date), true);
          if (day == null) {
            mDateView.setText(date);
          } else {
            mDateView.setText(day);
          }
        }
        int countLikes =
            data.getInt(FeedNewsFragment.COL_LIKES);
        mLikesCountView.setText(String.valueOf(countLikes));
        SimpleTarget target = new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            mImageView.setImageBitmap(bitmap);
          }
        };
        mUploadImagePath = data.getString(FeedNewsFragment.COL_IMAGE);
        Glide.with(this)
            .load(mUploadImagePath)
            .asBitmap()
            .error(R.drawable.art_snow)
            .into(target);
        int mIsLike = data.getInt(FeedNewsFragment.COL_IS_LIKE);
        if (mIsLike == 1) {
          mLikeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
          mLikeButton.setTag("1");
        } else if (mIsLike == 0) {
          mLikeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
          mLikeButton.setTag("0");
        }
        Cursor mCursorComments = getCommentCursor(mIdPreview);
        if (mCursorComments.moveToFirst()) {
          mCommentsAdapter.swapCursor(mCursorComments);
        }
        int count = mCursorComments.getCount();
        mCommentCountView.setText(String.valueOf(count));
        Cursor mCursorTags = getTagCursor(mIdPreview);
        if (mCursorTags.moveToFirst()) {
          mTagAdapter.swapCursor(mCursorTags);
        }
      }
    }

  }


  private Cursor getCommentCursor(long mIdPreview) {
    Uri uriCommentList = FeedContract.CommentEntry.getCommentsListById(mIdPreview);
    String sortOrder = FeedContract.PreviewEntry.COLUMN_CREATE_DATE + " DESC";
    return App.instance().getContentResolver().query(
        uriCommentList,
        null,
        null,
        null,
        sortOrder
    );
  }

  public ArrayList<CommentModel> getComments(long mIdPreview) {
    Cursor cursor = getCommentCursor(mIdPreview);
    ArrayList<CommentModel> commentModels = new ArrayList<CommentModel>();
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        do {
          CommentModel commentModel = new CommentModel(
              cursor.getInt(FeedNewsFragment.COL_COMMENT_UNIC_ID),
              cursor.getInt(FeedNewsFragment.COL_COMMENT_POST_ID),
              cursor.getInt(FeedNewsFragment.COL_COMMENT_CREATOR_KEY),
              cursor.getString(FeedNewsFragment.COL_COMMENT_BODY),
              cursor.getInt(FeedNewsFragment.COL_COMMENT_PARENT_COMMENT_ID),
              cursor.getString(FeedNewsFragment.COL_COMMENT_CREATE_DATE));
          commentModels.add(commentModel);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return commentModels;
  }

  public ArrayList<TagModel> getTags(long mIdPreview) {
    Cursor cursor;
    cursor = getTagCursor(mIdPreview);
    ArrayList<TagModel> tagModels = new ArrayList<TagModel>();
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        do {
          TagModel tagModel = new TagModel(cursor.getInt(FeedNewsFragment.COL_TAG_ID),
              cursor.getString(FeedNewsFragment.COL_TAG_NAME));
          tagModels.add(tagModel);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return tagModels;
  }

  @Override
  public void addTagToItemDetail(Bundle bundle) {
    TagModel tagModel = bundle.getParcelable("tagModel");
    ArrayList<TagModel> tags;
    tags = getTags(FeedContract.PreviewEntry.getIdFromUri(mUriPreview));
    if (!bundle.isEmpty() && tagModel != null) {

      Vector<ContentValues> cVTagsVector = new Vector<ContentValues>(1);
      boolean flag = true;
      for (TagModel model : tags) {
        if (tagModel.getName().equals(model.getName())) {
          flag = false;
        }
      }
      if (flag) {
        ContentValues tagValues = new ContentValues();
        tagValues.put(FeedContract.TagEntry.COLUMN_TAG_ID, tagModel.getId());
        tagValues.put(FeedContract.TagEntry.COLUMN_NAME, tagModel.getName());
        tagValues.put(FeedContract.TagEntry.COLUMN_PREVIEW_ID,
            FeedContract.PreviewEntry.getIdFromUri(mUriPreview));
        cVTagsVector.add(tagValues);
        if (cVTagsVector.size() > 0) {
          ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
          cVTagsVector.toArray(cvArray);
          App.instance().getContentResolver()
              .bulkInsert(FeedContract.TagEntry.CONTENT_URI, cvArray);
          Log.i(TAG, "Add new tag: " + tagModel.getName());
        }
      }
      Cursor newTagCursor = getTagCursor(FeedContract.PreviewEntry.getIdFromUri(mUriPreview));
      Log.i(TAG, "Swap cursor new tags ");
      mTagAdapter.swapCursor(newTagCursor);
    }
    checkUpdateImage();

  }

  public void checkUpdateImage() {
    if (mUriImageSelected != null) {
      Log.i(TAG, "You selected new image ");
      mReceiver = new BlobUploadResultReceiver(new Handler());
      mReceiver.setReceiver(this);
      Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
          BlobUploadService.class);
      intent.putExtra("receiver", mReceiver);
      intent.putExtra("uri", mUriImageSelected);
      getActivity().startService(intent);
    } else {
      CloudPreferences preferences = new CloudPreferences();
      if (mUploadImagePath != null) {
        Log.i(TAG, "Get old path to image: " + mUploadImagePath);
      }
      invokeEditNewsCommand();
    }
  }

  private void invokeEditNewsCommand() {

    Log.i(TAG, "Invoke edit news Command ");
    String upTitle = null;
    String upDescription = null;
    long idPreview = FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
    ArrayList<TagModel> newTagsArray = getTags(idPreview);
    if (mTitleView != null) {
      upTitle = mTitleView.getText().toString();
    }
    if (mDescriptionView != null) {
      upDescription = mDescriptionView.getText().toString();
    }
    if (upTitle != null && upDescription != null && newTagsArray != null) {
      mPresenter
          .updateNews(newTagsArray, upTitle, upDescription, mUploadImagePath, (int) idPreview);
    }
  }

  protected Cursor getTagCursor(long mIdPreview) {
    Uri uriTagsList = FeedContract.TagEntry.getTagsListById(mIdPreview);
    return App.instance().getContentResolver().query(
        uriTagsList,
        null,
        null,
        null,
        null
    );
  }

  private void updateDescriptionColumnPreview(String textDescription) {
    ContentValues values = new ContentValues();
    values.put(FeedContract.PreviewEntry.COLUMN_TEXT, textDescription);
    if (mUriPreview != null) {
      long id = FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
      Uri uriPreviewId = FeedContract.PreviewEntry.buildSetDescriptionInPreviewById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  private void updateTitleColumnPreview(String textTitle) {
    ContentValues values = new ContentValues();
    values.put(FeedContract.PreviewEntry.COLUMN_TITLE, textTitle);
    if (mUriPreview != null) {
      long id = FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
      Uri uriPreviewId = FeedContract.PreviewEntry.buildSetTitleInPreviewById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  private void updateImageUrlColumnPreview(String url) {
    ContentValues values = new ContentValues();
    values.put(FeedContract.PreviewEntry.COLUMN_IMAGE, url);
    if (mUriPreview != null) {
      long id = FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
      Uri uriPreviewId = FeedContract.PreviewEntry.buildSetImageUrlInPreviewById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
  }

  public void refreshItemDetailsFragmentLoader() {
    if (getLoaderManager().getLoader(DETAIL_NEWS_LOADER) != null) {
      getLoaderManager().restartLoader(DETAIL_NEWS_LOADER, null, this);
    }
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.add_comment_button) {
      if (mUriPreview != null && mCommentView != null && mCommentView.getText() != null) {
        Log.i(TAG, "Start invoke Add Comment Command: new body: " + mCommentView.getText());
        int postId = (int) FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
        mPresenter.postComment(postId, mCommentView.getText().toString(), null);
      }
    }
    if (view.getId() == R.id.edit_button) {
      ((Callback) getActivity()).onEditItemSelected(mUriPreview);
    }
    if (view.getId() == R.id.change_image_link) {
      selectImage();
    }
    if (view.getId() == R.id.delete_button) {
      ((Callback) getActivity()).onDeleteItemSelected(mUriPreview);
    }
    if (view.getId() == R.id.like_button) {
      int mCountIsLikes = 0;
      int isLikeFlag = 0;
      String mCountLikes = mLikesCountView.getText().toString();
      if (mCountLikes != null) {
        mCountIsLikes = Integer.parseInt(mCountLikes);
      }
        if (mLikeButton.getTag() == "1") {
          isLikeFlag = 0;
          mLikeButton.setTag("0");
          mLikeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
          mCountIsLikes = mCountIsLikes - 1;
        } else if (mLikeButton.getTag() == "0") {
          isLikeFlag = 1;
          mLikeButton.setTag("1");
          mLikeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
          mCountIsLikes = mCountIsLikes + 1;
        }
        mLikesCountView.setText(String.valueOf(mCountIsLikes));
        ((Callback) getActivity()).onLikeItemSelected(mUriPreview, isLikeFlag, mCountIsLikes);
    }
  }

  @Override
  public void updateDetailsFields(Bundle data) {
    NewsUpdateModel mNewsUpdateModel = data.getParcelable("model");
    updateImageUrlColumnPreview(mNewsUpdateModel.getImage());
    updateTitleColumnPreview(mNewsUpdateModel.getTitle());
    updateDescriptionColumnPreview(mNewsUpdateModel.getText());
  }


  @Override
  public void showSuccessEditNewsDialog() {
    UIDialogNavigation.showWarningDialog(R.string.set_edit_success)
        .show(getActivity().getSupportFragmentManager(), "info");
  }

  @Override
  public void showErrorEditNewsDialog() {
    UIDialogNavigation.showWarningDialog(R.string.set_edit_success)
        .show(getActivity().getSupportFragmentManager(), "info");
  }

  @Override
  public void showSuccessAddCommentDialog() {
    UIDialogNavigation.showWarningDialog(R.string.set_add_comment_success)
        .show(getActivity().getSupportFragmentManager(), "info");
  }

  @Override
  public void showErrorAddCommentDialog() {
  }

  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    switch (resultCode) {
      case BlobUploadService.STATUS_RUNNING:
        break;
      case BlobUploadService.STATUS_FINISHED:
        String result = resultData.getString("result");
        mUploadImagePath = result;
        Log.i(TAG, "Get new path to image: " + mUploadImagePath);
        invokeEditNewsCommand();
        break;
      case BlobUploadService.STATUS_ERROR:
        String error = resultData.getString(Intent.EXTRA_TEXT);
        break;
    }
  }

  @Override
  public void addCommentToBd(Bundle data) {
    int postId = data.getInt("post_id");
    String body = data.getString("body");
    int creatorID = getCreatorId();
    Uri commentUri = CommentEntry.buildCommentUri(postId);
    Vector<ContentValues> cVTagsVector = new Vector<ContentValues>(1);
    ContentValues previewValues = new ContentValues();
    previewValues.put(CommentEntry.COLUMN_POST_ID, postId);
    previewValues.put(FeedContract.CommentEntry.COLUMN_CREATOR_KEY, creatorID);
    previewValues.put(CommentEntry.COLUMN_BODY, body);
    previewValues.put(CommentEntry.COLUMN_PARENT_COMMENT_ID, 0);
    previewValues.put(CommentEntry.COLUMN_CREATE_DATE, Utils.getCurrentTime());
    cVTagsVector.add(previewValues);
    if (cVTagsVector.size() > 0) {
      ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
      cVTagsVector.toArray(cvArray);
      App.instance().getContentResolver()
          .bulkInsert(FeedContract.CommentEntry.CONTENT_URI, cvArray);
    }
    refreshItemDetailsFragmentLoader();
  }

  private int getCreatorId() {

    if (mFullNameView != null) {
      String author = mFullNameView.getText().toString();
      Uri uriCreatorAuthor = FeedContract.CreatorEntry.buildGetIdCreatorByAuthor(
          author);
      Log.i(TAG, "Get Creator ID for Author: " + author);
      if (author != null && author.trim().length() > 0) {
        Cursor byAuthorCreatorCursor = App.instance().getApplicationContext().getContentResolver()
            .query(
                uriCreatorAuthor,
                null,
                null,
                null,
                null
            );
        if (byAuthorCreatorCursor.moveToFirst()) {

          int creatorId = byAuthorCreatorCursor.getInt(FeedNewsFragment.COL_CREATOR_UNIC_ID);
          Log.i(TAG, "Get Creator ID for Author: " + author + " [creator_id]: " + creatorId);
          return creatorId;
        }
      }
    }
    return 0;
  }
}