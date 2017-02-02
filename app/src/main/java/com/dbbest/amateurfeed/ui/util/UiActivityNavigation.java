package com.dbbest.amateurfeed.ui.util;

import android.content.Context;
import android.content.Intent;

import com.dbbest.amateurfeed.ui.HomeActivity;
import com.dbbest.amateurfeed.ui.ResetPasswordActivity;
import com.dbbest.amateurfeed.ui.SettingsActivity;
import com.dbbest.amateurfeed.ui.SignUpActivity;
import com.dbbest.amateurfeed.ui.StartActivity;

/**
 * Created by antonina on 19.01.17.
 */

public class UiActivityNavigation {

    private UiActivityNavigation() {

    }

    public static Intent startActivity(Context context) {return new Intent(context, StartActivity.class);
    }

    public static Intent registerActivity(Context context) {return new Intent(context, SignUpActivity.class);
    }

    public static Intent resetPassActivity(Context context) {
        return new Intent(context, ResetPasswordActivity.class);
    }

    public static Intent homeActivity(Context context) {return new Intent(context, HomeActivity.class);}
    public static Intent settingsActivity(Context context) {return new Intent(context, SettingsActivity.class);}
//    public static Intent startCamera() {
//        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    }


    }
