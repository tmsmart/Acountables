package group.g203.countables.base.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import group.g203.countables.base.Constants;
import group.g203.countables.base.receiver.BaseTimingReceiver;
import group.g203.countables.base.utils.CalendarUtils;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.model.Countable;

public class BaseTimingManager {

    private static BaseTimingManager instance;
    Context mContext;

    public static BaseTimingManager getInstance(Context context) {
        if (instance == null) {
            instance = new BaseTimingManager();
            instance.mContext = context;
        }
        return instance;
    }

    public void setTimeBasedAction(Countable countable) {
        cancelTimeBasedAction(countable);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (!CollectionUtils.isEmpty(countable.anchorDates, true)) {
            if (countable.dayRepeater == Constants.ONE) {
                PendingIntent dailyPendingIntent = createDailyPendingIntent(countable.id);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        countable.anchorDates.get(0).date.getTime(), AlarmManager.INTERVAL_DAY, dailyPendingIntent);
            } else {
                for (int i = 0; i < countable.anchorDates.size(); i++) {
                    PendingIntent weeklyOrCustomPendingIntent = createWeeklyOrCustomPendingIntent(countable.id,
                            countable.anchorDates.get(i).date);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            countable.anchorDates.get(i).date.getTime(),
                            AlarmManager.INTERVAL_DAY * countable.dayRepeater,
                            weeklyOrCustomPendingIntent);
                }
            }
        }
    }

    public void cancelTimeBasedAction(Countable countable) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (!CollectionUtils.isEmpty(countable.anchorDates, true)) {
            if (countable.dayRepeater == Constants.ONE) {
                alarmManager.cancel(createDailyPendingIntent(countable.id));
            } else {
                for (int i = 0; i < countable.anchorDates.size(); i++) {
                    alarmManager.cancel(createWeeklyOrCustomPendingIntent(countable.id,
                            countable.anchorDates.get(i).date));
                }
            }
        }
    }

    PendingIntent createDailyPendingIntent(int id) {
        Intent dailyIntent = new Intent(mContext, BaseTimingReceiver.class);
        dailyIntent.putExtra(Constants.COUNTABLE_ID, id);
        return PendingIntent.getBroadcast(mContext, Constants.ZERO,
                dailyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    PendingIntent createWeeklyOrCustomPendingIntent(int id, Date date) {
        int dayOfWeekInt = CalendarUtils.getDayOfWeekInt(date);
        Intent customIntent = new Intent(mContext, BaseTimingReceiver.class);
        customIntent.putExtra(Constants.COUNTABLE_ID, id);
        customIntent.putExtra(Constants.DATE_INDEX, dayOfWeekInt);
        return PendingIntent.getBroadcast(mContext, dayOfWeekInt,
                customIntent, PendingIntent.FLAG_ONE_SHOT);
    }
}
