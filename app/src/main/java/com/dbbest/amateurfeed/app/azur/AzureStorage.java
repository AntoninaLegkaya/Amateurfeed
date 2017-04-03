package com.dbbest.amateurfeed.app.azur;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadService;
import com.dbbest.amateurfeed.utils.Utils;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class AzureStorage extends Storage {

  private final String TAG = AzureStorage.class.getName();
  private CloudBlobContainer container;

  public AzureStorage() {
    super();
    String acctName = prefs.getAccountName();
    String accessKey = prefs.getAccessKey();

    String storageConn = "DefaultEndpointsProtocol=http;" +
        "AccountName=" + acctName +
        ";AccountKey=" + accessKey;

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
  public String uploadToStorage(Uri file_path, ResultReceiver receiver) {
    Bundle bundle = new Bundle();
    try {
      CloudBlockBlob blob = container.getBlockBlobReference(
          Utils.getNameFromPath(file_path));
      File source = new File(file_path.getPath());
      try {
        blob.upload(new FileInputStream(source), source.length());
      } catch (FileNotFoundException e) {
        bundle.putString(Intent.EXTRA_TEXT, e.toString());
        receiver.send(BlobUploadService.STATUS_ERROR, bundle);
      } catch (IOException e) {
        bundle.putString(Intent.EXTRA_TEXT, e.toString());
        receiver.send(BlobUploadService.STATUS_ERROR, bundle);
      }
      blob.getProperties().setContentType(Utils.getImageMimeType(file_path.getPath()));
      blob.uploadProperties();
      return blob.getName();
    } catch (URISyntaxException e) {
      bundle.putString(Intent.EXTRA_TEXT, e.toString());
      receiver.send(BlobUploadService.STATUS_ERROR, bundle);
    } catch (StorageException e) {
      bundle.putString(Intent.EXTRA_TEXT, e.toString());
      receiver.send(BlobUploadService.STATUS_ERROR, bundle);
    }
    return null;
  }


}