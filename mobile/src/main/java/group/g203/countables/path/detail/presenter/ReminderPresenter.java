package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.custom_view.week_view.DateRepeatAspect;
import group.g203.countables.custom_view.week_view.DateRepeatPresenter;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.ReminderFragment;
import group.g203.countables.path.detail.view.ReminderView;
import io.realm.Realm;

public class ReminderPresenter implements BasePresenter, DateRepeatPresenter {

    Realm mRealm;
    ReminderView mReminderView;
    Context mContext;
    SwitchCompat mSwitch;
    LinearLayout mIsSetLayout;
    ImageView ivDelete;
    ImageView ivEdit;
    DateRepeatAspect mAspect;
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
        mAspect = (DateRepeatAspect) views[1];
        mContext = mAspect.getContext();

        mSwitch = ((ReminderFragment) mReminderView).mSwitch;
        mIsSetLayout = ((ReminderFragment) mReminderView).mIsSetLayout;
        ivDelete = ((ReminderFragment) mReminderView).ivDelete;
        ivEdit = ((ReminderFragment) mReminderView).ivEdit;

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

    @Override
    public void setActiveAspect() {

    }

    @Override
    public void setReadOnlyAspect() {

    }

    @Override
    public void setRepeatOptionClicks() {

    }

    @Override
    public void setMonth() {

    }

    @Override
    public void setDays() {

    }

    @Override
    public void setRepeatDay() {

    }

    @Override
    public void setRepeatDayWatcher() {

    }

    public void handleDisplay() {

    }

    public void setSwitch() {

    }

    public void setDeleteClick() {

    }

    public void setEditClick() {

    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}
