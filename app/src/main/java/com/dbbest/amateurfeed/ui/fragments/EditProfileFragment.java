package com.dbbest.amateurfeed.ui.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadResultReceiver;
import com.dbbest.amateurfeed.app.azur.service.BlobUploadService;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedContract.PreviewEntry;
import com.dbbest.amateurfeed.presenter.EditProfilePresenter;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.utils.location.LocationPreferences;
import com.dbbest.amateurfeed.utils.location.service.GeocodeResultReceiver;
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

public class EditProfileFragment extends EditFragment implements EditProfileView,
    GeocodeResultReceiver.Receiver,
    BlobUploadResultReceiver.Receiver,
    OnMapReadyCallback {

  private static final String TAG = EditProfileFragment.class.getName();
  private static final String PARAM_KEY = "param_key";
  private EditProfilePresenter mPresenter;
  private EditText mUserName;
  private EditText mUserEmail;
  private EditText mUserPhone;
  private TextView mUserLocation;

  private SupportMapFragment mMapFragment;
  private GoogleMap mMap;
  private Marker mPlaceMarkerA;
  private GeocodeResultReceiver mReceiver;
  private BlobUploadResultReceiver mBlobReceiver;


  public EditProfileFragment() {
    setHasOptionsMenu(true);
  }

  public static EditProfileFragment newInstance(String key) {
    EditProfileFragment fragment = new EditProfileFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PARAM_KEY, key);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.action_back_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home:
        ((Callback) getActivity()).moveToProfileFragment();
        return true;
      case R.id.action: {
        checkUpdateImage();
        return true;
      }
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void update() {

    String name = mUserName.getText().toString().trim();
    String email = mUserEmail.getText().toString();
    String phone = mUserPhone.getText().toString();

    if (!name.equals("")) {
      if (Utils.isEmailValid(email)) {
        if (Utils.isPhoneValid(phone)) {
          mPresenter.updateUserProfile(name, email, mUploadImagePath, phone, "");
        } else {
          showErrorValidPhoneDialog();
        }
      } else {
        showErrorValidEmailDialog();
      }
    } else {
      showErrorValidNameDialog();
    }
  }

  public void checkUpdateImage() {
    if (mUriImageSelected != null) {
      Log.i(TAG, "You selected new image ");
      mBlobReceiver = new BlobUploadResultReceiver(new Handler());
      mBlobReceiver.setReceiver(this);
      Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
          BlobUploadService.class);
      intent.putExtra("receiver", mBlobReceiver);
      intent.putExtra("uri", mUriImageSelected);
      getActivity().startService(intent);
    } else {
      if (mUploadImagePath != null) {
        Log.i(TAG, "Get old path to image: " + mUploadImagePath);
      }
      update();
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    mMapFragment = SupportMapFragment.newInstance();
    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");
    fragmentTransaction.commit();
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
    Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    mUserName = (EditText) rootView.findViewById(R.id.profile_user_name);
    mUserEmail = (EditText) rootView.findViewById(R.id.profile_user_email);
    mUserPhone = (EditText) rootView.findViewById(R.id.profile_user_phone);
    mUserLocation = (TextView) rootView.findViewById(R.id.profile_user_location);
    mImageView = (ImageButton) rootView.findViewById(R.id.profile_image_item);
    mImageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        selectImage();
      }
    });

    ImageButton clearName = (ImageButton) rootView.findViewById(R.id.clear_name_button);
    clearName.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mUserName.setText("");
      }
    });

    ImageButton clearEmail = (ImageButton) rootView.findViewById(R.id.clear_email_button);
    clearEmail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mUserEmail.setText("");
      }
    });
    ImageButton clearPhone = (ImageButton) rootView.findViewById(R.id.clear_phone_button);
    clearPhone.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mUserPhone.setText("");
      }
    });

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
      if (mImageView != null) {
        String imagePath = userPreferences.getImage();
        if (imagePath != null) {
          mUploadImagePath = imagePath;
          Glide.with(App.instance().getApplicationContext())
              .load(imagePath)
              .error(R.drawable.art_snow)
              .crossFade()
              .into(mImageView);
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
  public void showErrorValidEmailDialog() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectEmail)
        .show(getActivity().getSupportFragmentManager(), "warn");

  }

  @Override
  public void showErrorValidPhoneDialog() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectPhone)
        .show(getActivity().getSupportFragmentManager(), "warn");

  }

  @Override
  public void showErrorValidNameDialog() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectName)
        .show(getActivity().getSupportFragmentManager(), "warn");

  }

  @Override
  public void showSuccessEditProfileDialog() {
    UIDialogNavigation.showWarningDialog(R.string.update_profile_success)
        .show(getActivity().getSupportFragmentManager(), "info");

  }

  @Override
  public void showErrorEditProfileDialog() {
    UIDialogNavigation.showWarningDialog(R.string.update_profile_error)
        .show(getActivity().getSupportFragmentManager(), "warn");
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
      Log.i(TAG, "Init user address: " + update.latitude + " : " + update.longitude);
      mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
          getGeocodeAddress(latLng);
        }
      });
    }
  }

  @Override
  public void getGeocodeAddress(LatLng latLng) {
    mReceiver = new GeocodeResultReceiver(new Handler());
    mReceiver.setReceiver(this);
    Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
        GeocodeService.class);
    intent.putExtra("receiver", mReceiver);
    intent.putExtra("lat", latLng.latitude);
    intent.putExtra("lon", latLng.longitude);
    getActivity().startService(intent);
  }

  @Override
  public void refreshFeed() {
    UserPreferences userPreferences = new UserPreferences();
    Uri uriPreviewAuthor = FeedContract.PreviewEntry
        .buildPreviewUriByAuthor(userPreferences.getFullName());
    Log.i(TAG, "Get news items by uri: " + uriPreviewAuthor.toString());
    Cursor authorPreviewCursor = App.instance().getApplicationContext().getContentResolver()
        .query(
            uriPreviewAuthor,
            null,
            null,
            null,
            null
        );
    if (authorPreviewCursor.moveToFirst()) {
      do {
        long id = authorPreviewCursor.getLong(FeedNewsFragment.COL_FEED_ID);
        Log.i(TAG,
            "Update  author name for [_ID]: " + id + "Image path: " + userPreferences.getImage());
        updateColumnAuthorImageInBd(id, userPreferences.getImage(), userPreferences.getFullName());
      } while (authorPreviewCursor.moveToNext());
    }
    authorPreviewCursor.close();
  }

  public void updateColumnAuthorImageInBd(long id, String imagePath, String authorName) {
    ContentValues values = new ContentValues();
    values.put(PreviewEntry.COLUMN_AUTHOR_IMAGE, imagePath);
    values.put(PreviewEntry.COLUMN_AUTHOR, authorName);
    if (imagePath != null) {
      Uri uriPreviewId = FeedContract.PreviewEntry.buildSetAuthorImageInPreviewUriById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
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
    mMap.moveCamera(
        CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 11.0f)));
  }


  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    switch (resultCode) {
      case GeocodeService.STATUS_RUNNING:
        break;
      case GeocodeService.STATUS_FINISHED:
        String addressText = resultData.getString("result");
        if (mUserLocation != null && addressText != null) {
          mUserLocation.setText(addressText);
        }
        String address = resultData.getString("address");
        String country = resultData.getString("country");
        LatLng latLng = new LatLng(resultData.getDouble("lat"), resultData.getDouble("lon"));
        moveToLocation(latLng, address, country, true);
        break;
      case GeocodeService.STATUS_ERROR:
        String error = resultData.getString(Intent.EXTRA_TEXT);
        Log.e(TAG, "Could not get new address: " + error);
        break;
      case BlobUploadService.STATUS_RUNNING:
        break;
      case BlobUploadService.STATUS_FINISHED:
        String result = resultData.getString("result");
        mUploadImagePath = result;
        Log.i(TAG, "Get new path to image: " + mUploadImagePath);
        update();
        break;
      case BlobUploadService.STATUS_ERROR:
        String errorBlob = resultData.getString(Intent.EXTRA_TEXT);
        Log.i(TAG, "Could not get  path to image: " + errorBlob);
        break;
    }
  }

  public interface Callback {

    void moveToProfileFragment();
  }
}
