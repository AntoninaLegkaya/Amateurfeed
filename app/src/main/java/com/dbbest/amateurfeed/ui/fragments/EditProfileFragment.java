package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.EditProfilePresenter;
import com.dbbest.amateurfeed.utils.location.LocationPreferences;
import com.dbbest.amateurfeed.utils.location.service.GeocodeResultReceiver;
import com.dbbest.amateurfeed.utils.location.service.GeocodeResultReceiver.Receiver;
import com.dbbest.amateurfeed.utils.location.service.GeocodeService;
import com.dbbest.amateurfeed.utils.preferences.UserPreferences;
import com.dbbest.amateurfeed.view.EditProfileView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditProfileFragment extends Fragment implements EditProfileView, Receiver,
    OnMapReadyCallback {

  private static final String TAG = EditProfileFragment.class.getName();
  private static final String PARAM_KEY = "param_key";
  private EditProfilePresenter mPresenter;
  private EditText mUserName;
  private EditText mUserEmail;
  private EditText mUserPhone;
  private TextView mUserLocation;
  private ImageButton mUserImage;
  private SupportMapFragment mMapFragment;
  private GoogleMap mMap;
  private Marker mPlaceMarkerA;
  private GeocodeResultReceiver mReceiver;


  public static EditProfileFragment newInstance(String key) {
    EditProfileFragment fragment = new EditProfileFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PARAM_KEY, key);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i(TAG, "onActivityCreated: Added Map into mapContainer");
    mMapFragment = SupportMapFragment.newInstance();
    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");
    fragmentTransaction.commit();
    Log.i(TAG, "onActivityCreated: getMapAsync(this)");
    mMapFragment.getMapAsync(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new EditProfilePresenter();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onStart() {
    super.onStart();
    mPresenter.attachView(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    mPresenter.detachView();
  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
    mUserName = (EditText) rootView.findViewById(R.id.profile_user_name);
    mUserEmail = (EditText) rootView.findViewById(R.id.profile_user_email);
    mUserPhone = (EditText) rootView.findViewById(R.id.profile_user_phone);
    mUserLocation = (TextView) rootView.findViewById(R.id.profile_user_location);
    mUserImage = (ImageButton) rootView.findViewById(R.id.profile_image_item);

    UserPreferences userPreferences = new UserPreferences();
    if (userPreferences != null) {
      if (mUserName != null) {
        mUserName.setText(userPreferences.getFullName());
      }
      if (mUserEmail != null) {
        mUserEmail.setText(userPreferences.getEmail());
      }
      if (mUserPhone != null) {
        mUserPhone.setText(userPreferences.getPhone());
      }
      if (mUserImage != null) {

        String imagePath = userPreferences.getImage();

        if (imagePath != null) {
          Glide.with(App.instance().getApplicationContext())
              .load(imagePath)
              .error(R.drawable.art_snow)
              .crossFade()
              .into(mUserImage);
        }
      }
    }

    return rootView;
  }


  @Override
  public void requestPermission(int code, @NonNull String... permissions) {
    if (permissions.length > 0) {
      ActivityCompat.requestPermissions(getActivity(), permissions, code);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    mPresenter.onPermissionsRequestResult(requestCode, grantResults);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    if (mMap != null) {
      Toast.makeText(App.instance().getApplicationContext(), "Wait, Map is ready....",
          Toast.LENGTH_SHORT).show();
      mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

      mMap.setMyLocationEnabled(true);
      mMap.getUiSettings().setCompassEnabled(true);
      mMap.getUiSettings().setZoomControlsEnabled(true);
      mMap.getUiSettings().setMyLocationButtonEnabled(true);
      mMap.getUiSettings().setRotateGesturesEnabled(true);
      mMap.getUiSettings().setTiltGesturesEnabled(true);
      LatLng update = LocationPreferences.readLastLocation(App.instance().getApplicationContext());
      if (update != null) {
//        mMap.moveCamera(
//            CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(update, 11.0f)));
        getGeocodeAddress(update);
      }
      mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
          getGeocodeAddress(latLng);
        }
      });
    }
  }

  private void getGeocodeAddress(LatLng latLng) {
    mReceiver = new GeocodeResultReceiver(new Handler());
    mReceiver.setReceiver(this);
    Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
        GeocodeService.class);
    intent.putExtra("receiver", mReceiver);
    intent.putExtra("lat", latLng.latitude);
    intent.putExtra("lon", latLng.longitude);
    getActivity().startService(intent);
  }

  private void moveToLocation(LatLng latLng, String address, String country,
      final boolean moveCamera) {
    if (latLng == null) {
      return;
    }

    if (mMap != null && moveCamera) {
      mMap.clear();
      mPlaceMarkerA = mMap.addMarker(new MarkerOptions()
          .position(latLng)
          .title(country != null ? country : "")
          .snippet(address != null ? address : "")
          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
//    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.moveCamera(
        CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 11.0f)));
  }


  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    switch (resultCode) {
      case GeocodeService.STATUS_RUNNING:
        break;
      case GeocodeService.STATUS_FINISHED:
        String address = resultData.getString("address");
        String country = resultData.getString("country");
        LatLng latLng = new LatLng(resultData.getDouble("lat"), resultData.getDouble("lon"));
        moveToLocation(latLng, address, country, true);
        break;
      case GeocodeService.STATUS_ERROR:
        String error = resultData.getString(Intent.EXTRA_TEXT);
        Log.e(TAG, "Could not get new address: " + error);
        break;
    }
  }

}
