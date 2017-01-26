package com.dbbest.amateurfeed.app.net.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 19.01.17.
 */

public class LoginResponseModel {
    @SerializedName("userId")
    private int mUserId;

    @SerializedName("access_token")
    private String mAccessToken;

    @SerializedName("userName")
    private String mUserName;

    @SerializedName("role")
    private String mRole;

    @SerializedName("profileImage")
    private String mProfileImage;


    public LoginResponseModel(int userId, String accessToken, String userName, String role, String profileImage) {
        mUserId = userId;
        mAccessToken = accessToken;
        mUserName = userName;
        mRole = role;
        mProfileImage = profileImage;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getRole() {
        return mRole;
    }

    public String getProfileImage() {
        return mProfileImage;
    }


    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "mUserId=" + mUserId +
                ", mAccessToken='" + mAccessToken + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mProfileImage='" + mProfileImage + '\'' +
                '}';
    }
}
