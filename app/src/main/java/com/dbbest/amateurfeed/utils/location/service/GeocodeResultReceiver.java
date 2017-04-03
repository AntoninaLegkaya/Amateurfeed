package com.dbbest.amateurfeed.utils.location.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class GeocodeResultReceiver extends ResultReceiver {

  private Receiver receiver;

  /**
   * Create a new ResultReceive to receive results.  Your
   * {@link #onReceiveResult} method will be called from the thread running
   * <var>handler</var> if given, or from an arbitrary thread if null.
   */
  public GeocodeResultReceiver(Handler handler) {
    super(handler);
  }

  @Override
  protected void onReceiveResult(int resultCode, Bundle resultData) {
    if (receiver != null) {
      receiver.onReceiveResult(resultCode, resultData);
    }
  }

  public void setReceiver(Receiver r) {
    this.receiver = r;
  }

  public interface Receiver {

    void onReceiveResult(int resultCode, Bundle resultData);
  }
}
