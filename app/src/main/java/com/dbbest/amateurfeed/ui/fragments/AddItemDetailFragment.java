package com.dbbest.amateurfeed.ui.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadResultReceiver;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadResultReceiver.Receiver;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadService;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.TagEntry;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.utils.preferences.UserPreferences;
import com.dbbest.amateurfeed.view.DetailView;
import java.util.ArrayList;
import java.util.Vector;


public class AddItemDetailFragment extends BaseEditDetailFragment implements DetailView,
    View.OnClickListener,
    Receiver {

  public static final String RESULT = "result";
  public static final String NEWS_ID = "newsId";
  public static final String URI = "uri";
  public static final String RECEIVER = "receiver";
  public static final String TAG_MODEL = "tagModel";
  private static final String PARAM_KEY = "param_key";
  private ArrayList<TagModel> tags = new ArrayList<>();
  private String upTitle;
  private String upDescription;

  public static AddItemDetailFragment newInstance(String key) {
    AddItemDetailFragment fragment = new AddItemDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PARAM_KEY, key);
    fragment.setArguments(bundle);
    return fragment;
  }

  public AddItemDetailFragment() {
    setHasOptionsMenu(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home:
        ((Callback) getActivity()).moveToFeedFragment();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_item_detail, container, false);
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
    imageView = (ImageButton) view.findViewById(R.id.button_add_image_item);
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectImage();
      }
    });

    Button publishButton = (Button) view.findViewById(R.id.button_publish_item);
    publishButton.setOnClickListener(this);
    descriptionView = (AppCompatEditText) view.findViewById(R.id.text_item_description);
    titleView = (AppCompatEditText) view.findViewById(R.id.text_item_title);
    return view;
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.button_publish_item) {
      String textDescription = descriptionView.getText().toString();
      String[] tagsPattern = Utils.getTagsPattern(textDescription);
      for (String tag : tagsPattern) {
        presenter.checkTag(tag);
      }
    }
  }

  @Override
  public void addTagToItemDetail(Bundle data) {
    TagModel tagModel = data.getParcelable(TAG_MODEL);
    tags.add(tagModel);
    checkUpdateImage();
  }

  @Override
  public void checkUpdateImage() {
    if (uriImageSelected != null) {
      BlobUploadResultReceiver receiver = new BlobUploadResultReceiver(new Handler());
      receiver.setReceiver(this);
      Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
          BlobUploadService.class);
      intent.putExtra(RECEIVER, receiver);
      intent.putExtra(URI, uriImageSelected);
      getActivity().startService(intent);
    } else {
      UIDialogNavigation.showWarningDialog(R.string.add_news_image)
          .show(getActivity().getSupportFragmentManager(), "info");
      tags.clear();
    }
  }

  @Override
  public void refreshFeedNews(Bundle data) {
    int newsId = data.getInt(NEWS_ID);
    uriPreview = PreviewEntry.buildPreviewUriById(newsId);
    Vector<ContentValues> cVTagsVector = new Vector<>(1);
    ContentValues previewValues = new ContentValues();
    previewValues.put(PreviewEntry._ID, newsId);
    previewValues.put(PreviewEntry.COLUMN_TITLE, upTitle);
    previewValues.put(PreviewEntry.COLUMN_TEXT, upDescription);
    previewValues.put(PreviewEntry.COLUMN_LIKES, 0);
    previewValues.put(PreviewEntry.COLUMN_IS_LIKE, 0);
    previewValues.put(PreviewEntry.COLUMN_AUTHOR, new UserPreferences().getFullName());
    previewValues
        .put(PreviewEntry.COLUMN_AUTHOR_IMAGE, new UserPreferences().getImage());
    String createDate = Utils.getCurrentTime();
    previewValues.put(PreviewEntry.COLUMN_CREATE_DATE, createDate);
    previewValues.put(PreviewEntry.COLUMN_IMAGE, uploadImagePath);
    previewValues.put(PreviewEntry.COLUMN_IS_MY, 1);
    cVTagsVector.add(previewValues);
    if (cVTagsVector.size() > 0) {
      ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
      cVTagsVector.toArray(cvArray);
      App.instance().getContentResolver()
          .bulkInsert(PreviewEntry.CONTENT_URI, cvArray);
    }
    insertTagsInBd();
  }

  @Override
  public void updateDetailsFields(Bundle data) {
  }

  @Override
  public void showSuccessEditNewsDialog() {
    UIDialogNavigation.showWarningDialog(R.string.add_news_success)
        .show(getActivity().getSupportFragmentManager(), "info");
  }

  @Override
  public void showErrorEditNewsDialog() {
  }

  @Override
  public void showSuccessAddCommentDialog() {
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
        uploadImagePath = resultData.getString(RESULT);
        invokeAddNewsCommand();
        break;
      case BlobUploadService.STATUS_ERROR:
        String error = resultData.getString(Intent.EXTRA_TEXT);
        Log.e(TAG, error);
        break;
    }
  }

  private void invokeAddNewsCommand() {
    if (titleView != null) {
      upTitle = titleView.getText().toString();
    }
    if (descriptionView != null) {
      upDescription = descriptionView.getText().toString();
    }
    if (upTitle != null && upDescription != null && tags != null && uploadImagePath != null) {
      presenter.addNewNews(upTitle, upDescription, uploadImagePath, tags);
    }
  }

  private void insertTagsInBd() {
    if (!tags.isEmpty()) {
      for (TagModel tagModel : tags) {
        Vector<ContentValues> cVTagsVector = new Vector<>(1);
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagEntry.COLUMN_TAG_ID, tagModel.getId());
        tagValues.put(TagEntry.COLUMN_NAME, tagModel.getName());
        tagValues.put(TagEntry.COLUMN_PREVIEW_ID,
            PreviewEntry.getIdFromUri(uriPreview));
        cVTagsVector.add(tagValues);
        Log.i(TAG,
            "Add tag from Description to BD (tag table): " + "id: " + tagModel.getId() + " "
                + "name: " + tagModel.getName() + " " +
                "preview_id: " + PreviewEntry.getIdFromUri(uriPreview));
        if (cVTagsVector.size() > 0) {
          ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
          cVTagsVector.toArray(cvArray);
          App.instance().getContentResolver()
              .bulkInsert(TagEntry.CONTENT_URI, cvArray);
        }
      }
    }
    showSuccessEditNewsDialog();
    ((Callback) getActivity()).refreshFeed();
    ((Callback) getActivity()).moveToFeedFragment();
  }
}
