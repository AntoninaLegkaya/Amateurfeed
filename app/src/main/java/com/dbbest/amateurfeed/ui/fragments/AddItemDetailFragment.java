package com.dbbest.amateurfeed.ui.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.dbbest.amateurfeed.app.azur.task.BlobUploadTask;
import com.dbbest.amateurfeed.app.storage.processor.UserPreferences;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.DetailView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by antonina on 24.01.17.
 */

public class AddItemDetailFragment extends BaseChangeDetailFragment implements DetailView, BlobUploadTask.UploadCallback, View.OnClickListener {
    private static final String PARAM_KEY = "param_key";
    public final static String ADD_DETAIL_FRAGMENT = "AddDetailFragment";
    private Button mPublishButton;
    private List<TagModel> tags = new ArrayList<>();
    private String upTitle = null;
    private String upDescription = null;

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
        String upTitle = null;
        String upDescription = null;

        switch (item.getItemId()) {
            case android.R.id.home:

                ((Callback) getActivity()).moveToFeedFragment();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_detail, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mImageView = (ImageButton) view.findViewById(R.id.add_image_item);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        mPublishButton = (Button) view.findViewById(R.id.publish_item_button);
        mPublishButton.setOnClickListener(this);
        mDescriptionView = (AppCompatEditText) view.findViewById(R.id.item_description);
        mTitleView = (AppCompatEditText) view.findViewById(R.id.item_title);

        return view;
    }


    @Override
    public void getUploadUrl(String url) {
        mUploadUrl = url;
        invokeAddNewsCommand();
    }

    private void invokeAddNewsCommand() {


        if (mTitleView != null) {
            upTitle = mTitleView.getText().toString();
        }

        if (mDescriptionView != null) {

            upDescription = mDescriptionView.getText().toString();

        }


        if (upTitle != null && upDescription != null && tags != null && mUploadUrl != null) {
            mPresenter.addNewNews(upTitle, upDescription, mUploadUrl, tags);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.publish_item_button) {

            String textDescription = mDescriptionView.getText().toString();
            if (textDescription != null) {
                String[] tags = Utils.getTagsPattern(textDescription);
                if (tags != null) {
                    for (String tag : tags) {
                        mPresenter.checkTag(tag);
                    }
                }
            }

            if (mUriImage != null) {
                BlobUploadTask uploadTask = new BlobUploadTask(getContext(), mUriImage, this);
                uploadTask.execute();

            } else {
                invokeAddNewsCommand();
            }

        }
    }

    @Override
    public void addTagToItemDetail(Bundle data) {
        TagModel tagModel = data.getParcelable("tagModel");
        tags.add(tagModel);

    }

    private void insertTagsInBd() {
        if (!tags.isEmpty()) {

            for (TagModel tagModel : tags) {
                Vector<ContentValues> cVTagsVector = new Vector<ContentValues>(1);
                Log.i(ADD_DETAIL_FRAGMENT, "You try add new tag: " + tagModel.getName());
                ContentValues tagValues = new ContentValues();
                tagValues.put(FeedContract.TagEntry.COLUMN_TAG_ID, tagModel.getId());
                tagValues.put(FeedContract.TagEntry.COLUMN_NAME, tagModel.getName());
                tagValues.put(FeedContract.TagEntry.COLUMN_PREVIEW_ID, FeedContract.PreviewEntry.getIdFromUri(mUriPreview));
                cVTagsVector.add(tagValues);

                Log.i(ADD_DETAIL_FRAGMENT, "Add tag from Description to BD (tag table): " + "id: " + tagModel.getId() + " " + "name: " + tagModel.getName() + " " +
                        "preview_id: " + FeedContract.PreviewEntry.getIdFromUri(mUriPreview));

                if (cVTagsVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
                    cVTagsVector.toArray(cvArray);
                    App.instance().getContentResolver().bulkInsert(FeedContract.TagEntry.CONTENT_URI, cvArray);
                }
            }
        }

        showSuccessEditNewsDialog();
        ((Callback) getActivity()).refreshFeed();
        ((Callback) getActivity()).moveToFeedFragment();


    }

    @Override
    public void refreshFeedNews(Bundle data) {
        int newsId = data.getInt("newsId");
        mUriPreview = FeedContract.PreviewEntry.buildPreviewUriById(newsId);
        Vector<ContentValues> cVTagsVector = new Vector<ContentValues>(1);
        Log.i(ADD_DETAIL_FRAGMENT, "You try add new preview by ID: " + newsId);
        ContentValues previewValues = new ContentValues();
        previewValues.put(FeedContract.PreviewEntry._ID, newsId);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_TITLE, upTitle);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_TEXT, upDescription);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_LIKES, 0);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_LIKE, 0);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR, UserPreferences.getFullName());
        previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE, UserPreferences.getImage());
        String createDate = Utils.getCurrentTime();
        Log.i(ADD_DETAIL_FRAGMENT, "New created : " + createDate);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_CREATE_DATE, createDate);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_IMAGE, mUploadUrl);
        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_MY, 1);
        cVTagsVector.add(previewValues);


        if (cVTagsVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
            cVTagsVector.toArray(cvArray);
            App.instance().getContentResolver().bulkInsert(FeedContract.PreviewEntry.CONTENT_URI, cvArray);
        }

        insertTagsInBd();
    }

    @Override
    public void updateDetailsFields(Bundle data) {

    }

    @Override
    public void showSuccessEditNewsDialog() {
        UIDialogNavigation.showWarningDialog(R.string.add_news_success).show(getActivity().getSupportFragmentManager(), "info");
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


}
