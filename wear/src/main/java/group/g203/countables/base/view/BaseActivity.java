package group.g203.countables.base.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableRecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import group.g203.countables.R;
import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.path.detail.presenter.DetailPresenter;
import group.g203.countables.path.main.presenter.MainPresenter;

public class BaseActivity extends Activity implements BaseView {

    public WearableRecyclerView mRecyclerView;
    public ProgressBar mProgress;
    public LinearLayout mButtonLayout;
    public CircledImageView mButton;
    public TextView mTopText;
    public TextView mButtonText;
    MainPresenter mMainPresenter;
    DetailPresenter mDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_layout);

        mRecyclerView = (WearableRecyclerView) findViewById(R.id.countable_list);
        mProgress = (ProgressBar) findViewById(R.id.mProgress);
        mButtonLayout = (LinearLayout) findViewById(R.id.llButtonLayout);
        mTopText = (TextView) findViewById(R.id.tvTopText);
        mButton = (CircledImageView) findViewById(R.id.cvButton);
        mButtonText = (TextView) findViewById(R.id.tvButtonText);
    }

    @Override
    public <P extends GeneralPresenter> void setPresenter(P presenter) {
        if (presenter instanceof MainPresenter) {
            mMainPresenter = (MainPresenter) presenter;
            mMainPresenter.bindViews(this);
        } else if (presenter instanceof DetailPresenter) {
            mDetailPresenter = (DetailPresenter) presenter;
            mDetailPresenter.bindViews(this);
        } else {
            mMainPresenter.displayToast(getString(R.string.general_error));
        }
    }

    @Override
    public void handleContentDisplay() {
        if (mDetailPresenter == null) {
            mMainPresenter.handleContentDisplay();
        } else {
            mDetailPresenter.handleContentDisplay();
        }
    }

    public MainPresenter getMainPresenter() {
        return mMainPresenter;
    }

    public DetailPresenter getDetailPresenter() {
        return mDetailPresenter;
    }
}