package com.dbbest.amateurfeed.utils.location;

import android.os.Parcel;
import android.os.Parcelable;


public class LatLng implements Parcelable {

  public static final Parcelable.Creator<LatLng> CREATOR =
      new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel source) {
          return new LatLng(source);
        }

        @Override
        public LatLng[] newArray(int size) {
          return new LatLng[size];
        }
      };
  private final double mLatitude;
  private final double mLongitude;

  public LatLng(double latitude, double longitude) {
    this.mLatitude = latitude;
    this.mLongitude = longitude;
  }

  private LatLng(Parcel in) {
    mLatitude = in.readDouble();
    mLongitude = in.readDouble();
  }

  public double latitude() {
    return mLatitude;
  }

  public double longitude() {
    return mLongitude;
  }

  public boolean isValid() {
    return mLatitude != 0.0d && mLongitude != 0.0d;
  }

  @Override
  public String toString() {
    return "lat:" + mLatitude + " lon:" + mLongitude;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(mLatitude);
    dest.writeDouble(mLongitude);
  }
}