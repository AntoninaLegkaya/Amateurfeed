package com.dbbest.amateurfeed.app.net.command;

import android.app.IntentService;
import android.content.Intent;
import com.dbbest.amateurfeed.App;

public class ExecutionService extends IntentService {

  private static final String ACTION_EXECUTE = "ACTION_EXECUTE";
  private static final String KEY_COMMAND = "COMMAND";
  private String TAG = ExecutionService.class.getName();

  public ExecutionService() {
    super("Execution Service");
  }

  static void send(Command command) {
    if (command == null) {
    }
    Intent intent = new Intent(App.instance(), ExecutionService.class);
    intent.setAction(ACTION_EXECUTE);
    intent.putExtra(KEY_COMMAND, command);
    App.instance().startService(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null && intent.getAction().equals(ACTION_EXECUTE)) {
      Command command = intent.getParcelableExtra(KEY_COMMAND);
      command.execute();
    }
  }
}
