package com.dbbest.amateurfeed.utils.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.bumptech.glide.util.Util;
import com.dbbest.amateurfeed.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


public class UserLocationProvider implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

  private static String TAG = UserLocationProvider.class.getName();
  private GoogleApiClient mGoogleApiClient;
  private Context mContext;
  private LocationProviderListener mListener;

  public UserLocationProvider(Context context) {
    mContext = context;
    mGoogleApiClient = new GoogleApiClient.Builder(mContext)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  public void setListener(LocationProviderListener listener) {
    mListener = listener;
  }

  public void update() {
    Log.i(TAG, "Location updated");
    mGoogleApiClient.connect();
  }


  public LatLng getLastLocation() {
    LatLng location = LocationPreferences.readLastLocation(mContext);
    if (!Utils.isValid(location)) {
      update();
    }
    return location;
  }


  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Log.i(TAG, "on Connected");
    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

      Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
      processLocation(location);
    } else if (mListener != null) {
      mListener.onLocationPermissionsDenied();
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
  }

  @Override
  public void onLocationChanged(Location location) {
    processLocation(location);
  }

  private void processLocation(Location location) {
    Log.i(TAG, "processLocation");
    LatLng latLng = grabLocation(location);
    if (latLng == null) {
      requestLocation();
    } else {
      saveLocation(grabLocation(location));
      mGoogleApiClient.disconnect();
    }
  }

  private LatLng grabLocation(@Nullable Location location) {
    LatLng latLng = null;
    if (location != null) {
//      Log.i(TAG,
//          " Grab Location Latitude: " + location.getLatitude() + "\n" + "Longitude: " + location
//              .getLongitude());
      latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }
    return latLng;
  }

  private void requestLocation() {
    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setNumUpdates(1);
    locationRequest.setInterval(1);//5000
    locationRequest.setFastestInterval(1);//1000
    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
      LocationServices.FusedLocationApi
          .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    } else if (mListener != null) {
      mListener.onLocationPermissionsDenied();
    }
  }

  private void saveLocation(LatLng location) {
    Log.i(TAG, "Save Location");
    LocationPreferences.saveLastLocation(mContext, location);
    if (mListener != null) {
      mListener.onUserLocationUpdated(location);
    }
    // TODO: send new user location

  }


  public interface LocationProviderListener {

    void onUserLocationUpdated(LatLng location);

    void onLocationPermissionsDenied();
  }
}
