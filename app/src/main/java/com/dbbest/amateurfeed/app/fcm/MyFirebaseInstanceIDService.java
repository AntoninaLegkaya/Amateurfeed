package com.dbbest.amateurfeed.app.fcm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.dbbest.amateurfeed.ui.activity.HomeActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "MyFirebaseIIDService";

  /**
   * Called if InstanceID token is updated. This may occur if the security of
   * the previous token had been compromised. Note that this is called when the InstanceID token
   * is initially generated so this is where you would retrieve the token.
   */
  @Override
  public void onTokenRefresh() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);
    FirebaseMessaging.getInstance().subscribeToTopic("newsTopic");
    sendRegistrationToServer(refreshedToken);
    sharedPreferences.edit().putBoolean(HomeActivity.SENT_TOKEN_TO_SERVER, true).apply();
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
    // TODO: Implement this method to send token to your app server.
    Log.i(TAG, "GCM Registration Token: " + token);
  }
}
