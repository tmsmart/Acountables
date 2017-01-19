package group.g203.countables.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.ManagerOfNotifications;
import group.g203.countables.base.utils.ComparisonUtils;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.main.view.MainActivity;
import io.realm.Realm;

public class ReminderClickReceiver extends BroadcastReceiver {

    Realm mRealm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startIntent;
        if (ComparisonUtils.isNumber(intent.getAction())) {
            Integer countableId = Integer.parseInt(intent.getAction());
            Countable countable;

            getRealmInstance().beginTransaction();
            countable = getRealmInstance().where(Countable.class).equalTo(Constants.ID, countableId).findFirst();
            getRealmInstance().commitTransaction();

            if (countable != null) {
                startIntent = new Intent(context, DetailActivity.class);
                startIntent.putExtra(Constants.COUNTABLE_ID, countableId);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startIntent);
            } else {
                initiateMainScreenStart(context);
            }

        } else {
            initiateMainScreenStart(context);
        }
        ManagerOfNotifications.getInstance(context).removeNotificationIdsFromPrefs();
    }

    void initiateMainScreenStart(Context context) {
        Intent startIntent = new Intent(context, MainActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startIntent);
    }


    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}
