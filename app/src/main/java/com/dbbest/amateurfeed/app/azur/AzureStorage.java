package com.dbbest.amateurfeed.app.azur;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.dbbest.amateurfeed.utils.Utils;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.File;
import java.io.FileInputStream;

public class AzureStorage extends Storage {


  private CloudBlobContainer container;
  private String TAG = AzureStorage.class.getName();


  public AzureStorage(Context ctx) {
    super(ctx);

    String acct_name = prefs.getAccountName();
    String access_key = prefs.getAccessKey();

    String storageConn = "DefaultEndpointsProtocol=http;" +
        "AccountName=" + acct_name +
        ";AccountKey=" + access_key;

    try {
      CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConn);

      CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

      container = blobClient.getContainerReference(prefs.getContainer());
      container.createIfNotExists();
    } catch (Exception e) {
      Log.e(TAG, "Error from initBlob: " + e.getMessage());
    }
  }


  @Override
  public String uploadToStorage(Uri file_path) {

    try {
      CloudBlockBlob blob = container.getBlockBlobReference(
          Utils.getNameFromPath(file_path));
      File source = new File(file_path.getPath());
      blob.upload(new FileInputStream(source), source.length());
      blob.getProperties().setContentType(Utils.getImageMimeType(file_path.getPath()));
      blob.uploadProperties();
      return blob.getName();
    } catch (Exception e) {
      Log.e(TAG, "Error from uploadToStorage: " + e.getMessage(), e);
    }
    return null;
  }


}