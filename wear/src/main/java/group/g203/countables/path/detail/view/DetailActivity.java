package group.g203.countables.path.detail.view;

import android.os.Bundle;

import group.g203.countables.base.view.BaseActivity;
import group.g203.countables.path.detail.presenter.DetailPresenter;

public class DetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new DetailPresenter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleContentDisplay();
    }
}