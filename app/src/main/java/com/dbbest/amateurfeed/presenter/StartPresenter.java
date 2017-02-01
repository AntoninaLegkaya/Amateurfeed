package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.common.util.TextUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.LoginCommand;
import com.dbbest.amateurfeed.app.net.command.RegistrationFacebookCommand;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.utils.location.LatLng;
import com.dbbest.amateurfeed.utils.location.UserLocationProvider;
import com.dbbest.amateurfeed.view.StartView;
import com.facebook.login.LoginManager;

/**
 * Created by antonina on 19.01.17.
 */

public class StartPresenter extends Presenter<StartView> implements CommandResultReceiver.CommandListener,
        UserLocationProvider.LocationProviderListener {

    private static final String INCORRECT_PASSWORD_MSG_RESPONSE = "An error has occurred.";
    private static final int PERMISSION_LOCATION = 0;


    private static final int CODE_LOGIN = 0;
    private static final int CODE_REGISTRATION_FB = 1;

    private CommandResultReceiver mResultReceiver;
    private UserLocationProvider mLocationProvider;

    public void login(String email, String password, String deviceId, String osType, String deviceToken) {


        if (getView() != null) {
            StartView view = getView();

            if (TextUtils.isEmpty(email)) {
                view.showEmptyEmailError();
                return;
            } else if (!Utils.isEmailValid(email)) {
                view.showEmailValidationError();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                view.showEmptyPasswordError();
                return;
            } else if (!Utils.isPasswordLengthValid(password)) {
                view.showPasswordLengthValidationError();
                return;
            } else if (!Utils.isPasswordValid(password)) {
                view.showPasswordValidationError();
                return;
            }

            view.showProgressDialog();
        }
        Command command = new LoginCommand(email, password, deviceId, osType, deviceToken);
        command.send(CODE_LOGIN, mResultReceiver);
    }

    public void loginFaceBook(String token) {
        LatLng location = mLocationProvider.getLastLocation();
        RegistrationFacebookCommand command = new RegistrationFacebookCommand(token, location.longitude(), location.latitude());
        LoginManager.getInstance().logOut();

        if (getView() != null) {
            getView().showProgressDialog();
        }

        command.send(CODE_REGISTRATION_FB, mResultReceiver);
    }


    @Override
    public void onSuccess(int code, Bundle data) {
        Log.i(Utils.TAG_LOG, "StartPresenter: Success");
        getView().dismissProgressDialog();
        if (getView() != null) {
            if (code == CODE_LOGIN) {
                getView().navigateToHomeScreen();
            }
        }

    }

    @Override
    public void onFail(int code, Bundle data) {
        Log.e(Utils.TAG_LOG, "StartPresenter: onFail");
        if (getView() != null) {
            getView().dismissProgressDialog();
            int errCode = Command.grabErrorCode(data);
            String errMessage = Command.grabErrorText(data);
            if (errCode == NetworkUtil.CODE_SOCKET_TIMEOUT || errCode == NetworkUtil.CODE_UNABLE_TO_RESOLVE_HOST) {
                getView().showErrorConnectionDialog();
            } else {
                if (errMessage != null && errMessage.equals(INCORRECT_PASSWORD_MSG_RESPONSE)) {
                    getView().showErrorIncorrectPassword();
                } else {
                    getView().showErrorLoginDialog();
                }
            }
        }
    }


    @Override
    protected void onAttachView(@NonNull StartView view) {
        super.onAttachView(view);
        if (mResultReceiver == null) {
            mResultReceiver = new CommandResultReceiver();
        }
        mResultReceiver.setListener(this);
    }

    @Override
    protected void onDetachView(@NonNull StartView view) {
        super.onDetachView(view);
        if (mResultReceiver != null)
            mResultReceiver.setListener(null);
    }


    @Override
    public void onProgress(int code, Bundle data, int progress) {

    }

    @Override
    public void onUserLocationUpdated(LatLng location) {

    }

    @Override
    public void onLocationPermissionsDenied() {

    }

}