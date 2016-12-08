package group.g203.countables.base;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CountablesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
