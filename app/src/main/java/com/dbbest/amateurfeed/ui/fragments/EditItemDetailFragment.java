package com.dbbest.amateurfeed.ui.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.BuildConfig;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.azur.task.BlobUploadTask;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.HorizontalListAdapter;
import com.dbbest.amateurfeed.data.adapter.VerticalListAdapter;
import com.dbbest.amateurfeed.model.CommentModel;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.dbbest.amateurfeed.presenter.DetailPresenter;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.UtilImagePreferences;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.DetailView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by antonina on 24.01.17.
 */

public class EditItemDetailFragment extends Fragment implements DetailView, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, BlobUploadTask.UploadCallback {
    public final static String DETAIL_FRAGMENT = "DetailFragment";
    public final static String DETAIL_FRAGMENT_IMAGE = "DetailFragmentI_image";
    private static final String PARAM_KEY = "param_key";
    public static final String DETAIL_URI = "URI";
    public static final String DETAIL_TYPE = "TYPE_ITEM";

    public static int RESULT_LOAD_IMAGE = 1;
    //    static final int REQUEST_IMAGE_CAPTURE = 1;
//    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int PHOTO_REQUEST_CAMERA = 0;//camera
    private static final int PHOTO_REQUEST_GALLERY = 1;//gallery
    private static final int PHOTO_REQUEST_CUT = 2;//image crop


    private String userChosenTask;

    private static final int DETAIL_NEWS_LOADER = 1;
    private int mLayoutType;
    private Uri mUriPreview;
    private TextView mChangeIconLink;
    private DetailPresenter mPresenter;
    private Uri mUriImage;
    private String mUploadUrl;
    private String mCurrentPhotoPath;


    public ImageView mIconView;
    public TextView mFullNameView;
    public TextView mTitleView;
    public TextView mDateView;
    public ImageView mImageView;
    public TextView mLikesCountView;
    public TextView mCommentCountView;
    public TextView mDescriptionView;
    public ImageButton mLikeButton;
    public Button mCommentButton;
    public ImageButton mEditButton;
    public ImageButton mRemoveButton;

    private RecyclerView mHorizontalList;
    private RecyclerView mCommentList;
    private VerticalListAdapter mVerticalListAdapter;
    private HorizontalListAdapter mHorizontalListAdapter;

    private int mLikeImage = R.drawable.ic_favorite_black_24dp;
    private int mDisLikeImage = R.drawable.ic_favorite_border_black_24dp;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DetailPresenter();

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
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detachView();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_back_menu, menu);
    }

    private void finishCreatingMenu(Menu menu) {
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action);
        menuItem.setIntent(createSaveIntent());
    }

    private Intent createSaveIntent() {
        Intent saveIntent = new Intent(Intent.ACTION_SEND);
        return saveIntent;
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

                        if (mUriImage != null) {
                            BlobUploadTask uploadTask = new BlobUploadTask(getContext(), mUriImage, this);
                            uploadTask.execute();

                        } else {
                            invokeEditNewsCommand();
                        }


                    }
                }

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
        mHorizontalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mHorizontalListAdapter = new HorizontalListAdapter(getActivity());
        mHorizontalList.setAdapter(mHorizontalListAdapter);


        mCommentList = (RecyclerView) itemView.findViewById(R.id.comment_recycler_view);
        mCommentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mVerticalListAdapter = new VerticalListAdapter(getActivity());
        mCommentList.setAdapter(mVerticalListAdapter);


        return itemView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.i(DETAIL_FRAGMENT, "Query to  preview table get item for Details fragment by  uri: " + mUriPreview);
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
                mFullNameView.setText(fullName + String.valueOf(mIdPreview));

                String description = data.getString(FeedNewsFragment.COL_TEXT);
                if (description != null) {
                    mDescriptionView.setText(description);

                }


                String title =
                        data.getString(FeedNewsFragment.COL_TITLTE);
                mTitleView.setText(title);

                String date =
                        data.getString(FeedNewsFragment.COL_CREATE_DATE);
                String day = null;

                day = Utils.getFriendlyDayString(getActivity(), Utils.getLongFromString(date), true);

                if (day == null) {
                    mDateView.setText(date);
                } else {
                    mDateView.setText(day);
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

                mUploadUrl = data.getString(FeedNewsFragment.COL_IMAGE);
                Glide.with(this)
                        .load(mUploadUrl)
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
                    mVerticalListAdapter.swapCursor(mCursorComments);
                }

                int count = mCursorComments.getCount();
                mCommentCountView.setText(String.valueOf(count));

                Cursor mCursorTags = getTagCursor(mIdPreview);

                if (mCursorTags.moveToFirst()) {
                    mHorizontalListAdapter.swapCursor(mCursorTags);
                }


            }
        }

    }



    private Cursor getCommentCursor(long mIdPreview) {
        Uri uriCommentList = FeedContract.CommentEntry.getCommentsListById(mIdPreview);
// Sort order:  Ascending, by date.
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
//                    Log.i(DETAIL_FRAGMENT, "Compose array tags: " + cursor.getString(FeedNewsFragment.COL_TAG_NAME));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return commentModels;
    }

    public ArrayList<TagModel> getTags(long mIdPreview) {
        Cursor cursor = getTagCursor(mIdPreview);
        ArrayList<TagModel> tagModels = new ArrayList<TagModel>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TagModel tagModel = new TagModel(cursor.getInt(FeedNewsFragment.COL_TAG_ID), cursor.getString(FeedNewsFragment.COL_TAG_NAME));
                    tagModels.add(tagModel);
//                    Log.i(DETAIL_FRAGMENT, "Compose array tags: " + cursor.getString(FeedNewsFragment.COL_TAG_NAME));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return tagModels;
    }

    private Cursor getTagCursor(long mIdPreview) {
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


        if (view.getId() == R.id.edit_button) {

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
            if (mCountIsLikes >= 0) {
                if (mLikeButton.getTag() == "1") {

                    isLikeFlag = 0;
                    mLikeButton.setTag("0");
                    mLikeButton.setImageResource(mDisLikeImage);
                    mCountIsLikes = mCountIsLikes - 1;


                } else if (mLikeButton.getTag() == "0") {
                    isLikeFlag = 1;
                    mLikeButton.setTag("1");
                    mLikeButton.setImageResource(mLikeImage);
                    mCountIsLikes = mCountIsLikes + 1;


                }

                mLikesCountView.setText(String.valueOf(mCountIsLikes));
                ((Callback) getActivity()).onLikeItemSelected(mUriPreview, isLikeFlag, mCountIsLikes);
            } else {
                Log.i(DETAIL_FRAGMENT, "Error in like clear All!");
                mLikesCountView.setText(String.valueOf(0));
                ((Callback) getActivity()).onLikeItemSelected(mUriPreview, 0, mCountIsLikes);
            }


        }


    }

    @Override
    public void addTagToItemDetail(Bundle bundle) {

        // Insert the tags news information into the database

        TagModel tagModel = bundle.getParcelable("tagModel");
        ArrayList<TagModel> tags = getTags(FeedContract.PreviewEntry.getIdFromUri(mUriPreview));

        if (tagModel != null) {
            Vector<ContentValues> cVTagsVector = new Vector<ContentValues>(1);

            boolean flag = true;
            for (TagModel model : tags) {


                if (tagModel.getName().equals(model.getName())) {

                    Log.i(DETAIL_FRAGMENT, "You have tag: " + tagModel.getName());
                    flag = false;

                }


            }
            if (flag) {

                Log.i(DETAIL_FRAGMENT, "You try add new tag: " + tagModel.getName());
                ContentValues tagValues = new ContentValues();
                tagValues.put(FeedContract.TagEntry.COLUMN_TAG_ID, tagModel.getId());
                tagValues.put(FeedContract.TagEntry.COLUMN_NAME, tagModel.getName());
                tagValues.put(FeedContract.TagEntry.COLUMN_PREVIEW_ID, FeedContract.PreviewEntry.getIdFromUri(mUriPreview));
                cVTagsVector.add(tagValues);

                Log.i(DETAIL_FRAGMENT, "Add tag from Description to BD (tag table): " + "id: " + tagModel.getId() + " " + "name: " + tagModel.getName() + " " +
                        "preview_id: " + FeedContract.PreviewEntry.getIdFromUri(mUriPreview));

                if (cVTagsVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVTagsVector.size()];
                    cVTagsVector.toArray(cvArray);
                    App.instance().getContentResolver().bulkInsert(FeedContract.TagEntry.CONTENT_URI, cvArray);
                    Cursor newTagCursor = getTagCursor(FeedContract.PreviewEntry.getIdFromUri(mUriPreview));
                    mHorizontalListAdapter.swapCursor(newTagCursor);

                }
            } else {
                Log.i(DETAIL_FRAGMENT, "Nothing to add");
            }


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

        UIDialogNavigation.showWarningDialog(R.string.set_edit_success).show(getActivity().getSupportFragmentManager(), "info");

    }

    @Override
    public void showErrorEditNewsDialog() {
        UIDialogNavigation.showWarningDialog(R.string.set_edit_success).show(getActivity().getSupportFragmentManager(), "info");

    }

    private void invokeEditNewsCommand() {

        String upTitle = null;
        String upDescription = null;
        long idPreview = FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
        List<TagModel> newTagsArray = getTags(idPreview);
        if (mTitleView != null) {
            upTitle = mTitleView.getText().toString();
//            updateTitleColumnPreview(upTitle);
        }

        if (mDescriptionView != null) {

            upDescription = mDescriptionView.getText().toString();
//            updateDescriptionColumnPreview(upDescription);

        }


        if (upTitle != null && upDescription != null && newTagsArray != null) {
            mPresenter.updateNews(newTagsArray, upTitle, upDescription, mUploadUrl, (int) idPreview);
        }
    }

    @Override
    public void getUploadUrl(String url) {
        mUploadUrl = url;
        invokeEditNewsCommand();
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo from Camera", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(getContext());
                if (items[item].equals("Take Photo from Camera")) {
                    userChosenTask = "Take Photo from Camera";
                    if (result)
                        camera();
                } else if (items[item].equals("Choose from Gallery")) {
                    userChosenTask = "Choose from Gallery";
                    if (result)
                        gallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String PHOTO_FILE_NAME = UtilImagePreferences.getValue();
        File path;
        if (Utils.externalMemoryAvailable()) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Image");
            Log.i(DETAIL_FRAGMENT_IMAGE, "no storage device ");
        }
        File dir = new File(path, "/Image");
        if (!dir.exists())
            dir.mkdirs();
        switch (requestCode) {
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    File sourceFile = new File(getRealPathFromURI(data.getData()));
                    File destFile = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);
                    Log.i(DETAIL_FRAGMENT_IMAGE, "File path: " + data.getData().getPath());
                    try {
                        Utils.copyFile(sourceFile, destFile);
                        if (destFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getContext(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    destFile);
                            Log.i(DETAIL_FRAGMENT_IMAGE, "Crop image from Gallery by Url : " + photoURI);
                            crop(photoURI);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_REQUEST_CAMERA:

                File photoFile = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    crop(photoURI);

                }


                break;
            case PHOTO_REQUEST_CUT:


                File file = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);
                Log.i(DETAIL_FRAGMENT_IMAGE, "Cut image  by Path : " + file.getPath());
                mUriImage = Uri.fromFile(file);
                Log.i(DETAIL_FRAGMENT_IMAGE, "Cut image  by Uri : " + mUriImage);
                InputStream ims = null;
                try {
                    ims = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    Log.e(DETAIL_FRAGMENT_IMAGE, "a error happened when cut picture data: " + e.getMessage());
                }
                Bitmap bm = BitmapFactory.decodeStream(ims);
                mImageView.setImageBitmap(bm);
                break;

            default:
                break;
        }
    }

    public void gallery() {
        //set UUID to filename
        String PHOTO_FILE_NAME = UUID.randomUUID().toString() + ".jpg";
        UtilImagePreferences.putValue(PHOTO_FILE_NAME);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void camera() {
        File path;
        if (Utils.externalMemoryAvailable()) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Image");
            Log.i(DETAIL_FRAGMENT_IMAGE, "no storage device ");
        }
        //set UUID to filename
        String PHOTO_FILE_NAME = UUID.randomUUID().toString() + ".jpg";
        UtilImagePreferences.putValue(PHOTO_FILE_NAME);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File dir = new File(path, "/Image");
        if (!dir.exists())
            dir.mkdirs();
        Log.i(DETAIL_FRAGMENT_IMAGE, "Get directory absolute Path: " + dir.getAbsolutePath());
        File photoFile = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
        }
    }

    //Android N crop image
    public void crop(Uri uri) {
        getContext().grantUriPermission("com.android.camera", uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //Android N need set permission to uri otherwise system camera don't has permission to access file wait crop
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        //The proportion of the crop box is 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //Crop the output image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        //image type
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        //true - don't return uri |  false - return uri
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    //file uri to real location in filesystem
    public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public interface Callback {


        public void onLikeItemSelected(Uri uri, int isLike, int count);

        public void onCommentItemSelected(Uri uri);

        public void onEditItemSelected(Uri uri);

        public void onDeleteItemSelected(Uri uri);

        public void moveToFeedFragment();


    }
}