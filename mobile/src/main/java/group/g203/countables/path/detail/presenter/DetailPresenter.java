package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashSet;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.BaseDialogManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CalendarUtils;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.utils.DisplayUtils;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.custom_view.loading_view.LoadingAspect;
import group.g203.countables.custom_view.loading_view.LoadingPresenter;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.AccountableFragment;
import group.g203.countables.path.detail.view.DeleteDialog;
import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.detail.view.DetailView;
import group.g203.countables.path.detail.view.EditDialog;
import group.g203.countables.path.detail.view.EditTimeDialog;
import group.g203.countables.path.detail.view.ReminderFragment;
import group.g203.countables.path.detail.view.TimeLogFragment;
import group.g203.countables.path.main.presenter.InfoDialogPresenter;
import group.g203.countables.path.main.presenter.MainPresenter;
import group.g203.countables.path.main.view.InfoDialog;
import io.realm.Realm;
import io.realm.Sort;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DetailPresenter implements BasePresenter, InfoDialogPresenter, DeleteDialogPresenter,
        EditDialogPresenter, LoadingPresenter, DateViewHolderPresenter, TimeDialogPresenter,
        TimeSelectionDialogPresenter {

    private final static String COUNTABLE_DELETED = "Countable deleted successfully";
    private final static String COUNTABLE_EDITED = "Countable named edited successfully";

    public int mNavIndex = DetailActivity.TIME_LOG_INDEX;

    Realm mRealm;
    DetailView mDetailView;
    View mView;
    Context mContext;
    LoadingAspect mLoadingAspect;
    ProgressBar mInfoProgress;
    LinearLayout mAccountableLl;
    LinearLayout mTimeLogLl;
    LinearLayout mReminderLl;
    TextView mAccountableTv;
    ImageView mAccountableIv;
    TextView mReminderTv;
    ImageView mReminderIv;
    TextView mTimeLogTv;
    ImageView mTimeLogIv;
    Countable mCountable;
    InfoDialog mInfoDialog;
    DeleteDialog mDeleteDialog;
    EditDialog mEditDialog;

    public DetailPresenter() {}

    public DetailPresenter(int navIndex) {
        mNavIndex = navIndex;
    }

    @Override
    public void bindModels() {
        getRealmInstance().beginTransaction();
        Bundle bundle = ((DetailActivity) mDetailView).getIntent().getExtras();
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
        mDetailView = (DetailView) views[0];
        mView = ((DetailActivity) mDetailView).mView;
        mContext = mView.getContext();

        mAccountableLl = ((DetailActivity) mDetailView).llAccountable;
        mAccountableIv = ((DetailActivity) mDetailView).ivAccountable;
        mAccountableTv = ((DetailActivity) mDetailView).tvAccountable;
        mReminderLl = ((DetailActivity) mDetailView).llReminder;
        mReminderIv = ((DetailActivity) mDetailView).ivReminder;
        mReminderTv = ((DetailActivity) mDetailView).tvReminder;
        mTimeLogLl = ((DetailActivity) mDetailView).llTimeLog;
        mTimeLogIv = ((DetailActivity) mDetailView).ivTimeLog;
        mTimeLogTv = ((DetailActivity) mDetailView).tvTimeLog;

        mLoadingAspect = ((DetailActivity) mDetailView).mLoadingAspect;
        mInfoProgress = ((DetailActivity) mDetailView).mInfoProgress;
        bindModels();
    }

    @Override
    public void unbindViews() {

    }

    @Override
    public void unbindModelsAndViews() {

    }

    @Override
    public void displaySnackbarMessage(String errorMessage) {

    }

    @Override
    public void displayToast(String message) {

    }

    @Override
    public void setInfoDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setInfoDialogPositiveButton(AlertDialog.Builder builder, String buttonText) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setInfoDialogMessage(AlertDialog.Builder builder, String message) {
        builder.setMessage(message);
    }

    @Override
    public void setDeleteDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setDeleteDialogNegativeButton(AlertDialog.Builder builder, String buttonText) {
        builder.setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setDeleteDialogPositiveButton(AlertDialog.Builder builder, String buttonText,
                                              final DeleteDialog.DeleteCountableListener listener,
                                              final DeleteDialog deleteDialog) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDeleteCountableClick(deleteDialog);
            }
        });
    }

    @Override
    public void setEditDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setEditDialogNegativeButton(AlertDialog.Builder builder, String buttonText) {
        builder.setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setEditDialogPositiveButton(AlertDialog.Builder builder, String buttonText,
                                            final EditDialog.EditCountableListener listener, final EditDialog editDialog) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onEditCountableClick(editDialog);
            }
        });
    }

    public void displayInfoDialog(FragmentManager fm) {
        mInfoDialog = InfoDialog.getInstance(InfoDialog.TAG, Constants.DETAIL_INFO);
        BaseDialogManager.displayFragmentDialog(mInfoDialog, InfoDialog.TAG, fm);
    }

    public void displayDeleteDialog(FragmentManager fm) {
        mDeleteDialog = DeleteDialog.getInstance(DeleteDialog.TAG);
        BaseDialogManager.displayFragmentDialog(mDeleteDialog, DeleteDialog.TAG, fm);
    }

    public void displayEditDialog(FragmentManager fm) {
        mEditDialog = EditDialog.getInstance(EditDialog.TAG, mCountable.name);
        BaseDialogManager.displayFragmentDialog(mEditDialog, EditDialog.TAG, fm);
    }

    public void handleInitDisplay(int navIndex) {
        TextView tvTitle = ((TextView) mView.findViewById(R.id.tvCountableTitle));
        TextView tvCount = ((TextView) mView.findViewById(R.id.tvCompletedCount));

        mInfoProgress.setVisibility(View.VISIBLE);
        tvCount.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);

        if (mCountable == null) {
            bindModels();
        }

        tvTitle.setText(mCountable.name);
        setCompletedCount(Integer.toString(CalendarUtils.returnLoggedAndAccountableDates(mCountable.loggedDates,
                mCountable.accountableDates).size()));
        displayNavFragContent(navIndex);
        mDetailView.setTimeLogClick();
        mDetailView.setReminderClick();
        mDetailView.setAccountableClick();

        mInfoProgress.setVisibility(View.GONE);
        tvCount.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        mNavIndex = navIndex;
    }

    public void displayTimeLog(FragmentManager fm) {
        dismissKeyboard();
        displayLoading();
        String tag = TimeLogFragment.TAG;
        showFragmentContent(TimeLogFragment.getInstance(tag, getPassableIdOrIndex().first, getPassableIdOrIndex().second), tag, fm);
        handleBottomNavColorization(mTimeLogTv, mTimeLogIv, mReminderTv, mAccountableTv, mReminderIv, mAccountableIv);
        displayContent();
        mNavIndex = DetailActivity.TIME_LOG_INDEX;
    }

    public void displayAccountableView(FragmentManager fm) {
        dismissKeyboard();
        displayLoading();
        String tag = AccountableFragment.TAG;
        showFragmentContent(AccountableFragment.getInstance(tag, getPassableIdOrIndex().first, getPassableIdOrIndex().second), tag, fm);
        handleBottomNavColorization(mAccountableTv, mAccountableIv, mTimeLogTv, mReminderTv, mTimeLogIv, mReminderIv);
        displayContent();
        mNavIndex = DetailActivity.ACCOUNTABLE_INDEX;
    }

    public void displayReminderView(FragmentManager fm) {
        dismissKeyboard();
        displayLoading();
        String tag = ReminderFragment.TAG;
        showFragmentContent(ReminderFragment.getInstance(tag, getPassableIdOrIndex().first, getPassableIdOrIndex().second), tag, fm);
        handleBottomNavColorization(mReminderTv, mReminderIv, mTimeLogTv, mAccountableTv, mTimeLogIv, mAccountableIv);
        displayContent();
        mNavIndex = DetailActivity.REMINDER_INDEX;
    }

    public void setTimeLogClick(final FragmentManager fm) {
        mTimeLogLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTimeLog(fm);
            }
        });
    }

    public void setAccountableClick(final FragmentManager fm) {
        mAccountableLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAccountableView(fm);
            }
        });
    }

    public void setReminderClick(final FragmentManager fm) {
        mReminderLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayReminderView(fm);
            }
        });
    }

    public void displayNavFragContent(int navIndex) {
        switch (navIndex) {
            case DetailActivity.TIME_LOG_INDEX:
                mDetailView.displayTimeLog();
                break;
            case DetailActivity.ACCOUNTABLE_INDEX:
                mDetailView.displayAccountableView();
                break;
            case DetailActivity.REMINDER_INDEX:
                mDetailView.displayReminderView();
                break;
        }
    }

    void showFragmentContent(Fragment frag, String tag, FragmentManager fm) {
        Fragment fragment = (fm.findFragmentByTag(tag) == null) ? frag : fm.findFragmentByTag(tag);
        fm.beginTransaction().replace(R.id.frame, fragment, tag).commit();
    }

    void handleBottomNavColorization(View... views) {
        handleNavSelectionColor(views[0], views[1]);
        handleNavNonSelectionColors(views[2], views[3], views[4], views[5]);
    }

    public void deleteAndGoBack() {
        getRealmInstance().beginTransaction();
        mCountable.deleteFromRealm();
        MainPresenter.assignCountableIndices(getRealmInstance().where(Countable.class).findAll().sort(Constants.INDEX, Sort.ASCENDING));
        getRealmInstance().commitTransaction();

        DisplayUtils.displayToast(mContext, COUNTABLE_DELETED, Toast.LENGTH_SHORT);
        NavUtils.navigateUpFromSameTask(((DetailActivity) mDetailView));
    }

    public void renameCountable(String countableName) {
        getRealmInstance().beginTransaction();
        mCountable.name = countableName;
        getRealmInstance().commitTransaction();

        ((TextView) mView.findViewById(R.id.tvCountableTitle)).setText(countableName);
        DisplayUtils.displayToast(mContext, COUNTABLE_EDITED, Toast.LENGTH_SHORT);
    }

    public void setCompletedCount(String count) {
        String completionsText = mContext.getString(R.string.completed_count);
        String countText = completionsText + Constants.SPACE + count;

        final SpannableStringBuilder spanString = new SpannableStringBuilder(countText);
        final StyleSpan boldFont = new StyleSpan(android.graphics.Typeface.BOLD);

        spanString.setSpan(boldFont, countText.indexOf(count),
                countText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.app_green)), countText.indexOf(count),
                countText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        ((TextView) mView.findViewById(R.id.tvCompletedCount)).setText(spanString);
    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

    @Override
    public void displayLoading() {
        mLoadingAspect.mEmptyMessage.setVisibility(View.GONE);
        mLoadingAspect.mEmptyIcon.setVisibility(View.GONE);
        mLoadingAspect.mContent.setVisibility(View.GONE);

        mLoadingAspect.mIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayContent() {
        mLoadingAspect.mContent.setVisibility(View.VISIBLE);

        mLoadingAspect.mEmptyMessage.setVisibility(View.GONE);
        mLoadingAspect.mEmptyIcon.setVisibility(View.GONE);
        mLoadingAspect.mIndicator.setVisibility(View.GONE);
    }

    @Override
    public void displayEmptyView() {
        mLoadingAspect.mEmptyMessage.setVisibility(View.VISIBLE);
        mLoadingAspect.mEmptyIcon.setVisibility(View.VISIBLE);

        mLoadingAspect.mIndicator.setVisibility(View.GONE);
        mLoadingAspect.mContent.setVisibility(View.GONE);
    }

    @Override
    public void setEmptyMessage(String message) {
    }

    @Override
    public void setEmptyIcon(int iconResource) {
    }

    @Override
    public void setDateFormat(TextView textView, Date date) {
        textView.setText(CalendarUtils.returnEnglishFormat(date));
    }

    @Override
    public void handleDateColor(TextView textView) {
        Date date = CalendarUtils.englishToDate(textView.getText().toString());
        if (mCountable.isAccountable) {
            if (!CollectionUtils.isEmpty(mCountable.accountableDates, true)) {
                HashSet<String> dateList = CalendarUtils.getMonthDateYearDashedSet(mCountable.accountableDates);
                if (dateList.contains(CalendarUtils.getMonthDateYearDashedString(
                        CalendarUtils.englishToDate(textView.getText().toString())))) {
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.app_green));
                } else if (CalendarUtils.realmListToDateArrayList(mCountable.accountableDates).contains(date)) {
                    textView.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_dark));
                }
            }
        }
    }

    void handleNavSelectionColor(View... views) {
        int selectionColor = ContextCompat.getColor(mContext, R.color.dark_app_green);
        ((TextView) views[0]).setTextColor(selectionColor);
        ((ImageView) views[1]).setColorFilter(selectionColor);
    }

    void handleNavNonSelectionColors(View... views) {
        int iconColor = ContextCompat.getColor(mContext, R.color.fallback_gray);
        int textColor = ContextCompat.getColor(mContext, R.color.app_gray);
        ((TextView) views[0]).setTextColor(textColor);
        ((TextView) views[1]).setTextColor(textColor);
        ((ImageView) views[2]).setColorFilter(iconColor);
        ((ImageView) views[3]).setColorFilter(iconColor);
    }

    public void resetReminder(FragmentManager fm) {
        String tag = ReminderFragment.TAG;
        ReminderFragment fragment = (ReminderFragment) fm.findFragmentByTag(tag);
        if (fragment != null) {
            fragment.getPresenter().resetReminder();
        } else {
            DisplayUtils.displayToast(mContext, mContext.getString(R.string.countable_edit_error), Toast.LENGTH_SHORT);
        }
    }

    public void deleteReminder(FragmentManager fm) {
        String tag = ReminderFragment.TAG;
        ReminderFragment fragment = (ReminderFragment) fm.findFragmentByTag(tag);
        if (fragment != null) {
            fragment.getPresenter().onDeleteReminder();
        } else {
            DisplayUtils.displayToast(mContext, mContext.getString(R.string.countable_edit_error), Toast.LENGTH_SHORT);
        }
    }

    public void deleteAccountability(FragmentManager fm) {
        String tag = AccountableFragment.TAG;
        AccountableFragment fragment = (AccountableFragment) fm.findFragmentByTag(tag);
        if (fragment != null) {
            fragment.getPresenter().onDeleteAccountability();
        } else {
            DisplayUtils.displayToast(mContext, mContext.getString(R.string.countable_edit_error), Toast.LENGTH_SHORT);
        }
    }

    public void editAccountability(FragmentManager fm) {
        String tag = AccountableFragment.TAG;
        AccountableFragment fragment = (AccountableFragment) fm.findFragmentByTag(tag);
        if (fragment != null) {
            fragment.getPresenter().editAccountability();
        } else {
            DisplayUtils.displayToast(mContext, mContext.getString(R.string.countable_edit_error), Toast.LENGTH_SHORT);
        }
    }


    @Override
    public void setDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setDialogNegativeButton(AlertDialog.Builder builder, String buttonText) {
        builder.setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setDialogPositiveButton(AlertDialog.Builder builder, String buttonText, final EditTimeDialog.TimeDialogListener listener, final EditTimeDialog mDialog) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDialog.dialogCode == EditTimeDialog.DELETE_SCHEDULE) {
                    listener.onDeleteAccountableClick(mDialog);
                } else if (mDialog.dialogCode == EditTimeDialog.DELETE_REMINDER) {
                    listener.onDeleteReminderClick(mDialog);
                } else if (mDialog.dialogCode == EditTimeDialog.EDIT_SCHEDULE) {
                    listener.onEditAccountableClick(mDialog);
                } else if (mDialog.dialogCode == EditTimeDialog.EDIT_REMINDER) {
                    listener.onEditReminderClick(mDialog);
                }
            }
        });
    }

    @Override
    public void setNotificationTime(FragmentManager fm, int hour, int mins) {
        ReminderFragment fragment = (ReminderFragment) fm.findFragmentByTag(ReminderFragment.TAG);
        String minsVal = Integer.toString(mins);
        if (fragment != null) {
            if (hour < Constants.HOURS_MAX) {
                if (hour == Constants.ZERO) {
                    hour = Constants.HOURS_MAX;
                }
                fragment.tvAmPm.setText(mContext.getString(R.string.am));
            } else {
                if (hour > Constants.HOURS_MAX) {
                    hour = hour - Constants.HOURS_MAX;
                }
                fragment.tvAmPm.setText(mContext.getString(R.string.pm));
            }
            if (minsVal.length() == Constants.ONE) {
                minsVal = Constants.ZERO_STRING + minsVal;
            }
            fragment.tvMins.setText(minsVal);
            fragment.tvHours.setText(Integer.toString(hour));
        } else {
            DisplayUtils.displayToast(mContext, mContext.getString(R.string.countable_edit_error), Toast.LENGTH_SHORT);
        }

    }

    Pair<String, Integer> getPassableIdOrIndex() {
        Bundle bundle = ((DetailActivity) mDetailView).getIntent().getExtras();
        if (bundle.containsKey(Constants.COUNTABLE_ID)) {
            return new Pair<>(Constants.COUNTABLE_ID, bundle.getInt(Constants.COUNTABLE_ID));
        } else {
            return new Pair<>(Constants.COUNTABLE_INDEX, bundle.getInt(Constants.COUNTABLE_INDEX));
        }
    }

    void dismissKeyboard() {
        if (((DetailActivity) mDetailView).getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((DetailActivity) mDetailView).getCurrentFocus().getWindowToken(), 0);
        }
    }
}
