package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.GsonManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CalendarUtils;
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
import io.realm.Sort;

public class TimeLogPresenter implements BasePresenter {

    private final static String COMPLETED = "Successfully logged completion";
    private final static String REMOVED ="Logged completion removed";
    private final static String RESTORED ="Logged completion restored successfully";
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
        Bundle bundle = ((TimeLogFragment) mTimeLogView).getArguments();
        if (bundle.containsKey(Constants.COUNTABLE_ID)) {
            mCountable = getRealmInstance().where(Countable.class).equalTo(Constants.ID,
                    bundle.getInt(Constants.COUNTABLE_ID)).findFirst();
        } else {
            mCountable = getRealmInstance().where(Countable.class).equalTo(Constants.INDEX,
                    bundle.getInt(Constants.COUNTABLE_INDEX)).findFirst();
        }
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
        modelSanityCheck();
        ArrayList<Date> dateData = CalendarUtils.returnLoggedAndAccountableDates(mCountable.loggedDates,
                mCountable.accountableDates);
        setupTimeLogRv(dateData);
        if (!CollectionUtils.isEmpty(dateData, false)) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    void setupTimeLogRv(ArrayList<Date> dates) {
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

                mAdapter.setData(CalendarUtils.returnLoggedAndAccountableDates(mCountable.loggedDates,
                        mCountable.accountableDates));
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

                                mAdapter.setData(CalendarUtils.returnLoggedAndAccountableDates(mCountable.loggedDates,
                                        mCountable.accountableDates));
                                mAdapter.notifyDataSetChanged();

                                if (!CollectionUtils.isEmpty(mCountable.loggedDates, true)) {
                                    mTimeLogRv.smoothScrollToPosition(mCountable.loggedDates.size() - 1);
                                } else {
                                    mEmptyView.setVisibility(View.VISIBLE);
                                }
                                displaySnackbarMessage(REVERT);
                                sendCountableDataToWear();
                            }
                        }, null);
                sendCountableDataToWear();
            }
        });
    }

    void modelSanityCheck() {
        if (mCountable == null) {
            bindModels();
        }
    }

    void removeLoggedDate(int position) {
        getRealmInstance().beginTransaction();

        DateField df = mCountable.loggedDates.get(position);
        final Date storedDate = df.date;
        df.deleteFromRealm();
        mCountable.timesCompleted = mCountable.loggedDates.size();
        mCountable.lastModified = (mCountable.loggedDates.size() == 0) ? null :
                mCountable.loggedDates.last().date;
        mListener.onEditCompleteCount(Integer.toString(mCountable.loggedDates.size()));

        getRealmInstance().commitTransaction();

        mAdapter.setData(CalendarUtils.returnLoggedAndAccountableDates(mCountable.loggedDates,
                mCountable.accountableDates));
        mAdapter.notifyDataSetChanged();

        if (!CollectionUtils.isEmpty(mCountable.loggedDates, true)) {
            mTimeLogRv.smoothScrollToPosition(mCountable.loggedDates.size() - 1);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }

        DisplayUtils.displayActionSnackbar(mSnack,
                REMOVED,
                Constants.UNDO,
                Snackbar.LENGTH_LONG, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRealmInstance().beginTransaction();

                        DateField dateField = new DateField();
                        dateField.date = storedDate;

                        mCountable.loggedDates.add(dateField);
                        mCountable.timesCompleted = mCountable.loggedDates.size();
                        mCountable.lastModified = dateField.date;
                        mListener.onEditCompleteCount(Integer.toString(mCountable.loggedDates.size()));

                        mEmptyView.setVisibility(View.GONE);

                        getRealmInstance().commitTransaction();

                        mAdapter.setData(CalendarUtils.returnLoggedAndAccountableDates(mCountable.loggedDates,
                                mCountable.accountableDates));
                        mAdapter.notifyDataSetChanged();
                        mTimeLogRv.smoothScrollToPosition(mCountable.loggedDates.size() - 1);
                        displaySnackbarMessage(RESTORED);
                        sendCountableDataToWear();
                    }
                }, null);

        sendCountableDataToWear();
    }

    void sendCountableDataToWear() {
        ArrayList<String> countableList = new ArrayList<>(1);

        getRealmInstance().beginTransaction();
        List<Countable> allCountables = getRealmInstance().where(Countable.class).findAll().sort(Constants.INDEX, Sort.ASCENDING);
        getRealmInstance().commitTransaction();

        if (CollectionUtils.isEmpty(allCountables, true)) {
        } else {
            countableList = new ArrayList<>(allCountables.size());
            for (Countable countable : allCountables) {
                countableList.add(GsonManager.toJson(countable));
            }
        }
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(Constants.FORWARD_SLASH + mContext.getString(R.string.all_countable_data));
        putDataMapReq.getDataMap().putStringArrayList(mContext.getString(R.string.get_all_countables_key), countableList);
        putDataMapReq.getDataMap().putLong(mContext.getString(R.string.data_map_time), System.currentTimeMillis());
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(((DetailActivity)((TimeLogFragment)mTimeLogView).getActivity()).mClient, putDataReq);
    }
}
