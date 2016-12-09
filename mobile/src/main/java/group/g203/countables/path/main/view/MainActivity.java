package group.g203.countables.path.main.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.manager.BasePresenterManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.custom_view.loading_view.LoadingAspect;
import group.g203.countables.path.main.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainView {

    @Bind(R.id.loading_aspect)
    LoadingAspect mLoadingAspect;
    @Bind(R.id.rvCountables)
    public RecyclerView mCountablesRv;
    @Bind(R.id.etCountable)
    public EditText mCountableField;
    @Bind(R.id.ivAddCountable)
    public ImageView mAddCountable;
    @Bind(R.id.clSnack)
    public CoordinatorLayout clSnack;
    public View mView;
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.activity_main, null, false);
        setContentView(mView);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setPresenterFromState(savedInstanceState);
        setEmptyParams();
        handleContentDisplay();
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
    public void onResume() {
        super.onResume();
        if (mPresenter.mAdapter != null) {
            mPresenter.mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleContentDisplay() {
        mPresenter.handleInitialContentDisplay();
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

    void setPresenterFromState(Bundle savedState) {
        if (savedState != null) {
            setPresenter(BasePresenterManager.getInstance().getPresenter(savedState));
        } else {
            setPresenter(new MainPresenter());
        }
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof MainPresenter) {
            mPresenter = (MainPresenter) presenter;
            mPresenter.bindViews(this, mLoadingAspect);
        } else {
            mPresenter.displaySnackbarMessage(getString(R.string.general_error));
        }
    }

    @Override
    public MainPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        BasePresenterManager.getInstance().savePresenter(mPresenter, outState);
        super.onSaveInstanceState(outState);
    }

    public void setEmptyParams() {
        mPresenter.setEmptyIcon(R.mipmap.ic_empty_file);
        mPresenter.setEmptyMessage(getString(R.string.no_countables));
    }
}
