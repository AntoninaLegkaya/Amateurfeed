package com.dbbest.amateurfeed.app.net.command;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.utils.Utils;

/**
 * Created by antonina on 19.01.17.
 */
public class ExecutionService extends IntentService {

    private static final String ACTION_EXECUTE = "ACTION_EXECUTE";
    private static final String KEY_COMMAND = "COMMAND";

    public ExecutionService() {
        super("Execution Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction().equals(ACTION_EXECUTE)) {
            Command command = intent.getParcelableExtra(KEY_COMMAND);
            command.execute();
        }
    }


    static void send(Command command) {
        if (command == null) {
        }
        if (App.instance() != null) {
            Intent intent = new Intent(App.instance(), ExecutionService.class);
            intent.setAction(ACTION_EXECUTE);
            intent.putExtra(KEY_COMMAND, command);
            App.instance().startService(intent);
        } else {
            Log.i(Utils.TAG_LOG, "App.instance() == null");
        }
    }
}
