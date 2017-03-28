package com.dbbest.amateurfeed.app.fcm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.UpdateDeviceInfoCommand;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.ui.activity.HomeActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements
    CommandResultReceiver.CommandListener {

  private static final String TAG = "MyFirebaseIIDService";
  private int SENT_DEVICE_INFO_TO_SERVER = 0;
  private CommandResultReceiver mResultReceiver;

  /**
   * Called if InstanceID token is updated. This may occur if the security of
   * the previous token had been compromised. Note that this is called when the InstanceID token
   * is initially generated so this is where you would retrieve the token.
   */
  @Override
  public void onTokenRefresh() {

    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);
    FirebaseMessaging.getInstance().subscribeToTopic("newsTopic");
    sendRegistrationToServer(refreshedToken);

  }

  /**
   * Persist token to third-party servers.
   *
   * Modify this method to associate the user's FCM InstanceID token with any server-side account
   * maintained by your application.
   *
   * @param token The new token.
   */
  private void sendRegistrationToServer(String token) {
    AuthToken authToken = new AuthToken();
    authToken.updateFcmToken(token);
    Command command = new UpdateDeviceInfoCommand(authToken.getDeviceOs(), authToken.getFcmToken(),
        authToken.getDeviceID());
    command.send(SENT_DEVICE_INFO_TO_SERVER, mResultReceiver);
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (code == SENT_DEVICE_INFO_TO_SERVER) {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      sharedPreferences.edit().putBoolean(HomeActivity.SENT_TOKEN_TO_SERVER, true).apply();
      Log.d(TAG, "Device information sent to app server successfully");
    }

  }

  @Override
  public void onFail(int code, Bundle data) {
    if (code == SENT_DEVICE_INFO_TO_SERVER) {
      Log.d(TAG, "Fail sent Device information to app server");
    }
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {

  }
}
