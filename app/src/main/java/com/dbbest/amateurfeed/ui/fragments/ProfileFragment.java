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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.UserNewsEntry;
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
  public static final String LIST = "list";
  private final int LOAD_MY_NEWS = 0;
  private ProfilePresenter presenter;
  private ItemNewsAdapter itemNewsAdapter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new ProfilePresenter();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.attachView(this);
    presenter.getNews();

  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.detachView();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    Uri previewListUri = com.dbbest.amateurfeed.data.UserNewsEntry.CONTENT_URI;
    String sortOrder = UserNewsEntry.COLUMN_UPDATE_DATE + " DESC";
    return new CursorLoader(getActivity(),
        previewListUri,
        UserNewsEntry.NEWS_COLUMNS,
        null,
        null,
        sortOrder
    );
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (loader.getId() == LOAD_MY_NEWS) {
      if (data.moveToFirst()) {
        itemNewsAdapter.swapCursor(data);
      }
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    itemNewsAdapter.swapCursor(null);
  }

  @Override
  public void initLoader(Bundle data) {
    ArrayList<UserNewsModel> list = data.getParcelableArrayList(LIST);
    if (list != null && !list.isEmpty()) {
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

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

    ImageView profileImage = (ImageView) rootView.findViewById(R.id.image_user_icon);
    TextView profileName = (TextView) rootView.findViewById(R.id.text_user_name);
    UserPreferences userPreferences = new UserPreferences();
    String name = userPreferences.getFullName();
    String imagePath = userPreferences.getImage();

    if (profileImage != null) {
      Glide.with(App.instance().getApplicationContext())
          .load(imagePath)
          .error(R.drawable.art_snow)
          .crossFade()
          .into(profileImage);
    }
    if (profileName != null && name != null) {
      profileName.setText(name);
    }
    RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.view_my_feed_list);
    ImageButton settingsBtn = (ImageButton) rootView.findViewById(R.id.button_settings);
    settingsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (view.getId() == R.id.button_settings) {
          ((ProfileShowDetails) getActivity()).showPreferencesFragment();
        }
      }
    });
    FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_edit_profile);

    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ((ProfileShowDetails) getActivity()).showEditProfileFragment();
      }
    });

    mRecyclerView.setHasFixedSize(true);

    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
    mRecyclerView.setLayoutManager(layoutManager);
    itemNewsAdapter = new ItemNewsAdapter(null, new ShowItemDetailsCallback() {
      @Override
      public void showUserNewsDetailFragment(UserNewsHolder vh, int id) {
        ((ShowItemDetailsCallback) getActivity()).showUserNewsDetailFragment(vh, id);
      }
    });
    mRecyclerView.setAdapter(itemNewsAdapter);
    return rootView;
  }

  public interface ProfileShowDetails {

    void showEditProfileFragment();

    void showPreferencesFragment();

    void showChangePasswordFragment();
  }

}
