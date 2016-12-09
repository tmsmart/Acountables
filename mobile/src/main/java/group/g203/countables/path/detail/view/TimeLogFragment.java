package group.g203.countables.path.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.path.detail.presenter.TimeLogPresenter;

public class TimeLogFragment extends Fragment implements TimeLogView {

    public static final String TAG = "TimeLogFrag";

    @Bind(R.id.fab)
    public FloatingActionButton mFab;
    @Bind(R.id.rvTimeLog)
    public RecyclerView rvTimeLog;
    @Bind(R.id.log_empty_view)
    public RelativeLayout mEmptyView;
    public View mView;
    public CompleteCountListener mListener;
    TimeLogPresenter mPresenter;

    public static TimeLogFragment getInstance(String tag, String bundleArg, int bundleKey) {
        TimeLogFragment frag = new TimeLogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DIALOG_TAG, tag);
        bundle.putInt(bundleArg, bundleKey);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.time_log_layout, container, false);
        ButterKnife.bind(this, mView);
        setPresenter(new TimeLogPresenter());
        setFabClick();
        displayInitialTimeLog();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (CompleteCountListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof TimeLogPresenter) {
            mPresenter = (TimeLogPresenter) presenter;
            mPresenter.bindViews(this);
        } else {
            mPresenter.displaySnackbarMessage(getString(R.string.general_error));
        }
    }

    @Override
    public TimeLogPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setFabClick() {
        mPresenter.setFabClick();
    }

    @Override
    public void displayInitialTimeLog() {
        mPresenter.handleInitialContentDisplay();
    }

    public interface CompleteCountListener {
        public void onEditCompleteCount(String count);
    }
}
