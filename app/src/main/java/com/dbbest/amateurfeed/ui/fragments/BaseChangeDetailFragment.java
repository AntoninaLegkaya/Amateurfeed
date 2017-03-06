package com.dbbest.amateurfeed.ui.fragments;

import android.net.Uri;
import android.support.v4.app.Fragment;

public class BaseChangeDetailFragment extends Fragment {
    public interface Callback {

        public void onLikeItemSelected(Uri uri, int isLike, int count);

        public void onCommentItemSelected(Uri uri);

        public void onEditItemSelected(Uri uri);

        public void onDeleteItemSelected(Uri uri);

        public void moveToFeedFragment();


    }
}
