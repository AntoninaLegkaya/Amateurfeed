package com.dbbest.amateurfeed.app.net.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class UpdateProfileRequestModel {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("image")
    private String mImage;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("job")
    private String mJob;

    public UpdateProfileRequestModel(String fullName, String email, String image, String phone, String job) {
        this.fullName = fullName;
        mEmail = email;
        mImage = image;
        mPhone = phone;
        mJob = job;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getImage() {
        return mImage;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getJob() {
        return mJob;
    }
}
