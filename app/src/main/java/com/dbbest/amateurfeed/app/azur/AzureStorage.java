package com.dbbest.amateurfeed.app.azur;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.dbbest.amateurfeed.app.azur.exception.OperationException;
import com.dbbest.amateurfeed.utils.Utils;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;

import static com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment.DETAIL_FRAGMENT_IMAGE;

public class AzureStorage extends Storage {


    private CloudBlobContainer container;


    public AzureStorage(Context ctx) throws OperationException {
        super(ctx);

        // set from prefs
        String acct_name = prefs.getAccountName();
        String access_key = prefs.getAccessKey();

        // get connection string
        String storageConn = "DefaultEndpointsProtocol=http;" +
                "AccountName=" + acct_name +
                ";AccountKey=" + access_key;

        // get CloudBlobContainer
        try {
            // Retrieve storage account from storageConn
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConn);

            // Create the blob client
            // to get reference objects for containers and blobs
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container
            container = blobClient.getContainerReference(prefs.getContainer());
            container.createIfNotExists();
        } catch (Exception e) {
            throw new OperationException("Error from initBlob: " + e.getMessage(), e);
        }
    }


    @Override
    public String uploadToStorage(Uri file_path) throws OperationException {

        try {
            // Create or overwrite blob with contents from a local file
            // use same name than in local storage
            CloudBlockBlob blob = container.getBlockBlobReference(
                    Utils.getNameFromPath(file_path));
            File source = new File(file_path.getPath());
            blob.upload(new FileInputStream(source), source.length());
            blob.getProperties().setContentType(Utils.getImageMimeType(file_path.getPath()));
            blob.uploadProperties();
            Log.i(DETAIL_FRAGMENT_IMAGE, "Upload image:  " + blob.getName());
            return blob.getName();
        } catch (Exception e) {
            throw new OperationException("Error from uploadToStorage: " + e.getMessage(), e);
        }
    }


}