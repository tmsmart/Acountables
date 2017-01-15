package group.g203.countables.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.ManagerOfNotifications;
import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import io.realm.Realm;

public class BaseTimingReceiver extends BroadcastReceiver {

    Realm mRealm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Countable mCountable;
        int countableId = intent.getExtras().getInt(Constants.COUNTABLE_ID);

        getRealmInstance().beginTransaction();
        mCountable = getRealmInstance().where(Countable.class).equalTo(Constants.ID, countableId).findFirst();
        if (mCountable != null) {
            DateField date = getRealmInstance().createObject(DateField.class);
            date.date = new Date();

            mCountable.accountableDates.add(date);
        }
        getRealmInstance().commitTransaction();

        if (mCountable != null && mCountable.isReminderEnabled) {
            ManagerOfNotifications.getInstance(context)
                    .sendNotification(ManagerOfNotifications.getInstance(context)
                            .buildNotification(mCountable.name, Integer.toString(mCountable.id)));
        }
    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}
