package com.dbbest.amateurfeed.presenter;

import android.Manifest;
import android.common.framework.Presenter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.UpdateUserProfileCommand;
import com.dbbest.amateurfeed.app.net.command.UserProfileCommand;
import com.dbbest.amateurfeed.utils.location.UserLocationProvider;
import com.dbbest.amateurfeed.view.EditProfileView;
import com.google.android.gms.maps.model.LatLng;

public class EditProfilePresenter extends Presenter<EditProfileView> implements
    UserLocationProvider.LocationProviderListener,
    CommandResultReceiver.CommandListener {

  private static final int PERMISSION_LOCATION = 1;
  private static final int CODE_UPDATE_PROFILE = 2;
  private static final int CODE_USER_PREF = 3;
  private static final String TAG = EditProfilePresenter.class.getName();
  private CommandResultReceiver resultReceiver;
  private UserLocationProvider locationProvider;

  @Override
  protected void onAttachView(@NonNull EditProfileView view) {
    if (resultReceiver == null) {
      resultReceiver = new CommandResultReceiver();
    }
    resultReceiver.setListener(this);
    if (locationProvider == null) {
      locationProvider = new UserLocationProvider(view.getContext());
    }

    locationProvider.setListener(this);
    locationProvider.update();
  }

  @Override
  protected void onDetachView(@NonNull EditProfileView view) {
    if (resultReceiver != null) {
      resultReceiver.setListener(null);
    }
    if (locationProvider != null) {
      locationProvider.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
      if (code == CODE_UPDATE_PROFILE) {
        UserProfileCommand userProfileCommand = new UserProfileCommand();
        userProfileCommand.send(CODE_USER_PREF, resultReceiver);
      }
      if (code == CODE_USER_PREF) {
        getView().refreshFeed();
        getView().showSuccessEditProfileDialog();
      }
    }
  }

  @Override
  public void onFail(int code, Bundle data) {
    if (getView() != null) {
      if (code == CODE_UPDATE_PROFILE) {
        if (getView() != null) {
          getView().showErrorEditProfileDialog();
        }
      }
    }
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {

  }

  @Override
  public void onUserLocationUpdated(LatLng location) {
    if (getView() != null) {
      getView().getGeocodeAddress(location);
    }
  }

  @Override
  public void onLocationPermissionsDenied() {
    if (getView() != null) {
      Log.i(TAG, "Location permission denied");
      getView().requestPermission(PERMISSION_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION);
    }
  }

  public void updateUserProfile(String userName, String email, String image, String phone,
      String job) {

    Command command = new UpdateUserProfileCommand(userName, email, image, phone, job);
    command.send(CODE_UPDATE_PROFILE, resultReceiver);
  }

  public void onPermissionsRequestResult(int requestCode, @NonNull int[] grantResults) {
    if (requestCode == PERMISSION_LOCATION) {
      boolean isLocationPermissionGranted = true;
      if (grantResults.length > 0) {
        for (int permissionResult : grantResults) {
          if (permissionResult == PackageManager.PERMISSION_DENIED) {
            isLocationPermissionGranted = false;
            break;
          }
        }
      }
      if (isLocationPermissionGranted) {
        Log.i(TAG, "Location permission granted");
        locationProvider.update();
      }
    }
  }
}
