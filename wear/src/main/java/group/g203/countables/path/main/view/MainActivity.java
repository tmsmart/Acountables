package group.g203.countables.path.main.view;

import android.os.Bundle;

import com.google.android.gms.wearable.Wearable;

import group.g203.countables.base.view.BaseActivity;
import group.g203.countables.path.main.presenter.MainPresenter;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new MainPresenter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleContentDisplay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mClient, this);
    }
}
