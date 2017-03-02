package com.dbbest.amateurfeed.app.azur.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.dbbest.amateurfeed.app.azur.AzureStorage;
import com.dbbest.amateurfeed.app.azur.StorageFactory;
import com.dbbest.amateurfeed.app.azur.exception.OperationException;

import static com.dbbest.amateurfeed.app.azur.StorageFactory.getStorageInstance;

public class BlobUploadTask extends AsyncTask<String, Void, Void> {
    private AzureStorage mAzureStorage;
    private String mFilePath;

    public BlobUploadTask(Activity activity, String filePath) {
        try {
            mAzureStorage = (AzureStorage) StorageFactory.getStorageInstance(activity);
            mFilePath = filePath;
        } catch (OperationException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(String... arg0) {

        try {
            mAzureStorage.uploadToStorage(mFilePath);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
