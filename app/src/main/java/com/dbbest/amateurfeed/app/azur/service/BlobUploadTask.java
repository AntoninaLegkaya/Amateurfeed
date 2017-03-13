package com.dbbest.amateurfeed.app.azur.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.dbbest.amateurfeed.app.azur.AzureStorage;
import com.dbbest.amateurfeed.app.azur.exception.OperationException;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;

public class BlobUploadTask extends AsyncTask<Uri, Void, String> {

  private AzureStorage mAzureStorage;
  private Uri mFilePath;
  private Context mContext;
  private UploadCallback mUploadCallback;

  public BlobUploadTask(Context context, Uri filePath, UploadCallback callback) {

    mFilePath = filePath;
    mContext = context;
    mUploadCallback = callback;
  }


  @Override
  protected String doInBackground(Uri... arg0) {
    String path = null;


    try {
      mAzureStorage = new AzureStorage(mContext);
      String nameFile = mAzureStorage.uploadToStorage(mFilePath);
      CloudPreferences preferences = new CloudPreferences();
      path = preferences.getStorageUrl() + preferences.getContainer() + "/" + nameFile;

    } catch (OperationException e) {
      e.printStackTrace();
    }
    return path;
  }

  @Override
  protected void onPostExecute(String path) {
    super.onPostExecute(path);
    mUploadCallback.getUploadUrl(path);

  }

  public interface UploadCallback {

    public void getUploadUrl(String url);

  }
}
