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
import group.g203.countables.path.detail.view.AccountableFragment;
import group.g203.countables.path.detail.view.AccountableView;
import io.realm.Realm;

public class AccountablePresenter implements BasePresenter, DateRepeatPresenter {

    Realm mRealm;
    AccountableView mAccountableView;
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

        mSwitch = ((AccountableFragment)mAccountableView).mSwitch;
        mIsSetLayout = ((AccountableFragment)mAccountableView).mIsSetLayout;
        ivDelete = ((AccountableFragment)mAccountableView).ivDelete;
        ivEdit = ((AccountableFragment)mAccountableView).ivEdit;

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
