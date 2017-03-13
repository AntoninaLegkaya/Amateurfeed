package com.dbbest.amateurfeed.app.azur.service;


import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class BlobUploadResultReceiver extends ResultReceiver {

  private Receiver mReceiver;

  /**
   * Create a new ResultReceive to receive results.  Your
   * {@link #onReceiveResult} method will be called from the thread running
   * <var>handler</var> if given, or from an arbitrary thread if null.
   */
  public BlobUploadResultReceiver(Handler handler) {
    super(handler);
  }

  public void setReceiver(Receiver receiver) {
    mReceiver = receiver;
  }

  @Override
  protected void onReceiveResult(int resultCode, Bundle resultData) {
    if (mReceiver != null) {
      mReceiver.onReceiveResult(resultCode, resultData);
    }
  }

  public interface Receiver {

    public void onReceiveResult(int resultCode, Bundle resultData);
  }
}
