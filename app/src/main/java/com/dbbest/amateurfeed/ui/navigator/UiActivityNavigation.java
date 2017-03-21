package com.dbbest.amateurfeed.ui.navigator;

import android.content.Context;
import android.content.Intent;

import com.dbbest.amateurfeed.ui.activity.HomeActivity;
import com.dbbest.amateurfeed.ui.activity.ResetPasswordActivity;
import com.dbbest.amateurfeed.ui.activity.SignUpActivity;
import com.dbbest.amateurfeed.ui.activity.StartActivity;

public class UiActivityNavigation {

    private UiActivityNavigation() {
    }

    public static Intent startActivity(Context context) {
        return new Intent(context, StartActivity.class);
    }

    public static Intent registerActivity(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    public static Intent resetPassActivity(Context context) {
        return new Intent(context, ResetPasswordActivity.class);
    }

    public static Intent homeActivity(Context context) {
        return new Intent(context, HomeActivity.class);
    }

}
