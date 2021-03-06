package com.dbbest.amateurfeed.app.azur.service;


import android.app.IntentService;
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
  private String TAG = BlobUploadService.class.getName();
  private AzureStorage azureStorage;
  private Uri filePath;

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
    filePath = intent.getParcelableExtra("uri");
    azureStorage = new AzureStorage();
    String nameFile = azureStorage.uploadToStorage(filePath, receiver);
    CloudPreferences preferences = new CloudPreferences();
    if (nameFile != null) {
      path = preferences.getStorageUrl() + preferences.getContainer() + "/" + nameFile;
    }
    if (null != path) {
      Log.i(TAG, "Upload url: " + path);
      bundle.putString("result", path);
      receiver.send(STATUS_FINISHED, bundle);
    }
  }
}
