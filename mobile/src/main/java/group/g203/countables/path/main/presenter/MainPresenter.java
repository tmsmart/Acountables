package group.g203.countables.path.main.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.BaseDialogManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CalendarUtils;
import group.g203.countables.base.utils.DisplayUtils;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.base.view.ItemTouchHelperCallback;
import group.g203.countables.base.view.OnStartDragListener;
import group.g203.countables.custom_view.loading_view.LoadingAspect;
import group.g203.countables.custom_view.loading_view.LoadingPresenter;
import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import group.g203.countables.model.TempCountable;
import group.g203.countables.path.main.view.CountableAdapter;
import group.g203.countables.path.main.view.CreditsDialog;
import group.g203.countables.path.main.view.InfoDialog;
import group.g203.countables.path.main.view.MainActivity;
import group.g203.countables.path.main.view.MainView;
import group.g203.countables.path.main.view.SortDialog;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainPresenter implements BasePresenter, CreditsDialogPresenter, InfoDialogPresenter,
        SortDialogPresenter, LoadingPresenter, CountableViewHolderPresenter, OnStartDragListener {

    private final static String NAME = "name";
    private final static String INDEX = "index";
    private final static String EMPTY_DATE = "-- / -- / --";
    private final static String TIMES_COMPLETED = "timesCompleted";
    private final static String LAST_MODIFIED = "lastModified";
    private final static String MAX_NAME_PREFIX = "Countable name must be ";
    private final static String MAX_NAME_SUFFIX = " characters or less";
    private final static String NAME_MUST_BE_NONEMPTY = "Countable name cannot be empty";
    private final static String COUNTABLE_CREATED = "Countable created successfully";
    private final static String COUNTABLE_DELETED = "Countable deleted successfully";
    private final static String COUNTABLE_CREATE_REVERTED = "Countable creation reverted successfully";
    private final static String COUNTABLE_DELETE_REVERTED = "Countable deletion reverted successfully";
    private final static int AZ = 0;
    private final static int ZA = 1;
    private final static int RECENTLY_UPDATED = 2;
    private final static int LEAST_RECENTLY_UPDATED = 3;
    private final static int MOST_COMPLETED = 4;
    private final static int LEAST_COMPLTED = 5;
    private final static int EMPTY_ICON_DIMEN = 120;
    private final static int MAX_COUNTABLE_NAME_CHARS = 86;

    Realm mRealm;
    MainView mMainView;
    View mView;
    LoadingAspect mLoadingAspect;
    RecyclerView mCountablesRv;
    CountableAdapter mAdapter;
    ItemTouchHelper mTouchHelper;
    InfoDialog mInfoDialog;
    CreditsDialog mCreditsDialog;
    SortDialog mSortDialog;
    EditText mCountableField;
    ImageView mAddCountable;
    Context mContext;

    @Override
    public void bindModels() {

    }

    @Override
    public void unbindModels() {

    }

    @Override
    public void bindViews(BaseView... views) {
        mMainView = (MainView) views[0];
        mView = ((MainActivity) mMainView).mView;

        mLoadingAspect = ((MainActivity) mMainView).mLoadingAspect;
        mLoadingAspect.setPresenter(((MainActivity) mMainView).getPresenter());
        mContext = mLoadingAspect.getContext();

        mCountablesRv = ((MainActivity) mMainView).mCountablesRv;

        mAddCountable = ((MainActivity) mMainView).mAddCountable;
        mCountableField = ((MainActivity) mMainView).mCountableField;
        assignCountableTextWatcher();
    }

    @Override
    public void unbindViews() {

    }

    @Override
    public void unbindModelsAndViews() {
        unbindModels();
        unbindViews();
    }

    @Override
    public void displaySnackbarMessage(String errorMessage) {
        DisplayUtils.displaySimpleSnackbar(mView, errorMessage, Snackbar.LENGTH_SHORT, null);
    }

    @Override
    public void setCreditsDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setCreditsDialogPositiveButton(AlertDialog.Builder builder, String buttonText) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
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
    public void displayToast(String message) {
        DisplayUtils.displayToast(mContext, message, Toast.LENGTH_SHORT);
    }

    @Override
    public void setSortDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setSortDialogItems(final SortDialog sortDialog, final AlertDialog.Builder builder, int arrayResource) {
        builder.setSingleChoiceItems(arrayResource, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sortDialog.dismiss();
                switch (which) {
                    case AZ:
                        sortAZ();
                        break;
                    case ZA:
                        sortZA();
                        break;
                    case RECENTLY_UPDATED:
                        sortByMostRecentUpdate();
                        break;
                    case LEAST_RECENTLY_UPDATED:
                        sortByLeastRecentUpdate();
                        break;
                    case MOST_COMPLETED:
                        sortByCompletedMost();
                        break;
                    case LEAST_COMPLTED:
                        sortByCompletedLeast();
                        break;
                }
            }
        });
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
        mLoadingAspect.mEmptyMessage.setText(message);
    }

    @Override
    public void setEmptyIcon(int iconResource) {
        Picasso.with(mContext).load(iconResource).resize(EMPTY_ICON_DIMEN, EMPTY_ICON_DIMEN).into(mLoadingAspect.mEmptyIcon);
    }

    @Override
    public void handleReminderIcon(ImageView imageView, Countable countable) {
        int colorResource = R.color.fallback_gray;
        if (countable.isReminderEnabled) {
            colorResource = R.color.bright_app_green;
        }
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), colorResource));
    }

    @Override
    public void handleAccountableIcon(ImageView imageView, Countable countable) {
        int colorResource = R.color.fallback_gray;
        if (countable.isAccountable) {
            colorResource = R.color.bright_app_green;
        }
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), colorResource));
    }

    @Override
    public void setLastCompletedText(TextView textView, Countable countable) {
        String lastCompletedValue = (countable.lastModified != null) ? CalendarUtils.return_MMDDYYYY_Format(countable.lastModified) : EMPTY_DATE;
        textView.setText(lastCompletedValue);
    }

    @Override
    public void setCompletedCount(TextView textView, Countable countable) {
        textView.setText(Integer.toString(countable.timesCompleted));
    }

    @Override
    public void setCountableTitle(TextView textView, String title) {
        textView.setText(title);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mTouchHelper.startDrag(viewHolder);
    }

    public void handleInitialContentDisplay() {
        displayLoading();

        RealmResults<Countable> countables = getRealmInstance().where(Countable.class).
                findAllAsync().sort(INDEX, Sort.ASCENDING);

        setupCountablesRv(countables);

        if (countables.size() > 0) {
            displayContent();
        } else {
            displayEmptyView();
        }
    }

    void setupCountablesRv(OrderedRealmCollection<Countable> countables) {
        mCountablesRv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CountableAdapter(countables, this);
        mCountablesRv.setAdapter(mAdapter);
        mCountablesRv.setHasFixedSize(true);

        ItemTouchHelper.Callback itemTouchCallback = new ItemTouchHelperCallback(mAdapter);
        mTouchHelper = new ItemTouchHelper(itemTouchCallback);
        mTouchHelper.attachToRecyclerView(mCountablesRv);
    }

    public void displayCreditsDialog(FragmentManager fm) {
        mCreditsDialog = CreditsDialog.getInstance(CreditsDialog.TAG);
        BaseDialogManager.displayFragmentDialog(mCreditsDialog, CreditsDialog.TAG, fm);
    }

    public void displayInfoDialog(FragmentManager fm) {
        mInfoDialog = InfoDialog.getInstance(InfoDialog.TAG, Constants.MAIN_INFO);
        BaseDialogManager.displayFragmentDialog(mInfoDialog, InfoDialog.TAG, fm);
    }

    public void displaySortDialog(FragmentManager fm) {
        if (mLoadingAspect.mEmptyIcon.getVisibility() == View.VISIBLE ||
                mLoadingAspect.mEmptyMessage.getVisibility() == View.VISIBLE) {
            displayToast(mContext.getString(R.string.nothing_to_sort));
        } else {
            mSortDialog = SortDialog.getInstance(SortDialog.TAG);
            BaseDialogManager.displayFragmentDialog(mSortDialog, SortDialog.TAG, fm);
        }
    }

    void sortAZ() {
        sort(NAME, Sort.ASCENDING, mContext.getResources().getStringArray(R.array.sort_options)[0]);
    }

    void sortZA() {
        sort(NAME, Sort.DESCENDING, mContext.getResources().getStringArray(R.array.sort_options)[1]);
    }

    void sortByMostRecentUpdate() {
        sort(LAST_MODIFIED, Sort.DESCENDING, mContext.getResources().getStringArray(R.array.sort_options)[2]);
    }


    void sortByLeastRecentUpdate() {
        sort(LAST_MODIFIED, Sort.ASCENDING, mContext.getResources().getStringArray(R.array.sort_options)[3]);
    }


    void sortByCompletedMost() {
        sort(TIMES_COMPLETED, Sort.DESCENDING, mContext.getResources().getStringArray(R.array.sort_options)[4]);
    }


    void sortByCompletedLeast() {
        sort(TIMES_COMPLETED, Sort.ASCENDING, mContext.getResources().getStringArray(R.array.sort_options)[5]);
    }

    void sort(final String fieldName, final Sort sortOrder, final String sortName) {
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Countable> countables = realm.where(Countable.class).findAll().sort(fieldName, sortOrder);
                displayLoading();
                assignCountableIndices(countables);
                if (mAdapter == null) {
                    setupCountablesRv(countables);
                }
                mAdapter.setData(countables);
                mAdapter.notifyDataSetChanged();
                displayToast(mContext.getString(R.string.sorted_prefix) + Constants.SPACE + sortName);
                displayContent();
            }
        });
    }

    public static void assignCountableIndices(List<Countable> countables) {
        int index = 0;
        for (Countable countable : countables) {
            countable.index = index;
            index++;
        }
    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

    void assignCountableTextWatcher() {
        mCountableField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    handleAddButtonConfig(false);
                } else if (s.length() > 0) {
                    handleAddButtonConfig(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_COUNTABLE_NAME_CHARS) {
                    displaySnackbarMessage(MAX_NAME_PREFIX + MAX_COUNTABLE_NAME_CHARS + MAX_NAME_SUFFIX);
                    int maxLength = (mCountableField.getText().toString().length() > MAX_COUNTABLE_NAME_CHARS) ? MAX_COUNTABLE_NAME_CHARS : mCountableField.getText().toString().length();
                    String maxString = mCountableField.getText().toString().substring(0, maxLength - 1);
                    mCountableField.setText(maxString);
                    mCountableField.setSelection(mCountableField.getText().length());
                }
            }
        });
    }

    void handleAddButtonConfig(boolean enable) {
        if (enable) {
            mAddCountable.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.enabled_add_countable_border));
            mAddCountable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createCountable();
                }
            });
        } else {
            mAddCountable.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.disabled_add_countable_border));
            mAddCountable.setOnClickListener(null);
        }
    }

    void createCountable() {
        final String countableName = mCountableField.getText().toString();
        if (countableName.trim().isEmpty()) {
            displaySnackbarMessage(NAME_MUST_BE_NONEMPTY);
        } else if (countableName.length() > MAX_COUNTABLE_NAME_CHARS) {
            displaySnackbarMessage(MAX_NAME_PREFIX + MAX_COUNTABLE_NAME_CHARS + MAX_NAME_SUFFIX);
        } else {
            displayLoading();
            getRealmInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final Countable addedCountable = realm.createObject(Countable.class);
                    addedCountable.name = countableName;
                    addedCountable.isAccountable = false;
                    addedCountable.isReminderEnabled = false;
                    addedCountable.loggedDates = null;
                    addedCountable.lastModified = null;
                    addedCountable.timesCompleted = 0;

                    RealmResults<Countable> countables = realm.where(Countable.class).findAll();
                    addedCountable.index = countables.size() - 1;

                    mCountableField.setText(Constants.EMPTY_STRING);
                    mCountableField.clearFocus();

                    mAdapter.setData(countables);
                    mAdapter.notifyDataSetChanged();

                    displayContent();
                    mCountablesRv.smoothScrollToPosition(addedCountable.index);

                    DisplayUtils.displayActionSnackbar(mView, COUNTABLE_CREATED, Constants.UNDO, Snackbar.LENGTH_LONG,
                            ContextCompat.getColor(mContext, R.color.bright_app_green), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    revertCountableCreation(addedCountable);
                                }
                            }, null);
                }
            });
        }
    }

    void revertCountableCreation(final Countable countable) {
        displayLoading();
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                countable.deleteFromRealm();

                RealmResults<Countable> countables = realm.where(Countable.class).findAll();

                mAdapter.setData(countables);
                mAdapter.notifyDataSetChanged();

                if (countables.size() > 0) {
                    displayContent();
                    mCountablesRv.smoothScrollToPosition(countables.size() - 1);
                } else {
                    displayEmptyView();
                }

                DisplayUtils.displaySimpleSnackbar(mView, COUNTABLE_CREATE_REVERTED, Snackbar.LENGTH_SHORT, null);
            }
        });
    }

    public void deleteCountableViaSwipe(final Countable countable) {
        getRealmInstance().beginTransaction();
        final TempCountable tempCountable = TempCountable.createTempCountable(countable);
        countable.deleteFromRealm();
        mAdapter.setData(getRealmInstance().where(Countable.class).findAll());
        if (getRealmInstance().where(Countable.class).findAll().size() == 0) {
            displayEmptyView();
        }
        getRealmInstance().commitTransaction();

        DisplayUtils.displayActionSnackbar(mView, COUNTABLE_DELETED, Constants.UNDO, Snackbar.LENGTH_LONG,
                ContextCompat.getColor(mContext, R.color.bright_app_green), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            getRealmInstance().beginTransaction();
                            assignCountableIndices(getRealmInstance().where(Countable.class).findAll().sort(INDEX, Sort.ASCENDING));
                            getRealmInstance().commitTransaction();
                        } else {
                            displayLoading();

                            getRealmInstance().beginTransaction();
                            RealmList<DateField> dates = new RealmList<>();
                            for (Date date : tempCountable.loggedDates) {
                                DateField dateField = getRealmInstance().createObject(DateField.class);
                                dateField.date = date;
                                dates.add(dateField);
                                getRealmInstance().copyToRealm(dateField);
                            }

                            Countable countable = getRealmInstance().createObject(Countable.class);
                            countable.name = tempCountable.name;
                            countable.index = tempCountable.index;
                            countable.isAccountable = tempCountable.isAccountable;
                            countable.isReminderEnabled = tempCountable.isReminderEnabled;
                            countable.loggedDates = dates;
                            countable.lastModified = tempCountable.lastModified;
                            countable.timesCompleted = tempCountable.timesCompleted;

                            RealmResults<Countable> countables = getRealmInstance().where(Countable.class).findAll().sort(INDEX, Sort.ASCENDING);
                            getRealmInstance().commitTransaction();

                            mAdapter.setData(countables);
                            mAdapter.notifyDataSetChanged();

                            mCountablesRv.smoothScrollToPosition(countable.index);
                            DisplayUtils.displaySimpleSnackbar(mView, COUNTABLE_DELETE_REVERTED, Snackbar.LENGTH_SHORT, null);
                            displayContent();
                        }
                    }
                });
    }

    public void reorderCountablesViaDrag(final Countable countable, final int fromPosition,
                                         final int toPosition, final boolean increaseIndices) {
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Countable> countables = realm.where(Countable.class).findAll().sort(INDEX, Sort.ASCENDING);
                if (increaseIndices) {
                    for (int i = toPosition; i <= fromPosition - 1; i++) {
                        countables.get(i).index = countables.get(i).index + 1;
                    }
                } else {
                    for (int i = fromPosition; i <= toPosition; i++) {
                        countables.get(i).index = countables.get(i).index - 1;
                    }
                }
                countable.index = toPosition;
                mAdapter.setData(countables);
            }
        });
    }
}
