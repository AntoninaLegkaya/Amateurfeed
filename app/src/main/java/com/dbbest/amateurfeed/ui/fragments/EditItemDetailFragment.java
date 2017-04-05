package com.dbbest.amateurfeed.ui.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
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
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadResultReceiver;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadResultReceiver.Receiver;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadService;
import com.dbbest.amateurfeed.data.CommentEntry;
import com.dbbest.amateurfeed.data.CreatorEntry;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.TagEntry;
import com.dbbest.amateurfeed.data.adapter.CommentsAdapter;
import com.dbbest.amateurfeed.data.adapter.TagAdapter;
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
  public static final String TAG_MODEL = "tagModel";
  private static final String PARAM_KEY = "param_key";
  private static final int DETAIL_NEWS_LOADER = 1;
  public ImageView iconView;
  public TextView fullNameView;
  public TextView dateView;
  public TextView likesCountView;
  public TextView commentCountView;
  public TextView commentView;
  public ImageButton likeButton;
  public Button commentButton;
  public ImageButton editButton;
  public ImageButton removeButton;
  private int layoutType;
  private CommentsAdapter commentsAdapter;
  private TagAdapter tagAdapter;

  public static EditItemDetailFragment newInstance(String key) {
    EditItemDetailFragment fragment = new EditItemDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PARAM_KEY, key);
    fragment.setArguments(bundle);
    return fragment;
  }

  public EditItemDetailFragment() {
    setHasOptionsMenu(true);
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
    switch (item.getItemId()) {
      case android.R.id.home:
        ((Callback) getActivity()).moveToFeedFragment();
        return true;
      case R.id.action: {
        if (layoutType == R.layout.fragment_item_edit_user_detail) {
          String textDescription = descriptionView.getText().toString();
          String[] tags = Utils.getTagsPattern(textDescription);
          for (String tag : tags) {
            presenter.checkTag(tag);
          }
        }
        return true;
      }
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(getActivity(),
        uriPreview,
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
            .into(iconView);
        String fullName =
            data.getString(FeedNewsFragment.COL_AUTHOR);
        fullNameView.setText(fullName);
        String description = data.getString(FeedNewsFragment.COL_TEXT);
        if (description != null) {
          descriptionView.setText(description);
        }
        String title =
            data.getString(FeedNewsFragment.COL_TITLE);
        titleView.setText(title);
        String date =
            data.getString(FeedNewsFragment.COL_CREATE_DATE);
        String day;
        if (date != null) {
          day = Utils.getFriendlyDayString(getActivity(), Utils.getLongFromString(date), true);
          if (day == null) {
            dateView.setText(date);
          } else {
            dateView.setText(day);
          }
        }
        int countLikes =
            data.getInt(FeedNewsFragment.COL_LIKES);
        likesCountView.setText(String.valueOf(countLikes));
//        SimpleTarget target = new SimpleTarget<Bitmap>() {
//          @Override
//          public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//            imageView.setImageBitmap(bitmap);
//          }
//        };
        uploadImagePath = data.getString(FeedNewsFragment.COL_IMAGE);
        Glide.with(this)
            .load(uploadImagePath)
            .asBitmap()
            .error(R.drawable.art_snow)
            .into(imageView);
        int mIsLike = data.getInt(FeedNewsFragment.COL_IS_LIKE);
        if (mIsLike == 1) {
          likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
          likeButton.setTag("1");
        } else if (mIsLike == 0) {
          likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
          likeButton.setTag("0");
        }
        Cursor mCursorComments = getCommentCursor(mIdPreview);
        if (mCursorComments.moveToFirst()) {
          commentsAdapter.swapCursor(mCursorComments);
        }
        int count = mCursorComments.getCount();
        commentCountView.setText(String.valueOf(count));
        Cursor mCursorTags = getTagCursor(mIdPreview);
        if (mCursorTags.moveToFirst()) {
          tagAdapter.swapCursor(mCursorTags);
        }
      }
    }

  }

  @Override
  public void addTagToItemDetail(Bundle bundle) {
    TagModel tagModel = bundle.getParcelable(TAG_MODEL);
    ArrayList<TagModel> tags;
    tags = getTags(PreviewEntry.getIdFromUri(uriPreview));
    if (!bundle.isEmpty() && tagModel != null) {

      Vector<ContentValues> cVTagsVector = new Vector<>(1);
      boolean flag = true;
      for (TagModel model : tags) {
        if (tagModel.getName().equals(model.getName())) {
          flag = false;
        }
      }
      if (flag) {
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagEntry.COLUMN_TAG_ID, tagModel.getId());
        tagValues.put(TagEntry.COLUMN_NAME, tagModel.getName());
        tagValues.put(TagEntry.COLUMN_PREVIEW_ID,
            PreviewEntry.getIdFromUri(uriPreview));
        cVTagsVector.add(tagValues);
        if (cVTagsVector.size() > 0) {
          ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
          cVTagsVector.toArray(cvArray);
          App.instance().getContentResolver()
              .bulkInsert(TagEntry.CONTENT_URI, cvArray);
          Log.i(TAG, "Add new tag: " + tagModel.getName());
        }
      }
      Cursor newTagCursor = getTagCursor(PreviewEntry.getIdFromUri(uriPreview));
      Log.i(TAG, "Swap cursor new tags ");
      tagAdapter.swapCursor(newTagCursor);
    }
    checkUpdateImage();

  }


  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.button_add_comment) {
      if (uriPreview != null && commentView != null && commentView.getText() != null) {
        Log.i(TAG, "Start invoke Add Comment Command: new body: " + commentView.getText());
        int postId = (int) PreviewEntry.getIdFromUri(uriPreview);
        presenter.postComment(postId, commentView.getText().toString(), null);
      }
    }
    if (view.getId() == R.id.button_edit_item_news) {
      ((Callback) getActivity()).onEditItemSelected(uriPreview);
    }
    if (view.getId() == R.id.text_change_image_link) {
      selectImage();
    }
    if (view.getId() == R.id.button_delete_item_news) {
      ((Callback) getActivity()).onDeleteItemSelected(uriPreview);
    }
    if (view.getId() == R.id.button_like) {
      int mCountIsLikes;
      int isLikeFlag = 0;
      String mCountLikes = likesCountView.getText().toString();
      mCountIsLikes = Integer.parseInt(mCountLikes);
      if (likeButton.getTag() == "1") {
        isLikeFlag = 0;
        likeButton.setTag("0");
        likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        mCountIsLikes = mCountIsLikes - 1;
      } else if (likeButton.getTag() == "0") {
        isLikeFlag = 1;
        likeButton.setTag("1");
        likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        mCountIsLikes = mCountIsLikes + 1;
      }
      likesCountView.setText(String.valueOf(mCountIsLikes));
      ((Callback) getActivity()).onLikeItemSelected(uriPreview, isLikeFlag, mCountIsLikes);
    }
  }

  @Override
  public void updateDetailsFields(Bundle data) {
    NewsUpdateModel mNewsUpdateModel = data.getParcelable("model");
    updateImageUrlColumnPreview(mNewsUpdateModel != null ? mNewsUpdateModel.getImage() : null);
    updateTitleColumnPreview(mNewsUpdateModel != null ? mNewsUpdateModel.getTitle() : null);
    updateDescriptionColumnPreview(mNewsUpdateModel != null ? mNewsUpdateModel.getText() : null);
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
    commentView.setText("");
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
        uploadImagePath = resultData.getString("result");
        Log.i(TAG, "Get new path to image: " + uploadImagePath);
        invokeEditNewsCommand();
        break;
      case BlobUploadService.STATUS_ERROR:
        resultData.getString(Intent.EXTRA_TEXT);
        break;
    }
  }

  @Override
  public void addCommentToBd(Bundle data) {
    int postId = data.getInt("post_id");
    String body = data.getString("body");
    int creatorID = getCreatorId();
    Vector<ContentValues> cVTagsVector = new Vector<>(1);
    ContentValues previewValues = new ContentValues();
    previewValues.put(CommentEntry.COLUMN_POST_ID, postId);
    previewValues.put(CommentEntry.COLUMN_CREATOR_KEY, creatorID);
    previewValues.put(CommentEntry.COLUMN_BODY, body);
    previewValues.put(CommentEntry.COLUMN_PARENT_COMMENT_ID, 0);
    previewValues.put(CommentEntry.COLUMN_CREATE_DATE, Utils.getCurrentTime());
    cVTagsVector.add(previewValues);
    if (cVTagsVector.size() > 0) {
      ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
      cVTagsVector.toArray(cvArray);
      App.instance().getContentResolver()
          .bulkInsert(CommentEntry.CONTENT_URI, cvArray);
    }
    refreshItemDetailsFragmentLoader();
  }

//  public ArrayList<CommentModel> getComments(long mIdPreview) {
//    Cursor cursor = getCommentCursor(mIdPreview);
//    ArrayList<CommentModel> commentModels = new ArrayList<CommentModel>();
//    if (cursor != null) {
//      if (cursor.moveToFirst()) {
//        do {
//          CommentModel commentModel = new CommentModel(
//              cursor.getInt(FeedNewsFragment.COL_COMMENT_UNIC_ID),
//              cursor.getInt(FeedNewsFragment.COL_COMMENT_POST_ID),
//              cursor.getInt(FeedNewsFragment.COL_COMMENT_CREATOR_KEY),
//              cursor.getString(FeedNewsFragment.COL_COMMENT_BODY),
//              cursor.getInt(FeedNewsFragment.COL_COMMENT_PARENT_COMMENT_ID),
//              cursor.getString(FeedNewsFragment.COL_COMMENT_CREATE_DATE));
//          commentModels.add(commentModel);
//        } while (cursor.moveToNext());
//      }
//      cursor.close();
//    }
//    return commentModels;
//  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    Bundle arguments = getArguments();
    if (arguments != null) {
      uriPreview = arguments.getParcelable(EditItemDetailFragment.DETAIL_URI);
      layoutType = arguments.getInt(EditItemDetailFragment.DETAIL_TYPE);
    }
    View itemView = inflater.inflate(layoutType, container, false);

    Toolbar toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }

    iconView = (ImageView) itemView.findViewById(R.id.image_author_icon);
    imageView = (ImageView) itemView.findViewById(R.id.image_news_photo);
    dateView = (TextView) itemView.findViewById(R.id.text_item_date);
    fullNameView = (TextView) itemView.findViewById(R.id.text_author_name);
    titleView = (TextView) itemView.findViewById(R.id.text_edit_title_news);
    likesCountView = (TextView) itemView.findViewById(R.id.text_count_likes);
    commentCountView = (TextView) itemView.findViewById(R.id.text_comment_count);
    descriptionView = (TextView) itemView.findViewById(R.id.text_description);
    commentView = (TextView) itemView.findViewById(R.id.item_comment_text);

    likeButton = (ImageButton) itemView.findViewById(R.id.button_like);
    likeButton.setOnClickListener(this);
    commentButton = (Button) itemView.findViewById(R.id.button_add_comment);
    commentButton.setOnClickListener(this);

    TextView changeIconLink = (TextView) itemView.findViewById(R.id.text_change_image_link);
    if (changeIconLink != null) {
      changeIconLink.setOnClickListener(this);
    }

    editButton = (ImageButton) itemView.findViewById(R.id.button_edit_item_news);
    if (editButton != null) {
      editButton.setOnClickListener(this);
    }

    removeButton = (ImageButton) itemView.findViewById(R.id.button_delete_item_news);
    removeButton.setOnClickListener(this);

    RecyclerView horizontalList = (RecyclerView) itemView.findViewById(R.id.view_tags_list);
    horizontalList.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    tagAdapter = new TagAdapter();
    horizontalList.setAdapter(tagAdapter);
    RecyclerView commentList = (RecyclerView) itemView.findViewById(R.id.view_comments_list);
    commentList.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    commentsAdapter = new CommentsAdapter(null, 0);
    commentList.setAdapter(commentsAdapter);

    return itemView;
  }

  public void checkUpdateImage() {
    if (uriImageSelected != null) {
      Log.i(TAG, "You selected new image ");
      BlobUploadResultReceiver receiver = new BlobUploadResultReceiver(new Handler());
      receiver.setReceiver(this);
      Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
          BlobUploadService.class);
      intent.putExtra("receiver", receiver);
      intent.putExtra("uri", uriImageSelected);
      getActivity().startService(intent);
    } else {
      if (uploadImagePath != null) {
        Log.i(TAG, "Get old path to image: " + uploadImagePath);
      }
      invokeEditNewsCommand();
    }
  }

  public ArrayList<TagModel> getTags(long mIdPreview) {
    Cursor cursor;
    cursor = getTagCursor(mIdPreview);
    ArrayList<TagModel> tagModels = new ArrayList<>();
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

  public void refreshItemDetailsFragmentLoader() {
    if (getLoaderManager().getLoader(DETAIL_NEWS_LOADER) != null) {
      getLoaderManager().restartLoader(DETAIL_NEWS_LOADER, null, this);
    }
  }

  protected Cursor getTagCursor(long mIdPreview) {
    Uri uriTagsList = TagEntry.getTagsListById(mIdPreview);
    return App.instance().getContentResolver().query(
        uriTagsList,
        null,
        null,
        null,
        null
    );
  }

  private Cursor getCommentCursor(long mIdPreview) {
    Uri uriCommentList = CommentEntry.getCommentsListById(mIdPreview);
    String sortOrder = PreviewEntry.COLUMN_CREATE_DATE + " DESC";
    return App.instance().getContentResolver().query(
        uriCommentList,
        null,
        null,
        null,
        sortOrder
    );
  }

  private void invokeEditNewsCommand() {

    Log.i(TAG, "Invoke edit news Command ");
    String upTitle = null;
    String upDescription = null;
    long idPreview = PreviewEntry.getIdFromUri(uriPreview);
    ArrayList<TagModel> newTagsArray = getTags(idPreview);
    if (titleView != null) {
      upTitle = titleView.getText().toString();
    }
    if (descriptionView != null) {
      upDescription = descriptionView.getText().toString();
    }
    if (upTitle != null && upDescription != null && newTagsArray != null) {
      presenter
          .updateNews(newTagsArray, upTitle, upDescription, uploadImagePath, (int) idPreview);
    }
  }

  private void updateDescriptionColumnPreview(String textDescription) {
    ContentValues values = new ContentValues();
    values.put(PreviewEntry.COLUMN_TEXT, textDescription);
    if (uriPreview != null) {
      long id = PreviewEntry.getIdFromUri(uriPreview);
      Uri uriPreviewId = PreviewEntry.buildSetDescriptionInPreviewById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  private void updateTitleColumnPreview(String textTitle) {
    ContentValues values = new ContentValues();
    values.put(PreviewEntry.COLUMN_TITLE, textTitle);
    if (uriPreview != null) {
      long id = PreviewEntry.getIdFromUri(uriPreview);
      Uri uriPreviewId = PreviewEntry.buildSetTitleInPreviewById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  private void updateImageUrlColumnPreview(String url) {
    ContentValues values = new ContentValues();
    values.put(PreviewEntry.COLUMN_IMAGE, url);
    if (uriPreview != null) {
      long id = PreviewEntry.getIdFromUri(uriPreview);
      Uri uriPreviewId = PreviewEntry.buildSetImageUrlInPreviewById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  private int getCreatorId() {

    if (fullNameView != null) {
      String author = fullNameView.getText().toString();
      Uri uriCreatorAuthor = CreatorEntry.buildGetIdCreatorByAuthor(author);
      Log.i(TAG, "Get Creator ID for Author: " + author);
      if (author.trim().length() > 0) {
        Cursor byAuthorCreatorCursor = App.instance().getApplicationContext().getContentResolver()
            .query(
                uriCreatorAuthor,
                null,
                null,
                null,
                null
            );
        if (byAuthorCreatorCursor != null) {
          if (byAuthorCreatorCursor.moveToFirst()) {
            int creatorId = byAuthorCreatorCursor.getInt(FeedNewsFragment.COL_CREATOR_UNIC_ID);
            Log.i(TAG, "Get Creator ID for Author: " + author + " [creator_id]: " + creatorId);
            byAuthorCreatorCursor.close();
            return creatorId;
          }
        }
        if (byAuthorCreatorCursor != null) {
          byAuthorCreatorCursor.close();
        }
      }
    }
    return 0;
  }
}