package com.dbbest.amateurfeed.app.azur.service;

import static com.dbbest.amateurfeed.ui.fragments.BaseChangeDetailFragment.DETAIL_FRAGMENT_IMAGE;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dbbest.amateurfeed.app.azur.AzureStorage;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;

public class BlobUploadService extends IntentService {

  public static final int STATUS_RUNNING = 0;
  public static final int STATUS_FINISHED = 1;
  public static final int STATUS_ERROR = 2;
  private static final String TAG = "BlobUploadService";
  private AzureStorage mAzureStorage;
  private Uri mFilePath;
  private Context mContext;


  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public BlobUploadService() {
    super(BlobUploadService.class.getName());
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {

    String path = null;
    Bundle bundle = new Bundle();
    final ResultReceiver receiver = intent.getParcelableExtra("receiver");
    mFilePath=intent.getParcelableExtra("uri");
    try {
      mAzureStorage = new AzureStorage(mContext);
      String nameFile = mAzureStorage.uploadToStorage(mFilePath);
      CloudPreferences preferences = new CloudPreferences();
      path = preferences.getStorageUrl() + preferences.getContainer() + "/" + nameFile;

      if (null != path) {
        Log.i(DETAIL_FRAGMENT_IMAGE, "Upload url: " + path);
        bundle.putString("result", path);
        receiver.send(STATUS_FINISHED, bundle);
      }

    } catch (Exception e) {
      bundle.putString(Intent.EXTRA_TEXT, e.toString());
      receiver.send(STATUS_ERROR, bundle);
    }


  }
}
