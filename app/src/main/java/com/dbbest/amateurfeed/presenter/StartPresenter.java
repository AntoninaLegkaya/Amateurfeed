package com.dbbest.amateurfeed.presenter;

import android.Manifest;
import android.common.framework.Presenter;
import android.common.util.TextUtils;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.command.AzureCommand;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.LoginCommand;
import com.dbbest.amateurfeed.app.net.command.RegistrationFacebookCommand;
import com.dbbest.amateurfeed.app.net.command.UserProfileCommand;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.utils.location.UserLocationProvider;
import com.dbbest.amateurfeed.view.StartView;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.model.LatLng;


public class StartPresenter extends Presenter<StartView> implements
    CommandResultReceiver.CommandListener,
    UserLocationProvider.LocationProviderListener {

  private static final String INCORRECT_PASSWORD_MSG_RESPONSE = "An error has occurred.";
  private static final int PERMISSION_LOCATION = 0;
  private static final String TAG = StartPresenter.class.getName();
  private static final int CODE_LOGIN = 0;
  private static final int CODE_REGISTRATION_FB = 1;
  private static final int CODE_CLOUD_PREF = 2;
  private static final int CODE_USER_PREF = 3;

  private CommandResultReceiver resultReceiver;
  private UserLocationProvider locationProvider;

  @Override
  protected void onAttachView(@NonNull StartView view) {
    super.onAttachView(view);
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
  protected void onDetachView(@NonNull StartView view) {
    super.onDetachView(view);
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
      getView().dismissProgressDialog();
      if (code == CODE_LOGIN) {
        getCloudPreference();
        getUserPreference();
        getView().navigateToHomeScreen();
      }
    }
  }

  @Override
  public void onFail(int code, Bundle data) {
    if (getView() != null) {
      getView().dismissProgressDialog();
      int errCode = Command.grabErrorCode(data);
      String errMessage = Command.grabErrorText(data);
      if (errCode == NetworkUtil.CODE_SOCKET_TIMEOUT
          || errCode == NetworkUtil.CODE_UNABLE_TO_RESOLVE_HOST) {
        getView().showErrorConnectionDialog();
      } else {
        if (errMessage != null && errMessage.equals(INCORRECT_PASSWORD_MSG_RESPONSE)) {
          getView().showErrorIncorrectPassword();
        } else {
          getView().showErrorLoginDialog();
        }
      }
    }
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {

  }

  @Override
  public void onUserLocationUpdated(LatLng location) {

  }

  @Override
  public void onLocationPermissionsDenied() {
    if (getView() != null) {
      Log.i(TAG, "Location permission denied");
      getView().requestPermission(PERMISSION_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION);
    }
  }

  public void login(String email, String password, String deviceId, String osType,
      String deviceToken) {

    if (getView() != null) {
      StartView view = getView();

      if (TextUtils.isEmpty(email)) {
        view.showEmptyEmailError();
        return;
      } else if (!Utils.isEmailValid(email)) {
        view.showEmailValidationError();
        return;
      }

      if (TextUtils.isEmpty(password)) {
        view.showEmptyPasswordError();
        return;
      } else if (!Utils.isPasswordLengthValid(password)) {
        view.showPasswordLengthValidationError();
        return;
      } else if (!Utils.isPasswordValid(password)) {
        view.showPasswordValidationError();
        return;
      }

      view.showProgressDialog();
    }
    Command command = new LoginCommand(email, password, deviceId, osType, deviceToken);
    command.send(CODE_LOGIN, resultReceiver);
  }

  public void loginFaceBook(String token) {
    LatLng location = locationProvider.getLastLocation();
    RegistrationFacebookCommand command = new RegistrationFacebookCommand(token,
        location.longitude, location.latitude);
    LoginManager.getInstance().logOut();

    if (getView() != null) {
      getView().showProgressDialog();
    }
    command.send(CODE_REGISTRATION_FB, resultReceiver);
  }

  private void getCloudPreference() {
    AzureCommand azureCommand = new AzureCommand();

    azureCommand.send(CODE_CLOUD_PREF, resultReceiver);
  }

  private void getUserPreference() {
    UserProfileCommand userProfileCommand = new UserProfileCommand();
    userProfileCommand.send(CODE_USER_PREF, resultReceiver);

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