package com.dbbest.amateurfeed.ui.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
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
import android.util.Base64;
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
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.HorizontalListAdapter;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.presenter.DetailPresenter;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.DetailView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by antonina on 24.01.17.
 */

public class EditItemDetailFragment extends Fragment implements DetailView, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    public final static String DETAIL_FRAGMENT = "DetailFragment";
    public final static String DETAIL_FRAGMENT_IMAGE = "DetailFragmentI_image";
    private static final String PARAM_KEY = "param_key";
    public static final String DETAIL_URI = "URI";
    public static final String DETAIL_TYPE = "TYPE_ITEM";

    public static int RESULT_LOAD_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChosenTask;

    private static final int DETAIL_NEWS_LOADER = 1;
    private int mLayoutType;
    private Uri mUriPreview;
    private TextView mChangeIconLink;
    private DetailPresenter mPresenter;
    private String encImage;
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
            case R.id.action:
                if (mLayoutType == R.layout.fragment_item_edit_my_detail) {
                    String textDescription = mDescriptionView.getText().toString();
                    if (textDescription != null) {
                        String[] tags = Utils.getTagsPattern(textDescription);
                        if (tags != null) {
                            for (String tag : tags) {
                                mPresenter.checkTag(tag);
                            }
                        }

                        long idPreview = FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
                        List<TagModel> newTagsArray = getTags(idPreview);
                        if (mTitleView != null) {

                            upTitle = mTitleView.getText().toString();
                            updateTitleColumnPreview(upTitle);
                        }

                        if (mDescriptionView != null) {

                            upDescription = mDescriptionView.getText().toString();
                            updateDescriptionColumnPreview(upDescription);

                        }


                        if (upTitle != null && encImage != null && upDescription != null && newTagsArray != null) {
                            encImage = "https://webdefaulttemplate.blob.core.windows.net/images/0F6F50DA-8362-46DA-81F4-9E5B354C2718.jpeg";
                            mPresenter.updateNews(newTagsArray, upTitle, upDescription, encImage, (int) idPreview);
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
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        byte[] b = bytes.toByteArray();
                        encImage = Base64.encodeToString(b, Base64.DEFAULT);
                        Log.i(DETAIL_FRAGMENT_IMAGE, "Image Base64:  " + encImage);
                        mImageView.setImageBitmap(bitmap);
                    }
                };

                Glide.with(this)
                        .load(data.getString(FeedNewsFragment.COL_IMAGE))
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

                Uri uriCommentList = FeedContract.CommentEntry.getCommentsListById(mIdPreview);


                Cursor mCursorComments = App.instance().getContentResolver().query(
                        uriCommentList,
                        null,
                        null,
                        null,
                        null
                );
                int count = mCursorComments.getCount();
                mCommentCountView.setText(String.valueOf(count));

                Cursor mCursorTags = getTagCursor(mIdPreview);

                if (mCursorTags.moveToFirst()) {
                    mHorizontalListAdapter.swapCursor(mCursorTags);
                }


            }
        }

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
            Uri uriPreviewId = FeedContract.PreviewEntry.buildSetDescriptionInPreviewUriById(id);
            App.instance().getContentResolver().update(uriPreviewId, values, null, null);
        }
    }

    private void updateTitleColumnPreview(String textTitle) {
        ContentValues values = new ContentValues();
        values.put(FeedContract.PreviewEntry.COLUMN_TEXT, textTitle);
        if (mUriPreview != null) {

            long id = FeedContract.PreviewEntry.getIdFromUri(mUriPreview);
            Uri uriPreviewId = FeedContract.PreviewEntry.buildSetTitleInPreviewUriById(id);
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
    public void showSuccessEditNewsDialog() {

        UIDialogNavigation.showWarningDialog(R.string.set_edit_success).show(getActivity().getSupportFragmentManager(), "info");

    }

    public interface Callback {


        public void onLikeItemSelected(Uri uri, int isLike, int count);

        public void onCommentItemSelected(Uri uri);

        public void onEditItemSelected(Uri uri);

        public void onDeleteItemSelected(Uri uri);

        public void moveToFeedFragment();


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo from Camera", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
//        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(getContext());
                if (items[item].equals("Take Photo from Camera")) {
                    userChosenTask = "Take Photo from Camera";
                    if (result)
                        startCamera();
                } else if (items[item].equals("Choose from Gallery")) {
                    userChosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(DETAIL_FRAGMENT_IMAGE, " onRequestPermissionsResult: requestCode: " + requestCode);
        if (requestCode == Utils.MY_PERMISSIONS_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChosenTask.equals("Take Photo from Camera")) {
                    Log.i(DETAIL_FRAGMENT_IMAGE, " onRequestPermissionsResult: Start camera ");
                    startCamera();
                } else if (userChosenTask.equals("Choose from Gallery"))
                    galleryIntent();
            } else {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Log.i(DETAIL_FRAGMENT_IMAGE, "  onActivityResult: Show image ");
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                File file = new File(imageUri.getPath());
                try {
                    InputStream ims = new FileInputStream(file);

                    Bitmap bm = BitmapFactory.decodeStream(ims);
                    mImageView.setImageBitmap(bm);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    byte[] b = bytes.toByteArray();
                    encImage = Base64.encodeToString(b, Base64.DEFAULT);
                    Log.i(DETAIL_FRAGMENT_IMAGE, "Image Base64:  " + encImage);
                } catch (FileNotFoundException e) {
                    return;
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(getContext(),
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

        }

    }


    void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.i(DETAIL_FRAGMENT_IMAGE, "Ensure that there's a camera activity to handle the intent ");
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.i(DETAIL_FRAGMENT_IMAGE, "Create the File where the photo should go ");
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i(DETAIL_FRAGMENT_IMAGE, "Error occurred while creating the File ");
                return;
            }

            if (photoFile != null) {
                Log.i(DETAIL_FRAGMENT_IMAGE, "Continue... The File was successfully created ");
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.i(DETAIL_FRAGMENT_IMAGE, "Save a file: path for use with ACTION_VIEW intents: " + mCurrentPhotoPath);
        return image;
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mImageView.setImageBitmap(bm);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] b = bytes.toByteArray();
        encImage = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i(DETAIL_FRAGMENT_IMAGE, "Image Base64:  " + encImage);

    }
}