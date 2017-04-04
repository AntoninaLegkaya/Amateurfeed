package com.dbbest.amateurfeed.app.azur.service;


import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class BlobUploadResultReceiver extends ResultReceiver {

  private Receiver receiver;

  /**
   * Create a new ResultReceive to receive results.  Your
   * {@link #onReceiveResult} method will be called from the thread running
   * <var>handler</var> if given, or from an arbitrary thread if null.
   */
  public BlobUploadResultReceiver(Handler handler) {
    super(handler);
  }

  @Override
  protected void onReceiveResult(int resultCode, Bundle resultData) {
    if (receiver != null) {
      receiver.onReceiveResult(resultCode, resultData);
    }
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  public interface Receiver {

    public void onReceiveResult(int resultCode, Bundle resultData);
  }
}
