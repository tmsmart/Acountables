package group.g203.countables.path.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.BasePresenterManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.path.detail.presenter.DetailPresenter;

public class DetailActivity extends AppCompatActivity implements DetailView, DeleteDialog.DeleteCountableListener,
        EditDialog.EditCountableListener {

    DetailPresenter mPresenter;
    public View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.activity_detail, null, false);
        setContentView(mView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.EMPTY_STRING);
        toolbar.setSubtitle(Constants.EMPTY_STRING);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setPresenter(new DetailPresenter());
        setInitialCountableInfo();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void displayTimeLog() {

    }

    @Override
    public void displayAccountableView() {

    }

    @Override
    public void displayReminderView() {

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
    public void setInitialCountableInfo() {
        mPresenter.handleInitDisplay(getIntent().getExtras().getString(Constants.COUNTABLE_NAME),
                getIntent().getExtras().getString(Constants.COUNTABLE_COUNT));
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
}
