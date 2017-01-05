package group.g203.countables.base.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableRecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import group.g203.countables.R;
import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.path.detail.presenter.DetailPresenter;
import group.g203.countables.path.main.presenter.MainPresenter;

public class BaseActivity extends Activity implements BaseView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public WearableRecyclerView mRecyclerView;
    public ProgressBar mProgress;
    public LinearLayout mButtonLayout;
    public CircledImageView mButton;
    public TextView mTopText;
    public TextView mButtonText;
    public GoogleApiClient mClient;
    public Node mNode;
    MainPresenter mMainPresenter;
    DetailPresenter mDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_layout);

        mClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mRecyclerView = (WearableRecyclerView) findViewById(R.id.countable_list);
        mProgress = (ProgressBar) findViewById(R.id.mProgress);
        mButtonLayout = (LinearLayout) findViewById(R.id.llButtonLayout);
        mTopText = (TextView) findViewById(R.id.tvTopText);
        mButton = (CircledImageView) findViewById(R.id.cvButton);
        mButtonText = (TextView) findViewById(R.id.tvButtonText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mClient != null && !mClient.isConnected() && !mClient.isConnecting()) {
            mClient.connect();
        }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mDetailPresenter == null) {
            mMainPresenter.onGoogleApiConnected();
        } else {
            mDetailPresenter.onGoogleApiConnected();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mDetailPresenter == null) {
            mMainPresenter.displayToast(getString(R.string.connection_suspension));
        } else {
            mDetailPresenter.displayToast(getString(R.string.connection_suspension));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mDetailPresenter == null) {
            mMainPresenter.displayToast(getString(R.string.connection_error));
        } else {
            mDetailPresenter.displayToast(getString(R.string.connection_error));
        }
    }


}