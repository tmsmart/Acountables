package group.g203.countables.base;

import android.app.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class CountablesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(config);
        Realm.setDefaultConfiguration(config);
        createTestData();
    }

    private void createTestData() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<DateField> dates = new RealmList<>();
                DateField date = realm.createObject(DateField.class);
                date.date = new Date();

                DateField date0 = realm.createObject(DateField.class);
                date.date = new Date();

                DateField date1 = realm.createObject(DateField.class);
                date.date = new Date();

                DateField date2 = realm.createObject(DateField.class);
                date.date = new Date();

                DateField date3 = realm.createObject(DateField.class);
                date.date = new Date();

                DateField date4 = realm.createObject(DateField.class);
                date.date = new Date();

                DateField date5 = realm.createObject(DateField.class);
                date.date = new Date();

                dates.add(date);
                dates.add(date0);
                dates.add(date1);
                dates.add(date2);
                dates.add(date3);
                dates.add(date4);
                dates.add(date5);

                Date d = null;
                Date d1 = null;
                Date d2 = null;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    d = sdf.parse("25/12/2012");
                    d1 = sdf.parse("25/12/2013");
                    d2 = sdf.parse("25/12/2014");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Countable countable = realm.createObject(Countable.class);
                countable.name = "A Test";
                countable.index = 2;
                countable.isAccountable = false;
                countable.isReminderEnabled = false;
                countable.loggedDates = dates;
                countable.lastModified = d;
                countable.timesCompleted = 5;

                Countable countable1 = realm.createObject(Countable.class);
                countable1.name = "B Test";
                countable1.index = 1;
                countable1.isAccountable = false;
                countable1.isReminderEnabled = true;
                countable1.loggedDates = dates;
                countable1.lastModified = d1;
                countable1.timesCompleted = 50;

                Countable countable2 = realm.createObject(Countable.class);
                countable2.name = "C Test";
                countable2.index = 0;
                countable2.isAccountable = true;
                countable2.isReminderEnabled = false;
                countable2.loggedDates = dates;
                countable2.lastModified = d2;
                countable2.timesCompleted = 51;

                realm.copyToRealm(countable);
                realm.copyToRealm(countable1);
                realm.copyToRealm(countable2);
            }
        });
        realm.close();
    }
}
