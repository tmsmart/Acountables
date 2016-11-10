package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CalendarUtils;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.utils.ComparisonUtils;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import group.g203.countables.path.detail.view.ReminderFragment;
import group.g203.countables.path.detail.view.ReminderView;
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
    LinearLayout llTime;
    AppCompatRadioButton rbAm;
    AppCompatRadioButton rbPm;
    EditText etHours;
    EditText etMins;
    Countable mCountable;

    @Override
    public void displayToast(String message) {

    }

    @Override
    public void bindModels() {
        getRealmInstance().beginTransaction();
        mCountable = getRealmInstance().where(Countable.class).equalTo(Constants.INDEX,
                ((ReminderFragment) mReminderView).getArguments().getInt(Constants.COUNTABLE_INDEX)).findFirst();
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

        llTime = ((ReminderFragment) mReminderView).llTime;
        rbAm = ((ReminderFragment) mReminderView).rbAm;
        rbPm = ((ReminderFragment) mReminderView).rbPm;
        etHours = ((ReminderFragment) mReminderView).etHours;
        etMins = ((ReminderFragment) mReminderView).etMins;

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
                if (isChecked) {
                    modelSanityCheck();
                    getRealmInstance().beginTransaction();
                    mCountable.isReminderEnabled = true;
                    for (DateField field : mCountable.anchorDates) {
                        field.date = CalendarUtils.setNotificationDate(field.date, Constants.ZERO, Constants.ZERO);
                    }
                    getRealmInstance().commitTransaction();
                    rbAm.setChecked(true);
                    setActive();
                } else {
                    modelSanityCheck();
                    getRealmInstance().beginTransaction();
                    mCountable.isReminderEnabled = false;
                    getRealmInstance().commitTransaction();
                    setReadOnly(true);
                }
            }
        });
    }

    public void setDeleteClick() {

    }

    public void setEditClick() {

    }

    void setReadOnly(boolean disableNotifications) {
        if (disableNotifications) {
            modelSanityCheck();
            getRealmInstance().beginTransaction();
            mCountable.isReminderEnabled = false;

            clearRadioButtons(rbAm, rbPm);
        }

        handleRadioButtonsState(false, rbAm, rbPm);
        handleTimeDisplay(false, etHours, etMins);
    }

    void styleReadData() {
        int color = ContextCompat.getColor(mContext, R.color.darkest_app_green);

        modelSanityCheck();
        Pair<Integer, Integer> hoursAndMins = CalendarUtils.getHoursAndMins(mCountable.anchorDates.get(0));
        int hours = hoursAndMins.first;
        int mins = hoursAndMins.second;

        etHours.setTextColor(color);
        etMins.setTextColor(color);

        etMins.setText((Integer.toString(mins).length() == 1) ? Constants.ZERO_STRING + Integer.toString(mins) : Integer.toString(mins));

        if (hours >= Constants.HOURS_MAX) {
            rbPm.setChecked(true);
            etHours.setText((hours == Constants.HOURS_MAX) ? Integer.toString(hours) : Integer.toString(hours - Constants.HOURS_MAX));
        } else {
            rbAm.setChecked(true);
        }
    }

    void setActive() {
        handleRadioButtonsState(true, rbAm, rbPm);
        handleTimeDisplay(true, etHours, etMins);
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

    void handleRadioButtonsState(boolean enabled, AppCompatRadioButton... buttons) {
        int color = (enabled) ? ContextCompat.getColor(mContext, android.R.color.black) :
                ContextCompat.getColor(mContext, R.color.app_gray);

        for (AppCompatRadioButton button : buttons) {
            button.setEnabled(enabled);
            button.setTextColor(color);
        }
    }

    void clearRadioButtons(AppCompatRadioButton... buttons) {
        for (AppCompatRadioButton button : buttons) {
            button.setChecked(false);
        }
    }

    void handleTimeDisplay(boolean enabled, EditText... texts) {
        int color = (enabled) ? ContextCompat.getColor(mContext, android.R.color.black) :
                ContextCompat.getColor(mContext, R.color.app_gray);

        int editColor = (enabled) ? ContextCompat.getColor(mContext, R.color.bright_app_green) :
                ContextCompat.getColor(mContext, R.color.app_gray);

        for (final EditText text : texts) {
            text.setEnabled(enabled);
            text.setTextColor(editColor);
        }

        ((TextView) llTime.getChildAt(0)).setTextColor(color);
        ((TextView) llTime.getChildAt(2)).setTextColor(color);

        if (enabled) {
            final EditText hours = texts[0];
            final EditText mins = texts[1];

            hours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (TextUtils.isEmpty(hours.getText().toString())) {
                            hours.setText(Integer.toString(Constants.HOURS_MAX));
                        }
                        modelSanityCheck();
                        getRealmInstance().beginTransaction();
                        int minsVal = (TextUtils.isEmpty(mins.getText().toString())) ? Constants.ZERO :
                                Integer.parseInt(mins.getText().toString());

                        int hoursInt = Integer.parseInt(hours.getText().toString());
                        int hoursVal = (rbAm.isChecked()) ? hoursInt : (hoursInt == Constants.HOURS_MAX) ?
                                hoursInt : hoursInt + Constants.HOURS_MAX;

                        for (DateField field : mCountable.anchorDates) {
                            field.date = CalendarUtils.setNotificationDate(field.date,
                                    hoursVal, minsVal);
                        }
                        getRealmInstance().commitTransaction();
                    }
                }
            });

            hours.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (ComparisonUtils.isNumber(s.toString()) && Integer.parseInt(s.toString()) > Constants.HOURS_MAX) {
                        hours.setText(Constants.EMPTY_STRING);
                    }
                }
            });


            mins.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (ComparisonUtils.isNumber(s.toString()) && Integer.parseInt(s.toString()) > Constants.MINS_MAX) {
                        mins.setText(Constants.EMPTY_STRING);
                    }
                }
            });
            mins.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (TextUtils.isEmpty(mins.getText().toString())) {
                            mins.setText(Constants.TWO_ZEROES);
                        } else if (mins.getText().toString().length() == 1) {
                            mins.setText(Constants.ZERO_STRING + mins.getText().toString());
                        }
                        modelSanityCheck();
                        getRealmInstance().beginTransaction();
                        int hoursVal = (TextUtils.isEmpty(hours.getText().toString())) ? Constants.ZERO :
                                Integer.parseInt(hours.getText().toString());

                        for (DateField field : mCountable.anchorDates) {
                            field.date = CalendarUtils.setNotificationDate(field.date, hoursVal,
                                    Integer.parseInt(mins.getText().toString()));
                        }
                        getRealmInstance().commitTransaction();
                    }
                }
            });
        }
    }
}
