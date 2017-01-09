package group.g203.countables.path.detail.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.BasePresenterManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.ComparisonUtils;
import group.g203.countables.custom_view.loading_view.LoadingAspect;
import group.g203.countables.path.detail.presenter.DetailPresenter;

public class DetailActivity extends AppCompatActivity implements DetailView, DeleteDialog.DeleteCountableListener,
        EditDialog.EditCountableListener, TimeLogFragment.CompleteCountListener, EditTimeDialog.TimeDialogListener,
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public final static int TIME_LOG_INDEX = 0;
    public final static int ACCOUNTABLE_INDEX = 1;
    public final static int REMINDER_INDEX = 2;

    DetailPresenter mPresenter;
    @Bind(R.id.llAccountable)
    public LinearLayout llAccountable;
    @Bind(R.id.llTimeLog)
    public LinearLayout llTimeLog;
    @Bind(R.id.llReminder)
    public LinearLayout llReminder;
    @Bind(R.id.tvAccountable)
    public TextView tvAccountable;
    @Bind(R.id.ivAccountable)
    public ImageView ivAccountable;
    @Bind(R.id.tvReminder)
    public TextView tvReminder;
    @Bind(R.id.ivReminder)
    public ImageView ivReminder;
    @Bind(R.id.tvTimeLog)
    public TextView tvTimeLog;
    @Bind(R.id.ivTimeLog)
    public ImageView ivTimeLog;
    @Bind(R.id.loading_frame)
    public LoadingAspect mLoadingAspect;
    @Bind(R.id.loading_countable_info)
    public ProgressBar mInfoProgress;
    @Bind(R.id.frame)
    public FrameLayout mFrame;
    @Bind(R.id.clSnackBar)
    public CoordinatorLayout mSnack;
    public View mView;
    public GoogleApiClient mClient;
    public Node mNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.activity_detail, null, false);
        setContentView(mView);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.EMPTY_STRING);
        toolbar.setSubtitle(Constants.EMPTY_STRING);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setPresenterFromState(savedInstanceState);
    }

    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
        if (!TextUtils.isEmpty(intent.getAction()) && ComparisonUtils.isNumber(intent.getAction())) {
            setPresenter(new DetailPresenter(Integer.parseInt(intent.getAction())));
        } else {
            setPresenter(new DetailPresenter());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            displayEditDialog();
        } else if (id == R.id.action_info) {
            displayInfoDialog();
        } else if (id == R.id.action_delete) {
            displayDeleteDialog();
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mClient != null && !mClient.isConnected() && !mClient.isConnecting()) {
            mClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setInitialCountableInfo(mPresenter.mNavIndex);
        mPresenter.onGoogleApiConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mClient != null) {
            Wearable.DataApi.removeListener(mClient, this);
        }
    }

    @Override
    public void displayTimeLog() {
        mPresenter.displayTimeLog(getSupportFragmentManager());
    }

    @Override
    public void displayAccountableView() {
        mPresenter.displayAccountableView(getSupportFragmentManager());
    }

    @Override
    public void displayReminderView() {
        mPresenter.displayReminderView(getSupportFragmentManager());
    }

    @Override
    public void setTimeLogClick() {
        mPresenter.setTimeLogClick(getSupportFragmentManager());
    }

    @Override
    public void setAccountableClick() {
        mPresenter.setAccountableClick(getSupportFragmentManager());
    }

    @Override
    public void setReminderClick() {
        mPresenter.setReminderClick(getSupportFragmentManager());
    }

    @Override
    public void displayInfoDialog() {
        mPresenter.displayInfoDialog(getSupportFragmentManager());
    }

    @Override
    public void displayDeleteDialog() {
        mPresenter.displayDeleteDialog(getSupportFragmentManager());
    }

    @Override
    public void displayEditDialog() {
        mPresenter.displayEditDialog(getSupportFragmentManager());
    }

    @Override
    public void setInitialCountableInfo(int navIndex) {
        mPresenter.handleInitDisplay(navIndex);
    }

    void setPresenterFromState(Bundle savedState) {
        if (savedState != null) {
            setPresenter(BasePresenterManager.getInstance().getPresenter(savedState));
        } else {
            Intent intent = getIntent();
            if (intent != null && !TextUtils.isEmpty(intent.getAction()) && ComparisonUtils.isNumber(intent.getAction())) {
                setPresenter(new DetailPresenter(Integer.parseInt(intent.getAction())));
            } else {
                setPresenter(new DetailPresenter());
            }
        }
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof DetailPresenter) {
            mPresenter = (DetailPresenter) presenter;
            mPresenter.bindViews(this);
        } else {
            mPresenter.displaySnackbarMessage(getString(R.string.general_error));
        }
    }

    @Override
    public DetailPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        BasePresenterManager.getInstance().savePresenter(mPresenter, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDeleteCountableClick(DeleteDialog dialog) {
        mPresenter.deleteAndGoBack();
    }

    @Override
    public void onEditCountableClick(EditDialog dialog) {
        mPresenter.renameCountable(dialog.mCountableName.getText().toString());
    }

    @Override
    public void onEditCompleteCount(String count) {
        mPresenter.setCompletedCount(count);
    }

    @Override
    public void onEditAccountableClick(EditTimeDialog dialog) {
        mPresenter.editAccountability(getSupportFragmentManager());
    }

    @Override
    public void onEditReminderClick(EditTimeDialog dialog) {
        mPresenter.resetReminder(getSupportFragmentManager());
    }

    @Override
    public void onDeleteAccountableClick(EditTimeDialog dialog) {
        mPresenter.deleteAccountability(getSupportFragmentManager());
    }

    @Override
    public void onDeleteReminderClick(EditTimeDialog dialog) {
        mPresenter.deleteReminder(getSupportFragmentManager());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPresenter.onGoogleApiConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPresenter.displayToast(getString(R.string.connection_error));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mPresenter.displayToast(getString(R.string.connection_error));
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        mPresenter.onDataChanged(dataEventBuffer, getSupportFragmentManager());
    }
}
