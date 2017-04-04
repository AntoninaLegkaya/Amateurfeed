package com.dbbest.amateurfeed.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.ImageView;
import com.dbbest.amateurfeed.BuildConfig;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.utils.preferences.ImagePreferences;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class EditFragment extends Fragment {

  public final static String TAG_PARENT = EditFragment.class.getName();
  private static final int PHOTO_REQUEST_CAMERA = 0;
  private static final int PHOTO_REQUEST_GALLERY = 1;
  private static final int PHOTO_REQUEST_CUT = 2;
  public ImageView imageView;
  protected String userChosenTask;
  protected String uploadImagePath;
  protected Uri uriImageSelected;

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    String PHOTO_FILE_NAME = new ImagePreferences().getValue();
    File path;
    if (Utils.externalMemoryAvailable()) {
      path = Environment.getExternalStorageDirectory();
    } else {
      path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
          "/Image");
      Log.i(TAG_PARENT, "no storage device ");
    }
    File dir = new File(path, "/Image");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    switch (requestCode) {
      case PHOTO_REQUEST_GALLERY:
        if (data != null) {
          String realPathFromURI = getRealPathFromURI(data.getData());
          if (realPathFromURI != null) {
            File sourceFile = new File(realPathFromURI);
            File destFile = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);
            Log.i(TAG_PARENT, "File path: " + data.getData().getPath());
            try {
              Utils.copyFile(sourceFile, destFile);
              Uri photoURI = FileProvider.getUriForFile(getContext(),
                  BuildConfig.APPLICATION_ID + ".provider",
                  destFile);
              Log.i(TAG_PARENT, "Crop image from Gallery by Url : " + photoURI);
              crop(photoURI);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        break;
      case PHOTO_REQUEST_CAMERA:
        File photoFile = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);
        Uri photoURI = FileProvider.getUriForFile(getContext(),
            BuildConfig.APPLICATION_ID + ".provider",
            photoFile);
        crop(photoURI);

        break;
      case PHOTO_REQUEST_CUT:
        File file = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);
        Log.i(TAG_PARENT, "Cut image  by Path : " + file.getPath());
        uriImageSelected = Uri.fromFile(file);
        Log.i(TAG_PARENT, "Cut image  by Uri : " + uriImageSelected);
        InputStream ims = null;
        try {
          ims = new FileInputStream(file);
        } catch (FileNotFoundException e) {
          Log.e(TAG_PARENT, "a error happened when cut picture data: " + e.getMessage());
        }
        Bitmap bm = BitmapFactory.decodeStream(ims);
        imageView.setImageBitmap(bm);
        break;
      default:
        break;
    }
  }

  public void gallery() {
    String PHOTO_FILE_NAME = UUID.randomUUID().toString() + ".jpg";
    new ImagePreferences().putValue(PHOTO_FILE_NAME);
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
  }

  public void camera() {
    File path;
    if (Utils.externalMemoryAvailable()) {
      path = Environment.getExternalStorageDirectory();
    } else {
      path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
          "/Image");
      Log.i(TAG_PARENT, "no storage device ");
    }
    String PHOTO_FILE_NAME = UUID.randomUUID().toString() + ".jpg";
    new ImagePreferences().putValue(PHOTO_FILE_NAME);
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    File dir = new File(path, "/Image");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    Log.i(TAG_PARENT, "Get directory absolute Path: " + dir.getAbsolutePath());
    File photoFile = new File(dir.getAbsolutePath(), PHOTO_FILE_NAME);
    Uri photoURI = FileProvider.getUriForFile(getContext(),
        BuildConfig.APPLICATION_ID + ".provider",
        photoFile);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
    startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
  }

  public void crop(Uri uri) {
    getContext().grantUriPermission("com.android.camera", uri,
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
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

  public String getRealPathFromURI(Uri contentURI) {
    Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
    if (cursor == null) {
      return contentURI.getPath();
    } else {
      cursor.moveToFirst();
      int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
      String cursorString = cursor.getString(idx);
      cursor.close();
      return cursorString;
    }
  }

  protected void selectImage() {
    final CharSequence[] items = {"Take Photo from Camera", "Choose from Gallery",
        "Cancel"};
    AlertDialog.Builder builder = new AlertDialog.Builder(
        new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int item) {
        boolean result = Utils.checkPermission(getContext());
        if (items[item].equals("Take Photo from Camera")) {
          userChosenTask = "Take Photo from Camera";
          if (result) {
            camera();
          }
        } else if (items[item].equals("Choose from Gallery")) {
          userChosenTask = "Choose from Gallery";
          if (result) {
            gallery();
          }
        } else if (items[item].equals("Cancel")) {
          dialog.dismiss();
        }
      }
    });

    builder.show();
  }

}
