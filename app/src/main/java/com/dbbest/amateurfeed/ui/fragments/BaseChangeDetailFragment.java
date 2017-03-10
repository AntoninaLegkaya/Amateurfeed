package com.dbbest.amateurfeed.ui.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.BuildConfig;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.HorizontalListAdapter;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.presenter.DetailPresenter;
import com.dbbest.amateurfeed.utils.UtilImagePreferences;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.DetailView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

public class BaseChangeDetailFragment extends Fragment implements DetailView {


    public final static String DETAIL_FRAGMENT = "DetailFragment";
    public final static String DETAIL_FRAGMENT_IMAGE = "DetailFragmentI_image";
    public final static String DETAIL_FRAGMENT_COMMENT = "DetailFragmentI_comment";

    private static final int PHOTO_REQUEST_CAMERA = 0;//camera
    private static final int PHOTO_REQUEST_GALLERY = 1;//gallery
    private static final int PHOTO_REQUEST_CUT = 2;//image crop
    protected String userChosenTask;
    protected Uri mUriImage;
    public ImageView mImageView;
    protected DetailPresenter mPresenter;
    protected Uri mUriPreview;
    protected TextView mDescriptionView;
    protected TextView mTitleView;
    protected String mUploadUrl;


    @Override
    public void addTagToItemDetail(Bundle data) {

    }

    @Override
    public void updateDetailsFields(Bundle data) {

    }

    @Override
    public void showSuccessEditNewsDialog() {

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
    public void refreshFeedNews(Bundle data) {

    }

    public interface Callback {

        public void onLikeItemSelected(Uri uri, int isLike, int count);

        public void onCommentItemSelected(Uri uri);

        public void onEditItemSelected(Uri uri);

        public void onDeleteItemSelected(Uri uri);

        public void moveToFeedFragment();

        public void refreshFeed();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DetailPresenter();

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);


    }


    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detachView();

    }

    protected void selectImage() {
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

}
