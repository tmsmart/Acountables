package group.g203.countables.path.main.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.manager.BaseDialogManager;
import group.g203.countables.base.manager.BasePresenterManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.custom_view.loading_view.LoadingAspect;
import group.g203.countables.path.main.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainView {

    @Bind(R.id.loading_aspect)
    public LoadingAspect mLoadingAspect;
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        setContentView(contentView);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            setPresenter(new MainPresenter());
        } else {
            setPresenter(BasePresenterManager.getInstance().getPresenter(savedInstanceState));
            List<String> visibleDialogTags = BaseDialogManager.getInstance().getVisibleDialogTags(savedInstanceState);
            if (!CollectionUtils.isEmpty(visibleDialogTags)) {
                mPresenter.displayDialogsAfterStateChange(visibleDialogTags, getSupportFragmentManager());
            }
        }
        setEmptyParams();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            displaySortDialog();
        } else if (id == R.id.action_info) {
            displayInfoDialog();
        } else if (id == R.id.action_credits) {
            displayCreditsDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayEmptyView() {
    }

    @Override
    public void displayLoading() {

    }

    @Override
    public void finishLoading() {

    }

    @Override
    public void displayCountables() {

    }

    @Override
    public void addCountable() {

    }

    @Override
    public void deleteCountable() {

    }

    @Override
    public void undoCountableDelete() {

    }

    @Override
    public void displayInfoDialog() {
        mPresenter.displayInfoDialog(getSupportFragmentManager());
    }

    @Override
    public void displaySortDialog() {
        mPresenter.displaySortDialog(getSupportFragmentManager());
    }

    @Override
    public void displayCreditsDialog() {
        mPresenter.displayCreditsDialog(getSupportFragmentManager());
    }

    @Override
    public void enableAddButton() {

    }

    @Override
    public void disableAddButton() {

    }

    @Override
    public void resetAddCountableView() {

    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof MainPresenter) {
            mPresenter = (MainPresenter) presenter;
            mPresenter.bindViews(this, mLoadingAspect);
        } else {
            mPresenter.displayError(getString(R.string.general_error));
        }
    }

    @Override
    public MainPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        BasePresenterManager.getInstance().savePresenter(mPresenter, outState);

        List<BaseDialog> dialogs = (List<BaseDialog>)(List<?>) getSupportFragmentManager().getFragments();
        if (!CollectionUtils.isEmpty(dialogs)) {
            BaseDialogManager.getInstance().saveVisibleDialogTags(dialogs, outState);
            BaseDialogManager.removeFromFragmentManager(dialogs, getSupportFragmentManager());
        }

        super.onSaveInstanceState(outState);
    }

    public void setEmptyParams() {
        mPresenter.setEmptyIcon(R.mipmap.ic_empty_file);
        mPresenter.setEmptyMessage(getString(R.string.no_countables));
    }
}
