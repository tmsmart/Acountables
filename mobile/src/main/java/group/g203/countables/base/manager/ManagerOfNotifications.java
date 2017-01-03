package group.g203.countables.base.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.HashSet;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.receiver.ReminderClickReceiver;
import group.g203.countables.base.receiver.ReminderDeleteReceiver;
import group.g203.countables.model.NotificationWrapper;

public class ManagerOfNotifications {

    private final static int REMINDER_ID = -1;
    private final static String MULTIPLE_CONTENT_START = "You have (";
    private final static String MULTIPLE_CONTENT_END = ") Countable events awaiting completion";
    private final static String SINGLE_CONTENT_START = "Your \"";
    private final static String SINGLE_CONTENT_END = "\" Countable event is awaiting completion";
    private static ManagerOfNotifications instance;
    Context mContext;
    NotificationManager mNotificationManager;

    public static ManagerOfNotifications getInstance(Context context) {
        if (instance == null) {
            instance = new ManagerOfNotifications();
            instance.mContext = context;
            instance.mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return instance;
    }

    SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(
                mContext.getString(R.string.pref_file_key), Context.MODE_PRIVATE);
    }

    public NotificationWrapper buildNotification(String countableName, String id) {
        HashSet<String> idSet =
                new HashSet<>(getPreferences().getStringSet(Constants.NOTIFICATION_IDS, new HashSet<String>()));
        idSet.add(id);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(mContext.getString(R.string.app_name));
        builder.setSmallIcon(R.mipmap.ic_plus_one);
        builder.setColor(ContextCompat.getColor(mContext, R.color.app_green));
        builder.setContentText((idSet.size() > Constants.ONE)
                ? MULTIPLE_CONTENT_START + idSet.size()
                + MULTIPLE_CONTENT_END : SINGLE_CONTENT_START + countableName + SINGLE_CONTENT_END);

        Intent notificationDeleteIntent = new Intent(mContext, ReminderDeleteReceiver.class);
        PendingIntent pendingDeleteIntent =
                PendingIntent.getBroadcast(mContext, REMINDER_ID,
                        notificationDeleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setDeleteIntent(pendingDeleteIntent);

        Intent notificationClickIntent = new Intent(mContext, ReminderClickReceiver.class);
        notificationClickIntent.setAction((idSet.size() > Constants.ONE)
                ? mContext.getString(R.string.multiple_reminders) : id);
        PendingIntent pendingClickIntent =
                PendingIntent.getBroadcast(mContext, REMINDER_ID,
                        notificationClickIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(pendingClickIntent);

        addNotificationIdsToPrefs(idSet);

        Notification notification = builder.build();
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        return new NotificationWrapper(notification, idSet);
    }

    public void sendNotification(NotificationWrapper wrapper) {
        mNotificationManager.notify(REMINDER_ID, wrapper.mNotification);
    }

    public void removeNotificationIdsFromPrefs() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putStringSet(Constants.NOTIFICATION_IDS, null);
        editor.commit();
    }

    public void addNotificationIdsToPrefs(HashSet<String> ids) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putStringSet(Constants.NOTIFICATION_IDS, ids);
        editor.commit();
    }
}
