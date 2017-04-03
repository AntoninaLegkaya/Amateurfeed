package com.dbbest.amateurfeed.ui.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class EditProfileFragment extends EditFragment implements EditProfileView,
    GeocodeResultReceiver.Receiver,
    BlobUploadResultReceiver.Receiver,
    OnMapReadyCallback {

  private static final String TAG = EditProfileFragment.class.getName();
  private EditProfilePresenter presenter;
  private EditText userName;
  private EditText userEmail;
  private EditText userPhone;
  private TextView userLocation;

  private GoogleMap map;


  public EditProfileFragment() {
    setHasOptionsMenu(true);
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

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    SupportMapFragment mapFragment = SupportMapFragment.newInstance();
    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.layout_map_container, mapFragment, "map");
    fragmentTransaction.commit();
    mapFragment.getMapAsync(this);

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new EditProfilePresenter();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.attachView(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.detachView();
  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
    Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setDisplayHomeAsUpEnabled(true);
      supportActionBar.setDisplayShowTitleEnabled(false);
    }
    userName = (EditText) rootView.findViewById(R.id.text_profile_user_name);
    userEmail = (EditText) rootView.findViewById(R.id.text_profile_user_email);
    userPhone = (EditText) rootView.findViewById(R.id.text_profile_user_phone);
    userLocation = (TextView) rootView.findViewById(R.id.profile_user_location);
    imageView = (ImageButton) rootView.findViewById(R.id.button_get_profile_image);
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        selectImage();
      }
    });

    ImageButton clearName = (ImageButton) rootView.findViewById(R.id.button_clear_name);
    clearName.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        userName.setText("");
      }
    });

    ImageButton clearEmail = (ImageButton) rootView.findViewById(R.id.button_clear_email);
    clearEmail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        userEmail.setText("");
      }
    });
    ImageButton clearPhone = (ImageButton) rootView.findViewById(R.id.button_clear_phone);
    clearPhone.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        userPhone.setText("");
      }
    });

    UserPreferences userPreferences = new UserPreferences();
    if (userName != null) {
      userName.setText(userPreferences.getFullName());
    }
    if (userEmail != null) {
      userEmail.setText(userPreferences.getEmail());
    }
    if (userPhone != null) {
      userPhone.setText(userPreferences.getPhone());
    }
    if (imageView != null) {
      String imagePath = userPreferences.getImage();
      if (imagePath != null) {
        uploadImagePath = imagePath;
        Glide.with(App.instance().getApplicationContext())
            .load(imagePath)
            .error(R.drawable.art_snow)
            .crossFade()
            .into(imageView);
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
    presenter.onPermissionsRequestResult(requestCode, grantResults);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    if (map != null) {
      Toast.makeText(App.instance().getApplicationContext(), "Wait, Map is ready....",
          Toast.LENGTH_SHORT).show();
      map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
      if (ContextCompat.checkSelfPermission(getActivity(),
          Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {
        map.setMyLocationEnabled(true);
      }
      map.getUiSettings().setCompassEnabled(true);
      map.getUiSettings().setZoomControlsEnabled(true);
      map.getUiSettings().setMyLocationButtonEnabled(true);
      map.getUiSettings().setRotateGesturesEnabled(true);
      map.getUiSettings().setTiltGesturesEnabled(true);

      LatLng update = LocationPreferences.readLastLocation(App.instance().getApplicationContext());
      Log.i(TAG, "Init user address: " + update.latitude + " : " + update.longitude);
      map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
          getGeocodeAddress(latLng);
        }
      });
    }
  }

  @Override
  public void getGeocodeAddress(LatLng latLng) {
    GeocodeResultReceiver receiver = new GeocodeResultReceiver(new Handler());
    receiver.setReceiver(this);
    Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
        GeocodeService.class);
    intent.putExtra("receiver", receiver);
    intent.putExtra("lat", latLng.latitude);
    intent.putExtra("lon", latLng.longitude);
    getActivity().startService(intent);
  }

  @Override
  public void refreshFeed() {
    UserPreferences userPreferences = new UserPreferences();
    Uri uriPreviewAuthor = com.dbbest.amateurfeed.data.PreviewEntry
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
    if (authorPreviewCursor != null) {
      if (authorPreviewCursor.moveToFirst()) {
        do {
          long id = authorPreviewCursor.getLong(FeedNewsFragment.COL_FEED_ID);
          Log.i(TAG,
              "Update  author name for [_ID]: " + id + "Image path: " + userPreferences.getImage());
          updateColumnAuthorImageInBd(id, userPreferences.getImage(), userPreferences.getFullName());
        } while (authorPreviewCursor.moveToNext());
      }
    }
    if (authorPreviewCursor != null) {
      authorPreviewCursor.close();
    }
  }

  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    switch (resultCode) {
      case GeocodeService.STATUS_RUNNING:
        break;
      case GeocodeService.STATUS_FINISHED:
        String addressText = resultData.getString("result");
        if (userLocation != null && addressText != null) {
          userLocation.setText(addressText);
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
        uploadImagePath = resultData.getString("result");
        Log.i(TAG, "Get new path to image: " + uploadImagePath);
        update();
        break;
      case BlobUploadService.STATUS_ERROR:
        String errorBlob = resultData.getString(Intent.EXTRA_TEXT);
        Log.i(TAG, "Could not get  path to image: " + errorBlob);
        break;
    }
  }

  public void checkUpdateImage() {
    if (uriImageSelected != null) {
      Log.i(TAG, "You selected new image ");
      BlobUploadResultReceiver blobReceiver = new BlobUploadResultReceiver(new Handler());
      blobReceiver.setReceiver(this);
      Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(),
          BlobUploadService.class);
      intent.putExtra("receiver", blobReceiver);
      intent.putExtra("uri", uriImageSelected);
      getActivity().startService(intent);
    } else {
      if (uploadImagePath != null) {
        Log.i(TAG, "Get old path to image: " + uploadImagePath);
      }
      update();
    }
  }

  public void updateColumnAuthorImageInBd(long id, String imagePath, String authorName) {
    ContentValues values = new ContentValues();
    values.put(com.dbbest.amateurfeed.data.PreviewEntry.COLUMN_AUTHOR_IMAGE, imagePath);
    values.put(com.dbbest.amateurfeed.data.PreviewEntry.COLUMN_AUTHOR, authorName);
    if (imagePath != null) {
      Uri uriPreviewId = com.dbbest.amateurfeed.data.PreviewEntry.buildSetAuthorImageInPreviewUriById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  private void update() {

    String name = userName.getText().toString().trim();
    String email = userEmail.getText().toString();
    String phone = userPhone.getText().toString();

    if (!name.equals("")) {
      if (Utils.isEmailValid(email)) {
        if (Utils.isPhoneValid(phone)) {
          presenter.updateUserProfile(name, email, uploadImagePath, phone, "");
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

  private void moveToLocation(LatLng latLng, String address, String country,
      final boolean moveCamera) {
    if (latLng == null) {
      return;
    }
    if (map != null && moveCamera) {
      map.clear();
      map.addMarker(new MarkerOptions()
          .position(latLng)
          .title(country != null ? country : "")
          .snippet(address != null ? address : "")
          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
    map.moveCamera(
        CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 11.0f)));
  }

  public interface Callback {
    void moveToProfileFragment();
  }
}
