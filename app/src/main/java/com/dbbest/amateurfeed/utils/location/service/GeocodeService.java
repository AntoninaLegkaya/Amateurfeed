package com.dbbest.amateurfeed.utils.location.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodeService extends IntentService {

  public static final int STATUS_RUNNING = 0;
  public static final int STATUS_FINISHED = 1;
  public static final int STATUS_ERROR = 2;
  private Context mContext;
  private String TAG = GeocodeService.class.getName();

  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public GeocodeService() {
    super(GeocodeService.class.getName());
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    Geocoder geocoder = new Geocoder(App.instance().getApplicationContext(), Locale.getDefault());
    final ResultReceiver receiver = intent.getParcelableExtra("receiver");
    double latitude = intent.getDoubleExtra("lat", 0);
    double longitude = intent.getDoubleExtra("lon", 0);
    Bundle bundle = new Bundle();
    List<Address> addresses = null;
    String addressText = "";
    try {
      addresses = geocoder.getFromLocation(latitude, longitude, 1);
    } catch (IOException e) {
      bundle.putString(Intent.EXTRA_TEXT, e.toString());
      receiver.send(STATUS_ERROR, bundle);
    }
    if (addresses != null && addresses.size() > 0) {
      Address address = addresses.get(0);
      addressText = String.format("%s, %s",
          address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
          address.getCountryName());
      bundle.putString("address", address.getAddressLine(0));
      bundle.putString("country",  address.getCountryName());
      bundle.putDouble("lat", latitude);
      bundle.putDouble("lon", longitude);
      receiver.send(STATUS_FINISHED, bundle);
    } else {
      bundle.putString(Intent.EXTRA_TEXT, "Addresses is Empty");
      receiver.send(STATUS_ERROR, bundle);
    }
  }
}
