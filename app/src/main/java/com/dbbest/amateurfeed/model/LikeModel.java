package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class LikeModel {

    @SerializedName("isLike")
    private boolean mLike;

    public LikeModel(boolean like) {

        mLike = like;
    }

    public boolean isLike() {
        return mLike;
    }
}
