package group.g203.countables.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import group.g203.countables.base.manager.ManagerOfNotifications;

public class ReminderDeleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ManagerOfNotifications.getInstance(context).removeNotificationIdsFromPrefs();
    }
}
