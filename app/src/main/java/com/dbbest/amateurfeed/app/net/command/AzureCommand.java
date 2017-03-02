package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.azur.preferences.CloudPreferences;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.AzureServiceSettings;
import com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment;

public class AzureCommand extends Command {

    public AzureCommand() {

    }

    protected AzureCommand(Parcel in) {

        super(in);
    }

    @Override
    public void writeToParcel(int flags, Parcel dest) {

    }

    @Override
    public void execute() {

        RestApiClient apiClient = App.getApiFactory().restClient();
        AuthToken authToken = new AuthToken();
        ResponseWrapper<AzureServiceSettings> response = apiClient.getAzureInfo(authToken.bearer());
        if (response != null) {
            if (response.isSuccessful() && response.data() != null) {
                AzureServiceSettings settings = response.data();

                CloudPreferences cloudPreferences = new CloudPreferences();
                cloudPreferences.writeNameKey(settings.getAccountName());
                cloudPreferences.writeAccessKey(settings.getAccountKey());
                cloudPreferences.writeContainerName(settings.getContainerName());
                notifySuccess(Bundle.EMPTY);


            } else {
                notifyError(Bundle.EMPTY);
            }
        } else {
        }

    }

    public static final Parcelable.Creator<AzureCommand> CREATOR = new Parcelable.Creator<AzureCommand>() {
        @Override
        public AzureCommand createFromParcel(Parcel source) {
            return new AzureCommand(source);
        }

        @Override
        public AzureCommand[] newArray(int size) {
            return new AzureCommand[size];
        }
    };
}
