package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.os.StackedResultReceiver;


public class CommandResultReceiver extends
    StackedResultReceiver<CommandResultReceiver.CommandListener> {

  public static final Parcelable.Creator<CommandResultReceiver> CREATOR = new Parcelable.Creator<CommandResultReceiver>() {
    @Override
    public CommandResultReceiver createFromParcel(Parcel source) {
      return new CommandResultReceiver(source);
    }

    @Override
    public CommandResultReceiver[] newArray(int size) {
      return new CommandResultReceiver[size];
    }
  };
  private static final int CODE_SUCCESS = 0;
  private static final int CODE_FAIL = 1;
  private static final int CODE_PROGRESS = 2;
  private static final String KEY_PROGRESS = "PROGRESS";
  private static final String KEY_CODE = "CODE";

  public CommandResultReceiver() {

  }

  private CommandResultReceiver(Parcel in) {
    super(in);
  }

  @Override
  protected void onHandleResult(@NonNull CommandListener listener, int resultCode,
      Bundle resultData) {
    int code = resultData.getInt(KEY_CODE);
    if (resultCode == CODE_SUCCESS) {
      listener.onSuccess(code, resultData);
    } else if (resultCode == CODE_FAIL) {
      listener.onFail(code, resultData);
    } else if (resultCode == CODE_PROGRESS) {
      int progress = resultData.getInt(KEY_PROGRESS);
      listener.onProgress(code, resultData, progress);
    }
  }

  public void sendSuccess(int code, Bundle data) {
    Bundle bundle = new Bundle(data);
    bundle.putInt(KEY_CODE, code);
    send(CODE_SUCCESS, bundle);
  }

  public void sendFail(int code, Bundle data) {
    Bundle bundle = new Bundle(data);
    bundle.putInt(KEY_CODE, code);
    send(CODE_FAIL, bundle);
  }

  public void sendProgress(int code, Bundle data, int progress) {
    Bundle bundle = new Bundle(data);
    bundle.putInt(KEY_CODE, code);
    bundle.putInt(KEY_PROGRESS, progress);
    send(CODE_PROGRESS, bundle);
  }


  public interface CommandListener {

    void onSuccess(int code, Bundle data);

    void onFail(int code, Bundle data);

    void onProgress(int code, Bundle data, int progress);
  }
}