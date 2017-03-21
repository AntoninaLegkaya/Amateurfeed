package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedContract.UserNewsEntry;
import com.dbbest.amateurfeed.data.adapter.ItemNewsAdapter;
import com.dbbest.amateurfeed.data.adapter.ItemNewsAdapter.ShowItemDetailsCallback;
import com.dbbest.amateurfeed.data.adapter.ItemNewsAdapter.UserNewsHolder;
import com.dbbest.amateurfeed.model.UserNewsModel;
import com.dbbest.amateurfeed.presenter.ProfilePresenter;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.preferences.UserPreferences;
import com.dbbest.amateurfeed.view.ProfileView;
import java.util.ArrayList;

public class ProfileFragment extends Fragment implements ProfileView,
    LoaderManager.LoaderCallbacks<Cursor> {


  public static final int COL_MY_NEWS_ID = 0;
  public static final int COL_MY_NEWS_TITLE = 1;
  public static final int COL_MY_NEWS_UPDATE_DATE = 2;
  public static final int COL_MY_NEWS_STATUS = 3;
  public static final int COL_MY_NEWS_IMAGE = 4;
  public static final int COL_MY_NEWS_LIKES = 5;
  private static final String PARAM_KEY = "param_key";

  private final int LOAD_MY_NEWS = 0;
  private String TAG = ProfileFragment.class.getName();
  private ProfilePresenter mPresenter;
  private RecyclerView mRecyclerView;
  private ImageButton mSettingsBtn;
  private ImageView mProfileImage;
  private TextView mProfileName;
  private ArrayList<String> stringArrayList;
  private ItemNewsAdapter itemNewsAdapter;
  private FloatingActionButton mFloatingActionButton;

  public static ProfileFragment newInstance(String key) {
    ProfileFragment fragment = new ProfileFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PARAM_KEY, key);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new ProfilePresenter();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

  }

  @Override
  public void onStart() {
    super.onStart();
    mPresenter.attachView(this);
    mPresenter.getNews();

  }

  @Override
  public void onStop() {
    super.onStop();
    mPresenter.detachView();
  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

    mProfileImage = (ImageView) rootView.findViewById(R.id.user_icon);
    mProfileName = (TextView) rootView.findViewById(R.id.user_name);
    UserPreferences userPreferences = new UserPreferences();
    String name = userPreferences.getFullName();
    String imagePath = userPreferences.getImage();

    if (mProfileImage != null) {
      Glide.with(App.instance().getApplicationContext())
          .load(imagePath)
          .error(R.drawable.art_snow)
          .crossFade()
          .into(mProfileImage);
    }
    if (mProfileName != null && name != null) {
      mProfileName.setText(name);
    }
    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_feed_list_view);
    mSettingsBtn = (ImageButton) rootView.findViewById(R.id.settings_button);
    mSettingsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (view.getId() == R.id.settings_button) {
          ((ProfileShowDetails) getActivity()).showPreferencesFragment();
        }
      }
    });
    mFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_edit_profile);

    mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ((ProfileShowDetails) getActivity()).showEditProfileFragment();
      }
    });

    mRecyclerView.setHasFixedSize(true);

    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
    mRecyclerView.setLayoutManager(layoutManager);
    itemNewsAdapter = new ItemNewsAdapter(null, 0, new ShowItemDetailsCallback() {
      @Override
      public void showUserNewsDetailFragment(UserNewsHolder vh, int id) {
        ((ShowItemDetailsCallback) getActivity()).showUserNewsDetailFragment(vh, id);
      }
    });
    mRecyclerView.setAdapter(itemNewsAdapter);
    return rootView;
  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    Uri previewListUri = FeedContract.UserNewsEntry.CONTENT_URI;
    String sortOrder = UserNewsEntry.COLUMN_UPDATE_DATE + " DESC";
    return new CursorLoader(getActivity(),
        previewListUri,
        FeedContract.UserNewsEntry.NEWS_COLUMNS,
        null,
        null,
        sortOrder
    );
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (loader.getId() == LOAD_MY_NEWS) {
      Log.i(TAG, " Loading finished");
      if (data.moveToFirst()) {
        Log.i(TAG, " Loading finished, data not Null");
        itemNewsAdapter.swapCursor(data);
      }
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    Log.i(TAG, "Reset Loader");
    itemNewsAdapter.swapCursor(null);
  }

  @Override
  public void initLoader(Bundle data) {
    ArrayList<UserNewsModel> list = data.getParcelableArrayList("list");
    if (!list.isEmpty()) {
      getLoaderManager().initLoader(LOAD_MY_NEWS, data, this);
    } else {
      showEmptySearchDialog();
    }
  }

  @Override
  public void showEmptySearchDialog() {
    UIDialogNavigation.showWarningDialog(R.string.search_error)
        .show(getActivity().getSupportFragmentManager(), "warn");
  }

  public interface ProfileShowDetails {

    void showEditProfileFragment();

    void showPreferencesFragment();

    void showChangePasswordFragment();
  }

}
