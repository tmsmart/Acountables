package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Date;

import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.utils.DisplayUtils;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import group.g203.countables.path.detail.view.DateAdapter;
import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.detail.view.TimeLogFragment;
import group.g203.countables.path.detail.view.TimeLogView;
import io.realm.Realm;
import io.realm.RealmList;

public class TimeLogPresenter implements BasePresenter {

    private final static String COMPLETED = "Successfully logged completion";
    private final static String REVERT = "Latest completion reverted";

    Realm mRealm;
    View mView;
    Context mContext;
    CoordinatorLayout mSnack;
    TimeLogView mTimeLogView;
    TimeLogFragment.CompleteCountListener mListener;
    FloatingActionButton mFab;
    RecyclerView mTimeLogRv;
    RelativeLayout mEmptyView;
    DateAdapter mAdapter;
    Countable mCountable;

    @Override
    public void bindModels() {
        getRealmInstance().beginTransaction();
        mCountable = getRealmInstance().where(Countable.class).equalTo(Constants.INDEX,
                ((TimeLogFragment) mTimeLogView).getArguments().getInt(Constants.COUNTABLE_INDEX)).findFirst();
        getRealmInstance().commitTransaction();
    }

    @Override
    public void unbindModels() {

    }

    @Override
    public void bindViews(BaseView... views) {
        mTimeLogView = (TimeLogView) views[0];
        mView = ((TimeLogFragment)mTimeLogView).mView;
        mFab = ((TimeLogFragment)mTimeLogView).mFab;
        mContext = mView.getContext();
        mSnack = ((DetailActivity)((TimeLogFragment)mTimeLogView).getActivity()).mSnack;
        mListener = ((TimeLogFragment)mTimeLogView).mListener;

        mTimeLogRv = ((TimeLogFragment)mTimeLogView).rvTimeLog;
        mEmptyView = ((TimeLogFragment)mTimeLogView).mEmptyView;
        bindModels();
    }

    @Override
    public void unbindViews() {

    }

    @Override
    public void unbindModelsAndViews() {

    }

    @Override
    public void displaySnackbarMessage(String message) {
        DisplayUtils.displaySimpleSnackbar(mSnack, message, Snackbar.LENGTH_SHORT, null);
    }

    @Override
    public void displayToast(String message) {

    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

    public void handleInitialContentDisplay() {
        setupTimeLogRv(mCountable.loggedDates);
        if (!CollectionUtils.isEmpty(mCountable.loggedDates, true)) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    void setupTimeLogRv(RealmList<DateField> dates) {
        mTimeLogRv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new DateAdapter(dates);
        mTimeLogRv.setAdapter(mAdapter);
        mTimeLogRv.setHasFixedSize(true);
    }

    public void setFabClick() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRealmInstance().beginTransaction();

                DateField dateField = getRealmInstance().createObject(DateField.class);
                dateField.date = new Date();

                mCountable.loggedDates.add(dateField);
                mCountable.timesCompleted = mCountable.loggedDates.size();
                mCountable.lastModified = dateField.date;
                mListener.onEditCompleteCount(Integer.toString(mCountable.loggedDates.size()));

                mEmptyView.setVisibility(View.GONE);

                getRealmInstance().commitTransaction();

                mAdapter.setData(mCountable.loggedDates);
                mAdapter.notifyDataSetChanged();
                mTimeLogRv.smoothScrollToPosition(mCountable.loggedDates.size() - 1);

                DisplayUtils.displayActionSnackbar(mSnack,
                        COMPLETED,
                        Constants.UNDO,
                        Snackbar.LENGTH_LONG, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getRealmInstance().beginTransaction();

                                DateField df = mCountable.loggedDates.last();
                                df.deleteFromRealm();
                                mCountable.timesCompleted = mCountable.loggedDates.size();
                                mCountable.lastModified = (mCountable.loggedDates.size() == 0) ? null :
                                        mCountable.loggedDates.last().date;
                                mListener.onEditCompleteCount(Integer.toString(mCountable.loggedDates.size()));

                                getRealmInstance().commitTransaction();

                                mAdapter.setData(mCountable.loggedDates);
                                mAdapter.notifyDataSetChanged();

                                if (!CollectionUtils.isEmpty(mCountable.loggedDates, true)) {
                                    mTimeLogRv.smoothScrollToPosition(mCountable.loggedDates.size() - 1);
                                } else {
                                    mEmptyView.setVisibility(View.VISIBLE);
                                }
                                displaySnackbarMessage(REVERT);
                            }
                        }, null);
            }
        });
    }
}
