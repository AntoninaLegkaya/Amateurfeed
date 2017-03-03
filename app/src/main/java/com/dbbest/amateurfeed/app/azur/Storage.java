package com.dbbest.amateurfeed.app.azur;

import android.content.Context;
import android.net.Uri;

import com.dbbest.amateurfeed.app.azur.exception.OperationException;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;

public abstract class Storage {

    /**
     * All providers will have accesss to context
     */
    protected Context context;

    /**
     * All providers will have accesss to SharedPreferences
     */
    protected CloudPreferences prefs;

    /**
     * All downloads from providers will be saved on SD card
     */
//    protected String DOWNLOAD_PATH = "/sdcard/DCIM/Camera/";

    /**
     * @throws OperationException
     */
    public Storage(Context ctx) throws OperationException {
        context = ctx;
        prefs = new CloudPreferences();
        //DOWNLOAD_PATH = ctx.getFilesDir() + File.separator;
    }

    /**
     * @throws OperationException
     */
    public abstract String uploadToStorage(Uri file_path) throws OperationException;


}