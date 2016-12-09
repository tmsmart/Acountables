package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.BaseDialogManager;
import group.g203.countables.base.manager.BaseTimingManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CalendarUtils;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.utils.DisplayUtils;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import group.g203.countables.path.detail.view.EditTimeDialog;
import group.g203.countables.path.detail.view.ReminderFragment;
import group.g203.countables.path.detail.view.ReminderView;
import group.g203.countables.path.detail.view.TimeSelectionDialog;
import io.realm.Realm;

public class ReminderPresenter implements BasePresenter {

    Realm mRealm;
    ReminderView mReminderView;
    Context mContext;
    SwitchCompat mSwitch;
    LinearLayout mIsSetLayout;
    LinearLayout llReminderInfo;
    TextView tvDelete;
    TextView tvEdit;
    TextView tvTime;
    TextView tvHours;
    TextView tvMins;
    TextView tvAmPm;
    TextView tvColon;
    LinearLayout llTime;
    EditTimeDialog mEditDialog;
    EditTimeDialog mDeleteDialog;
    Countable mCountable;

    @Override
    public void displayToast(String message) {

    }

    @Override
    public void bindModels() {
        getRealmInstance().beginTransaction();
        Bundle bundle = ((ReminderFragment) mReminderView).getArguments();
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
        mReminderView = (ReminderView) views[0];

        mSwitch = ((ReminderFragment) mReminderView).mSwitch;
        mIsSetLayout = ((ReminderFragment) mReminderView).mIsSetLayout;
        llReminderInfo = ((ReminderFragment) mReminderView).llReminderInfo;
        tvDelete = ((ReminderFragment) mReminderView).tvDelete;
        tvEdit = ((ReminderFragment) mReminderView).tvEdit;
        tvTime = ((ReminderFragment) mReminderView).tvTime;
        tvHours = ((ReminderFragment) mReminderView).tvHours;
        tvMins = ((ReminderFragment) mReminderView).tvMins;
        tvAmPm = ((ReminderFragment) mReminderView).tvAmPm;
        tvColon = ((ReminderFragment) mReminderView).tvColon;
        llTime = ((ReminderFragment) mReminderView).llTime;

        mContext = mSwitch.getContext();

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

    }

    public void handleDisplay() {
        modelSanityCheck();
        setReadOnly(false);
        mSwitch.setText(mContext.getString(R.string.enable_reminder));

        if (CollectionUtils.isEmpty(mCountable.anchorDates, true)) {
            mSwitch.setEnabled(false);
        } else {
            if (!mCountable.isReminderEnabled) {
                mIsSetLayout.setVisibility(View.GONE);
                mSwitch.setVisibility(View.VISIBLE);
                llReminderInfo.setVisibility(View.VISIBLE);
                setSwitch();
            } else {
                mSwitch.setVisibility(View.GONE);
                llReminderInfo.setVisibility(View.GONE);
                mIsSetLayout.setVisibility(View.VISIBLE);
                styleReadData();
                setEditClick();
                setDeleteClick();
            }
        }
    }

    public void setSwitch() {
        mSwitch.setVisibility(View.VISIBLE);
        mSwitch.setEnabled(true);

        llReminderInfo.setVisibility(View.VISIBLE);
        mSwitch.setText(mContext.getString(R.string.enable_reminder));
        mSwitch.setChecked(false);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                modelSanityCheck();
                if (isChecked) {
                    getRealmInstance().beginTransaction();
                    for (DateField field : mCountable.anchorDates) {
                        field.date = CalendarUtils.setNotificationDate(field.date, Constants.ZERO, Constants.ZERO);
                    }
                    getRealmInstance().commitTransaction();
                    setActive();
                } else {
                    getRealmInstance().beginTransaction();
                    mCountable.isReminderEnabled = false;
                    getRealmInstance().commitTransaction();
                    setReadOnly(true);
                }
            }
        });
    }

    public void setDeleteClick() {
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteDialog = EditTimeDialog.getInstance(EditTimeDialog.TAG, mContext.getString(R.string.delete_reminder));
                BaseDialogManager.displayFragmentDialog(mDeleteDialog, EditTimeDialog.TAG, ((ReminderFragment) mReminderView).getFragmentManager());
            }
        });
    }

    public void setEditClick() {
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditDialog = EditTimeDialog.getInstance(EditTimeDialog.TAG, mContext.getString(R.string.edit_reminder));
                BaseDialogManager.displayFragmentDialog(mEditDialog, EditTimeDialog.TAG, ((ReminderFragment) mReminderView).getFragmentManager());
            }
        });
    }

    void setReadOnly(boolean disableNotifications) {
        if (disableNotifications) {
            modelSanityCheck();
            getRealmInstance().beginTransaction();
            mCountable.isReminderEnabled = false;
            getRealmInstance().commitTransaction();
        }

        handleTimeDisplay(false, tvTime, tvMins, tvHours, tvAmPm, tvColon);
    }

    void styleReadData() {
        int timeColor = ContextCompat.getColor(mContext, R.color.app_gray);
        int color = ContextCompat.getColor(mContext, R.color.darkest_app_green);

        modelSanityCheck();
        Pair<Integer, Integer> hoursAndMins = CalendarUtils.getHoursAndMins(mCountable.anchorDates.get(0));
        int hours = hoursAndMins.first;
        int mins = hoursAndMins.second;

        if (hours >= Constants.HOURS_MAX) {
            tvAmPm.setText(mContext.getString(R.string.pm));
            tvHours.setText((hours == Constants.HOURS_MAX) ? Integer.toString(hours) : Integer.toString(hours - Constants.HOURS_MAX));
        } else {
            tvAmPm.setText(mContext.getString(R.string.am));
            tvHours.setText((hours == Constants.ZERO) ? Integer.toString(Constants.HOURS_MAX) : Integer.toString(hours));
        }

        tvMins.setText((Integer.toString(mins).length() == 1) ? Constants.ZERO_STRING + Integer.toString(mins) : Integer.toString(mins));

        tvHours.setTextColor(color);
        tvMins.setTextColor(color);
        tvAmPm.setTextColor(color);
        tvTime.setTextColor(timeColor);
        tvColon.setTextColor(color);
    }

    void setActive() {
        handleTimeDisplay(true, tvTime, tvMins, tvHours, tvAmPm, tvColon);
    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

    void modelSanityCheck() {
        if (mCountable == null) {
            bindModels();
        }
    }

    void handleTimeDisplay(boolean enabled, TextView... texts) {
        int timeColor = (enabled) ? ContextCompat.getColor(mContext, R.color.app_gray) :
                ContextCompat.getColor(mContext, R.color.fallback_gray);

        int color = (enabled) ? ContextCompat.getColor(mContext, R.color.bright_app_green) :
                ContextCompat.getColor(mContext, R.color.fallback_gray);

        for (int i = 0; i < texts.length; i++) {
            if (i == 0) {
                texts[i].setTextColor(timeColor);
            } else {
                texts[i].setTextColor(color);
            }
        }

        if (enabled) {
          llTime.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  TimeSelectionDialog dialog = TimeSelectionDialog.getInstance(TimeSelectionDialog.TAG);
                  BaseDialogManager.displayFragmentDialog(dialog, dialog.getTag(),
                          ((ReminderFragment) mReminderView).getActivity().getSupportFragmentManager());
              }
          });
        } else {
            llTime.setOnClickListener(null);
        }
    }

    public void resetReminder() {
        mIsSetLayout.setVisibility(View.GONE);
        setActive();
        DisplayUtils.displayToast(mContext, mContext.getString(R.string.editing_reminder), Toast.LENGTH_SHORT);
    }

    public void onDeleteReminder() {
        modelSanityCheck();
        getRealmInstance().beginTransaction();
        mCountable.isReminderEnabled = false;
        for (DateField field : mCountable.anchorDates) {
            field.date = CalendarUtils.setNotificationDate(field.date, Constants.ZERO, Constants.ZERO);
        }
        getRealmInstance().commitTransaction();
        mSwitch.setEnabled(false);
        mIsSetLayout.setVisibility(View.GONE);
        mSwitch.setVisibility(View.VISIBLE);
        llReminderInfo.setVisibility(View.VISIBLE);
        setSwitch();
        styleReadData();
        setReadOnly(true);
        DisplayUtils.displayToast(mContext, mContext.getString(R.string.deleting_reminder), Toast.LENGTH_SHORT);
    }

    public void saveReminderTime(boolean isAm) {
        modelSanityCheck();
        getRealmInstance().beginTransaction();
        mCountable.isReminderEnabled = true;
        int minsVal = (TextUtils.isEmpty(tvMins.getText().toString())) ? Constants.ZERO :
                Integer.parseInt(tvMins.getText().toString());

        int hoursInt = Integer.parseInt(tvHours.getText().toString());
        int hoursVal;

        if (isAm) {
            if (hoursInt == Constants.HOURS_MAX) {
                hoursVal = Constants.ZERO;
            } else {
                hoursVal = hoursInt;
            }
        } else {
            if (hoursInt == Constants.HOURS_MAX) {
                hoursVal = hoursInt;
            } else {
                hoursVal = hoursInt + Constants.HOURS_MAX;
            }
        }

        for (DateField field : mCountable.anchorDates) {
            field.date = CalendarUtils.setNotificationDate(field.date,
                    hoursVal, minsVal);
        }
        getRealmInstance().commitTransaction();
        doTimeAction();
    }

    void doTimeAction() {
        BaseTimingManager.getInstance(mContext).setTimeBasedAction(mCountable);
    }
}
