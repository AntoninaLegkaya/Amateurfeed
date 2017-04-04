package com.dbbest.amateurfeed.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class AmateurfeedAuthenticatorService extends Service {

  // Instance field that stores the authenticator object
  private AmateurfeedAuthenticator authenticator;

  @Override
  public void onCreate() {
    // Create a new authenticator object
    authenticator = new AmateurfeedAuthenticator(this);
  }

  /*
   * When the system binds to this Service to make the RPC call
   * return the authenticator's IBinder.
   */
  @Override
  public IBinder onBind(Intent intent) {
    return authenticator.getIBinder();
  }
}
