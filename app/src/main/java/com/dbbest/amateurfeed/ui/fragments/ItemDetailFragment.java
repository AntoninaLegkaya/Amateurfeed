package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.HorizontalListAdapter;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter;
import com.dbbest.amateurfeed.presenter.DetailPresenter;
import com.dbbest.amateurfeed.presenter.ProfilePresenter;
import com.dbbest.amateurfeed.ui.SignUpActivity;
import com.dbbest.amateurfeed.ui.util.UiActivityNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.DetailView;
import com.dbbest.amateurfeed.view.ProfileView;

/**
 * Created by antonina on 24.01.17.
 */

public class ItemDetailFragment extends Fragment implements DetailView, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private static final String PARAM_KEY = "param_key";
    public static final String DETAIL_URI = "URI";
    public static final String DETAIL_TYPE = "TYPE_ITEM";

    private static final int DETAIL_NEWS_LOADER = 1;
    private int mLayoutType;
    private Uri mUriPreview;
    private TextView mChangeIconLink;
    DetailPresenter mPresenter;


    public ImageView mIconView;
    public TextView mFullNameView;
    public TextView mTitleView;
    public TextView mDateView;
    public ImageView mImageView;
    public TextView mLikesCountView;
    public TextView mCommentCountView;
    public TextView mDescriptionView;
    public ImageButton mLikeButton;
    public Button mCommentButton;
    public ImageButton mEditButton;
    public ImageButton mRemoveButton;

    private RecyclerView mHorizontalList;
    private HorizontalListAdapter mHorizontalListAdapter;

    private int mLikeImage = R.drawable.ic_favorite_black_24dp;
    private int mDisLikeImage = R.drawable.ic_favorite_border_black_24dp;


    public static ItemDetailFragment newInstance(String key) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DetailPresenter();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        getLoaderManager().initLoader(DETAIL_NEWS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUriPreview = arguments.getParcelable(ItemDetailFragment.DETAIL_URI);
            mLayoutType = arguments.getInt(ItemDetailFragment.DETAIL_TYPE);
        }
        View itemView = inflater.inflate(mLayoutType, container, false);


        mIconView = (ImageView) itemView.findViewById(R.id.list_item_icon);
        mImageView = (ImageView) itemView.findViewById(R.id.list_item_image);
        mDateView = (TextView) itemView.findViewById(R.id.list_item_date_textview);
        mFullNameView = (TextView) itemView.findViewById(R.id.list_item_name_textview);
        mTitleView = (TextView) itemView.findViewById(R.id.list_item_title);
        mLikesCountView = (TextView) itemView.findViewById(R.id.list_item_likes_count);
        mCommentCountView = (TextView) itemView.findViewById(R.id.list_item_comment_count);
        mDescriptionView = (TextView) itemView.findViewById(R.id.list_item_description);

        mLikeButton = (ImageButton) itemView.findViewById(R.id.like_button);
        mLikeButton.setOnClickListener(this);
        mCommentButton = (Button) itemView.findViewById(R.id.add_comment_button);
        mCommentButton.setOnClickListener(this);


        switch (mLayoutType) {

            case PreviewAdapter.VIEW_TYPE_MY: {
                mEditButton = (ImageButton) itemView.findViewById(R.id.edit_button);
                if (mEditButton != null) {
                    mEditButton.setOnClickListener(this);
                }

                mChangeIconLink = (TextView) itemView.findViewById(R.id.change_image_link);
                if (mChangeIconLink != null) {
                    mChangeIconLink.setOnClickListener(this);
                }
                break;
            }

        }

        mRemoveButton = (ImageButton) itemView.findViewById(R.id.delete_button);
        mRemoveButton.setOnClickListener(this);


        mHorizontalList = (RecyclerView) itemView.findViewById(R.id.list_tags_view);
        mHorizontalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mHorizontalListAdapter = new HorizontalListAdapter(getActivity());
        mHorizontalList.setAdapter(mHorizontalListAdapter);


        return itemView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.i(Utils.TAG_LOG, "Query to  preview table get item for Details Screen uri: " + mUriPreview);
        return new CursorLoader(getActivity(),
                mUriPreview,
                FeedNewsFragment.PREVIEW_COLUMNS,
                null,
                null,
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == DETAIL_NEWS_LOADER) {

            if (data.moveToFirst()) {


                long mIdPreview = data.getLong(FeedNewsFragment.COL_FEED_ID);
                Glide.with(this)
                        .load(data.getString(FeedNewsFragment.COL_AUTHOR_IMAGE))
                        .error(R.drawable.art_snow)
                        .crossFade()
                        .into(mIconView);

                String fullName =
                        data.getString(FeedNewsFragment.COL_AUTHOR);
                mFullNameView.setText(fullName + String.valueOf(mIdPreview));

                String description = data.getString(FeedNewsFragment.COL_TEXT);
                if (description != null) {
                    mDescriptionView.setText(description);
                }


                String title =
                        data.getString(FeedNewsFragment.COL_TITLTE);
                mTitleView.setText(title);

                String date =
                        data.getString(FeedNewsFragment.COL_CREATE_DATE);
                String day = null;

                day = Utils.getFriendlyDayString(getActivity(), Utils.getLongFromString(date), true);

                if (day == null) {
                    mDateView.setText(date);
                } else {
                    mDateView.setText(day);
                }

                int countLikes =
                        data.getInt(FeedNewsFragment.COL_LIKES);
                mLikesCountView.setText(String.valueOf(countLikes));


                Glide.with(this)
                        .load(data.getString(FeedNewsFragment.COL_IMAGE))
                        .error(R.drawable.art_snow)
                        .crossFade()
                        .into(mImageView);
                int mIsLike = data.getInt(FeedNewsFragment.COL_IS_LIKE);
                if (mIsLike == 1) {
                    mLikeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    mLikeButton.setTag("1");

                } else if (mIsLike == 0) {

                    mLikeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    mLikeButton.setTag("0");
                }

                Uri uriCommentList = FeedContract.CommentEntry.getCommentsListById(mIdPreview);


                Cursor mCursorComments = App.instance().getContentResolver().query(
                        uriCommentList,
                        null,
                        null,
                        null,
                        null
                );
                int count = mCursorComments.getCount();
                mCommentCountView.setText(String.valueOf(count));

                Uri uriTagsList = FeedContract.TagEntry.getTagsListById(mIdPreview);

                Cursor mCursorTags = App.instance().getContentResolver().query(
                        uriTagsList,
                        null,
                        null,
                        null,
                        null
                );

                if (mCursorTags.moveToFirst()) {
                    mHorizontalListAdapter.swapCursor(mCursorTags);
                }


            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public void refreshItemDetailsFragmentLoader() {
        if (getLoaderManager().getLoader(DETAIL_NEWS_LOADER) != null) {
            getLoaderManager().restartLoader(DETAIL_NEWS_LOADER, null, this);
        }

    }

    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.edit_button) {

        }
        if (view.getId() == R.id.change_image_link) {

        }

        if (view.getId() == R.id.delete_button) {

            Log.i(Utils.TAG_LOG, "You click DeleteButton!");
            ((Callback) getActivity()).onDeleteItemSelected(mUriPreview);

        }
        if (view.getId() == R.id.like_button) {

            Log.i(Utils.TAG_LOG, "You click LikeButton!");
            int mCountIsLikes = 0;
            int isLikeFlag = 0;

            String mCountLikes = mLikesCountView.getText().toString();
            if (mCountLikes != null) {
                mCountIsLikes = Integer.parseInt(mCountLikes);
            }
            if (mCountIsLikes >= 0) {
                if (mLikeButton.getTag() == "1") {

                    isLikeFlag = 0;
                    mLikeButton.setTag("0");
                    mLikeButton.setImageResource(mDisLikeImage);
                    mCountIsLikes = mCountIsLikes - 1;


                } else if (mLikeButton.getTag() == "0") {
                    isLikeFlag = 1;
                    mLikeButton.setTag("1");
                    mLikeButton.setImageResource(mLikeImage);
                    mCountIsLikes = mCountIsLikes + 1;


                }

                mLikesCountView.setText(String.valueOf(mCountIsLikes));
                ((Callback) getActivity()).onLikeItemSelected(mUriPreview, isLikeFlag);
            } else {
                Log.i(Utils.TAG_LOG, "Error in like clear All!");
                mLikesCountView.setText(String.valueOf(0));
                ((Callback) getActivity()).onLikeItemSelected(mUriPreview, 0);
            }


        }


    }

    public interface Callback {


        public void onLikeItemSelected(Uri uri, int isLike);

        public void onCommentItemSelected(Uri uri);

        public void onEditItemSelected(Uri uri);

        public void onDeleteItemSelected(Uri uri);

    }

}