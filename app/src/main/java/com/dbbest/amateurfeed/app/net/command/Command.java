package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;

/**
 * Created by antonina on 19.01.17.
 */

public abstract class Command implements Parcelable {

    private int mCode = -1;
    private CommandResultReceiver mResultReceiver;

    private static final String KEY_ERR_CODE = "ERR_CODE";
    private static final String KEY_ERR_TEXT = "ERR_TEXT";

    public Command() {

    }

    protected Command(Parcel in) {
        mCode = in.readInt();
        mResultReceiver = in.readParcelable(CommandResultReceiver.class.getClassLoader());
    }

    public void send(int code, CommandResultReceiver resultReceiver) {
        mCode = code;
        mResultReceiver = resultReceiver;
        selfExecute();
    }

    /**
     * Send self command to concrete service
     */
    protected void selfExecute() {
        ExecutionService.send(this);
    }

    /**
     * send a message through receiver to ui thread
     *
     * @param bundle data, if you have no data put {@link Bundle#EMPTY}
     */
    protected void notifyError(@NonNull Bundle bundle) {
        if (mResultReceiver != null) {
            mResultReceiver.sendFail(mCode, bundle);
        }
    }


    /**
     * send a message through receiver to ui thread
     *
     * @param bundle data, if you have no data put {@link Bundle#EMPTY}
     */
    protected void notifySuccess(@NonNull Bundle bundle) {
        if (mResultReceiver != null) {
            mResultReceiver.sendSuccess(mCode, bundle);
        }
    }


    /**
     * send a message through receiver to ui thread
     *
     * @param progress progress to publish
     * @param bundle   data, if you have no data put {@link Bundle#EMPTY}
     */
    protected void notifyProgress(int progress, @NonNull Bundle bundle) {
        if (mResultReceiver != null) {
            mResultReceiver.sendProgress(mCode, bundle, progress);
        }
    }


    protected void putErrorCode(int errorCode, @NonNull Bundle bundle) {
        bundle.putInt(KEY_ERR_CODE, errorCode);
    }

    protected void putErrorText(String errorText, @NonNull Bundle bundle) {
        bundle.putString(KEY_ERR_TEXT, errorText);
    }


    @NonNull
    protected Bundle getStatusBundle(@NonNull ResponseWrapper<?> wrapper) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ERR_CODE, wrapper.code());
        bundle.putString(KEY_ERR_TEXT, wrapper.message());
        return bundle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCode);
        dest.writeParcelable(mResultReceiver, flags);
        writeToParcel(flags, dest);
    }

    public abstract void writeToParcel(int flags, Parcel dest);

    public abstract void execute();

    /**
     * If no error code specified -1 will be return
     *
     * @param bundle bundle to grab error code
     * @return error code
     */
    public static int grabErrorCode(@NonNull Bundle bundle) {
        return bundle.getInt(KEY_ERR_CODE, -1);
    }


    /**
     * Returns error message. If no message specified returns null value
     *
     * @param bundle bundle to grab error text
     * @return error code
     */
    @Nullable
    public static String grabErrorText(@NonNull Bundle bundle) {
        return bundle.getString(KEY_ERR_TEXT, null);
    }

}