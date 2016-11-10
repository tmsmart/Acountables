package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CalendarUtils;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.custom_view.week_view.DateRepeatAspect;
import group.g203.countables.custom_view.week_view.DateRepeatPresenter;
import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import group.g203.countables.path.detail.view.AccountableFragment;
import group.g203.countables.path.detail.view.AccountableView;
import io.realm.Realm;
import io.realm.RealmList;

public class AccountablePresenter implements BasePresenter, DateRepeatPresenter {

    Realm mRealm;
    AccountableView mAccountableView;
    Context mContext;
    SwitchCompat mSwitch;
    LinearLayout mIsSetLayout;
    TextView tvDelete;
    TextView tvEdit;
    DateRepeatAspect mAspect;
    Countable mCountable;
    ArrayList<LinearLayout> mDayLayouts;

    @Override
    public void displayToast(String message) {

    }

    @Override
    public void bindModels() {
        getRealmInstance().beginTransaction();
        mCountable = getRealmInstance().where(Countable.class).equalTo(Constants.INDEX,
                ((AccountableFragment) mAccountableView).getArguments().getInt(Constants.COUNTABLE_INDEX)).findFirst();
        getRealmInstance().commitTransaction();
    }

    @Override
    public void unbindModels() {

    }

    @Override
    public void bindViews(BaseView... views) {
        mAccountableView = (AccountableView) views[0];
        mAspect = (DateRepeatAspect) views[1];
        mContext = mAspect.getContext();

        mSwitch = ((AccountableFragment) mAccountableView).mSwitch;
        mIsSetLayout = ((AccountableFragment) mAccountableView).mIsSetLayout;
        tvDelete = ((AccountableFragment) mAccountableView).tvDelete;
        tvEdit = ((AccountableFragment) mAccountableView).tvEdit;

        mDayLayouts = new ArrayList<>();
        mDayLayouts.add(mAspect.llDayOne);
        mDayLayouts.add(mAspect.llDayTwo);
        mDayLayouts.add(mAspect.llDayThree);
        mDayLayouts.add(mAspect.llDayFour);
        mDayLayouts.add(mAspect.llDayFive);
        mDayLayouts.add(mAspect.llDaySix);
        mDayLayouts.add(mAspect.llDaySeven);

        bindModels();
        setMonth();
        setDays();
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

    @Override
    public void setActiveAspect() {
        modelSanityCheck();
        getRealmInstance().beginTransaction();
        mCountable.isAccountable = true;

        handleRadioButtonsState(true,
                mAspect.rbCustom, mAspect.rbDaily, mAspect.rbWeekly);
        setRepeatOptionClicks();
        getRealmInstance().commitTransaction();
    }

    @Override
    public void setReadOnlyAspect(boolean makeUnaccountable) {
        if (makeUnaccountable) {
            modelSanityCheck();
            getRealmInstance().beginTransaction();
            mCountable.isAccountable = false;
            getRealmInstance().commitTransaction();

            clearRadioButtons(mAspect.rbCustom, mAspect.rbDaily, mAspect.rbWeekly);
            ((TextView) mAspect.llRepeat.getChildAt(1)).setText(Constants.ZERO_STRING);

        }

        handleMonthDisplay(false);
        handleRadioButtonsState(false, mAspect.rbCustom, mAspect.rbDaily, mAspect.rbWeekly);
        handleRepeatDisplay(false);
        handleDayClicks(false);
    }

    void styleReadData() {
        modelSanityCheck();
        if (mCountable.dayRepeater == Constants.ONE) {
            mAspect.rbDaily.setChecked(true);
        } else if (mCountable.dayRepeater == CalendarUtils.WEEK_LENGTH) {
            mAspect.rbWeekly.setChecked(true);
        } else {
            mAspect.rbCustom.setChecked(true);
            showReadOnlyRepeatDisplay();
        }

        if (!CollectionUtils.isEmpty(mCountable.anchorDates, true)) {
            int selectedColor = ContextCompat.getColor(mContext, R.color.darkest_app_green);
            int color = ContextCompat.getColor(mContext, R.color.fallback_gray);
            mAspect.tvMonth.setText(CalendarUtils.returnEnglishMonth(mCountable.anchorDates.first().date));
            mAspect.tvMonth.setTextColor(selectedColor);

            ArrayList<Pair<Date, Pair<String, String>>> dateList = CalendarUtils.dateRepeatAspectList(mCountable.anchorDates.first().date);
            HashSet<Pair<String, String>> anchorDatePairs = CalendarUtils.anchorDatePairs(mCountable.anchorDates);

            int i = 0;
            for (LinearLayout layout : mDayLayouts) {
                Pair<Date, Pair<String, String>> datePair = dateList.get(i);
                boolean isSelectedDate = anchorDatePairs.contains(datePair.second);

                if (isSelectedDate) {
                    layout.setBackgroundResource(R.drawable.darkest_green_border);
                }

                LinearLayout dayOfTheWeekSpace = (LinearLayout) layout.getChildAt(0);
                TextView dayOfTheWeek = (TextView) dayOfTheWeekSpace.getChildAt(0);
                dayOfTheWeek.setTextColor((isSelectedDate) ? selectedColor : color);
                dayOfTheWeek.setText(datePair.second.first);

                LinearLayout daySpace = (LinearLayout) layout.getChildAt(1);
                TextView day = (TextView) daySpace.getChildAt(0);
                day.setTextColor((isSelectedDate) ? selectedColor : color);
                day.setText(datePair.second.second);

                layout.setTag(R.id.dateId, datePair.first);
                layout.setTag(R.id.selectedId, false);
                i++;
            }
        }
    }

    @Override
    public void setRepeatOptionClicks() {
        if (mCountable.isAccountable) {
            mAspect.rbDaily.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modelSanityCheck();
                    getRealmInstance().beginTransaction();
                    mCountable.dayRepeater = Constants.ONE;
                    getRealmInstance().commitTransaction();
                    selectAllForDaily();
                    handleRepeatDisplay(false);
                }
            });

            mAspect.rbWeekly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modelSanityCheck();
                    getRealmInstance().beginTransaction();
                    mCountable.dayRepeater = CalendarUtils.WEEK_LENGTH;
                    getRealmInstance().commitTransaction();
                    unselectAllForDaily();
                    handleRepeatDisplay(false);
                    handleDayClicks(true);
                }
            });

            mAspect.rbCustom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unselectAllForDaily();
                    handleRepeatDisplay(true);
                    handleDayClicks(true);
                }
            });
        } else {
            mAspect.rbCustom.setOnClickListener(null);
            mAspect.rbWeekly.setOnClickListener(null);
            mAspect.rbDaily.setOnClickListener(null);
        }
    }

    @Override
    public void setMonth() {
        mAspect.tvMonth.setText(CalendarUtils.returnEnglishMonth(new Date()));
    }

    @Override
    public void setDays() {
        ArrayList<Pair<Date, Pair<String, String>>> dateList;
        if (CollectionUtils.isEmpty(mCountable.anchorDates, true)) {
            dateList = CalendarUtils.dateRepeatAspectList(new Date());
        } else {
            dateList = CalendarUtils.dateRepeatAspectList(mCountable.anchorDates.first().date);
        }

        setDaysOnView(mDayLayouts, dateList);
    }

    @Override
    public void setRepeatDay() {

    }

    @Override
    public void setRepeatDayWatcher() {

    }

    void handleMonthDisplay(boolean enabled) {
        int color = (enabled) ? ContextCompat.getColor(mContext, R.color.app_green) :
                ContextCompat.getColor(mContext, R.color.fallback_gray);
        mAspect.tvMonth.setTextColor(color);
    }

    public void handleDisplay() {
        modelSanityCheck();
        setReadOnlyAspect(false);

        if (!mCountable.isAccountable) {
            mIsSetLayout.setVisibility(View.GONE);
            setSwitch();
        } else {
            mSwitch.setVisibility(View.GONE);
            mIsSetLayout.setVisibility(View.VISIBLE);
            styleReadData();
            setEditClick();
            setDeleteClick();
        }
    }

    public void setSwitch() {
        mSwitch.setVisibility(View.VISIBLE);
        mSwitch.setText(mContext.getString(R.string.enable_accountable));
        mSwitch.setChecked(false);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    modelSanityCheck();
                    getRealmInstance().beginTransaction();
                    mCountable.isAccountable = true;
                    getRealmInstance().commitTransaction();
                    setActiveAspect();
                } else {
                    modelSanityCheck();
                    getRealmInstance().beginTransaction();
                    mCountable.isAccountable = false;
                    mCountable.anchorDates = null;
                    mCountable.dayRepeater = 0;
                    getRealmInstance().commitTransaction();
                    setReadOnlyAspect(true);
                }
            }
        });
    }

    void handleDayClicks(boolean enabled) {
        setDaysOnClick(mDayLayouts, enabled);
    }

    public void setDeleteClick() {

    }

    public void setEditClick() {

    }

    void modelSanityCheck() {
        if (mCountable == null) {
            bindModels();
        }
    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

    void handleSelectAllDays(boolean select) {
        int color = (select) ? ContextCompat.getColor(mContext, R.color.bright_app_green) :
                ContextCompat.getColor(mContext, R.color.fallback_gray);

        int background = (select) ? R.drawable.bright_green_border : R.drawable.gray_border;

        for (LinearLayout layout : mDayLayouts) {
            layout.setBackgroundResource(background);

            LinearLayout dayOfTheWeekSpace = (LinearLayout) layout.getChildAt(0);
            ((TextView) dayOfTheWeekSpace.getChildAt(0)).setTextColor(color);

            LinearLayout daySpace = (LinearLayout) layout.getChildAt(1);
            ((TextView) daySpace.getChildAt(0)).setTextColor(color);

            if (select) {
                layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                ArrayList<Date> dates = CalendarUtils.weekDates(new Date());

                modelSanityCheck();
                getRealmInstance().beginTransaction();
                mCountable.anchorDates = new RealmList<>();
                for (Date date : dates) {
                    DateField dateField = getRealmInstance().createObject(DateField.class);
                    dateField.date = date;
                    mCountable.anchorDates.add(dateField);
                }
                getRealmInstance().commitTransaction();
            } else {
                layout.setOnTouchListener(null);
                modelSanityCheck();
                getRealmInstance().beginTransaction();
                mCountable.anchorDates = null;
                getRealmInstance().commitTransaction();
            }
        }
    }

    void selectAllForDaily() {
        handleMonthDisplay(true);
        handleSelectAllDays(true);
    }

    void unselectAllForDaily() {
        handleMonthDisplay(true);
        handleSelectAllDays(false);
    }

    void setDaysOnView(ArrayList<LinearLayout> layouts, ArrayList<Pair<Date, Pair<String, String>>> dateList) {
        int i = 0;
        for (LinearLayout layout : layouts) {
            Pair<Date, Pair<String, String>> datePair = dateList.get(i);
            LinearLayout dayOfTheWeekSpace = (LinearLayout) layout.getChildAt(0);
            TextView dayOfTheWeek = (TextView) dayOfTheWeekSpace.getChildAt(0);
            dayOfTheWeek.setText(datePair.second.first);

            LinearLayout daySpace = (LinearLayout) layout.getChildAt(1);
            TextView day = (TextView) daySpace.getChildAt(0);
            day.setText(datePair.second.second);

            layout.setTag(R.id.dateId, datePair.first);
            layout.setTag(R.id.selectedId, false);
            i++;
        }
    }

    void setDaysOnClick(ArrayList<LinearLayout> layouts, boolean enabled) {
        for (final LinearLayout layout : layouts) {
            if (!enabled) {
                layout.setOnClickListener(null);

                int color = ContextCompat.getColor(mContext, R.color.fallback_gray);

                int background = R.drawable.gray_border;

                layout.setBackgroundResource(background);

                LinearLayout dayOfTheWeekSpace = (LinearLayout) layout.getChildAt(0);
                ((TextView) dayOfTheWeekSpace.getChildAt(0)).setTextColor(color);

                LinearLayout daySpace = (LinearLayout) layout.getChildAt(1);
                ((TextView) daySpace.getChildAt(0)).setTextColor(color);
            } else {
                int color = ContextCompat.getColor(mContext, android.R.color.black);

                int background = R.drawable.gray_border;

                layout.setBackgroundResource(background);

                LinearLayout dayOfTheWeekSpace = (LinearLayout) layout.getChildAt(0);
                ((TextView) dayOfTheWeekSpace.getChildAt(0)).setTextColor(color);

                LinearLayout daySpace = (LinearLayout) layout.getChildAt(1);
                ((TextView) daySpace.getChildAt(0)).setTextColor(color);

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean selectedTag = (boolean) layout.getTag(R.id.selectedId);
                        int color = (selectedTag == false) ? ContextCompat.getColor(mContext, R.color.bright_app_green) :
                                ContextCompat.getColor(mContext, android.R.color.black);

                        int background = (selectedTag == false) ? R.drawable.bright_green_border :
                                R.drawable.gray_border;

                        layout.setBackgroundResource(background);

                        LinearLayout dayOfTheWeekSpace = (LinearLayout) layout.getChildAt(0);
                        ((TextView) dayOfTheWeekSpace.getChildAt(0)).setTextColor(color);

                        LinearLayout daySpace = (LinearLayout) layout.getChildAt(1);
                        ((TextView) daySpace.getChildAt(0)).setTextColor(color);

                        modelSanityCheck();
                        getRealmInstance().beginTransaction();
                        if (selectedTag == false) {
                            if (mCountable.anchorDates == null) {
                                mCountable.anchorDates = new RealmList<>();
                            }
                            DateField date = getRealmInstance().createObject(DateField.class);
                            date.date = (Date) layout.getTag(R.id.dateId);
                            mCountable.anchorDates.add(date);
                            layout.setTag(R.id.selectedId, true);
                            layout.setTag(R.id.dateFieldId, date);
                        } else {
                            layout.setTag(R.id.selectedId, false);
                            if (mCountable.anchorDates != null && layout.getTag(R.id.dateFieldId) != null) {
                                mCountable.anchorDates.remove(layout.getTag(R.id.dateFieldId));
                            }
                        }
                        getRealmInstance().commitTransaction();
                    }
                });
            }
        }
    }

    void handleRadioButtonsState(boolean enabled, AppCompatRadioButton... buttons) {
        int color = (enabled) ? ContextCompat.getColor(mContext, android.R.color.black) :
                ContextCompat.getColor(mContext, R.color.app_gray);

        for (AppCompatRadioButton button : buttons) {
            button.setEnabled(enabled);
            button.setTextColor(color);
        }

        handleRepeatDisplay(false);
    }

    void clearRadioButtons(AppCompatRadioButton... buttons) {
        for (AppCompatRadioButton button : buttons) {
            button.setChecked(false);
        }
    }

    void handleRepeatDisplay(boolean enabled) {
        final EditText etRepeat = ((EditText) mAspect.llRepeat.getChildAt(1));

        int colorInt = (enabled) ?
                ContextCompat.getColor(mContext, android.R.color.black) :
                ContextCompat.getColor(mContext, R.color.app_gray);

        int editColorInt = (enabled) ?
                ContextCompat.getColor(mContext, R.color.bright_app_green) :
                ContextCompat.getColor(mContext, R.color.app_gray);

        if (enabled) {
            mAspect.llRepeat.setVisibility(View.VISIBLE);
            ((TextView) mAspect.llRepeat.getChildAt(0)).setTextColor(colorInt);
            ((TextView) mAspect.llRepeat.getChildAt(2)).setTextColor(colorInt);

            etRepeat.setTextColor(editColorInt);
            etRepeat.setEnabled(enabled);
            etRepeat.setVisibility(View.VISIBLE);

            etRepeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etRepeat.setSelection((etRepeat.getText().length()));
                }
            });
            etRepeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (TextUtils.isEmpty(etRepeat.getText().toString())) {
                            etRepeat.setText(Constants.ZERO_STRING);
                        }
                        modelSanityCheck();
                        getRealmInstance().beginTransaction();
                        mCountable.dayRepeater = Integer.parseInt(etRepeat.getText().toString());
                        getRealmInstance().commitTransaction();
                    }
                }
            });
        } else {
            mAspect.llRepeat.setVisibility(View.GONE);
        }
    }

    void showReadOnlyRepeatDisplay() {
        int editColorInt = ContextCompat.getColor(mContext, R.color.darkest_app_green);
        int colorInt = ContextCompat.getColor(mContext, R.color.fallback_gray);
        final EditText etRepeat = ((EditText) mAspect.llRepeat.getChildAt(1));

        mAspect.llRepeat.setVisibility(View.VISIBLE);

        etRepeat.setEnabled(false);
        etRepeat.setTextColor(editColorInt);
        ((TextView) mAspect.llRepeat.getChildAt(0)).setTextColor(colorInt);
        ((TextView) mAspect.llRepeat.getChildAt(2)).setTextColor(colorInt);
    }
}
