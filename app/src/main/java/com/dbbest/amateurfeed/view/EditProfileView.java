package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;


public interface EditProfileView extends IView {

  void requestPermission(int code, @NonNull String... permissions);

  void showSuccessEditProfileDialog();

  void showErrorEditProfileDialog();

  void showErrorValidEmailDialog();

  void showErrorValidPhoneDialog();

  void showErrorValidNameDialog();

  void getGeocodeAddress(LatLng latLng);

  void refreshFeed();
}
