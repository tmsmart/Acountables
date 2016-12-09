package group.g203.countables.model;

import android.app.Notification;

import java.util.HashSet;

public class NotificationWrapper {

    public Notification mNotification;
    public HashSet<String> mIds;

    public NotificationWrapper(Notification notification, HashSet<String> ids) {
        this.mNotification = notification;
        this.mIds = ids;
    }
}
