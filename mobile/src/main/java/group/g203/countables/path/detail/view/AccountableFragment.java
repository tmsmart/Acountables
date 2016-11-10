package group.g203.countables.path.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.custom_view.week_view.DateRepeatAspect;
import group.g203.countables.path.detail.presenter.AccountablePresenter;

public class AccountableFragment extends Fragment implements AccountableView {

    public static final String TAG = "AcctFrag";

    @Bind(R.id.switchView)
    public SwitchCompat mSwitch;
    @Bind(R.id.llIsSet)
    public LinearLayout mIsSetLayout;
    @Bind(R.id.tvDelete)
    public TextView tvDelete;
    @Bind(R.id.tvEdit)
    public TextView tvEdit;
    @Bind(R.id.dtrAspect)
    public DateRepeatAspect mAspect;
    public View mView;
    AccountablePresenter mPresenter;

    public static AccountableFragment getInstance(String tag, int countableIndex) {
        AccountableFragment frag = new AccountableFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DIALOG_TAG, tag);
        bundle.putInt(Constants.COUNTABLE_INDEX, countableIndex);
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
        mView = inflater.inflate(R.layout.accountable_layout, container, false);
        ButterKnife.bind(this, mView);
        setPresenter(new AccountablePresenter());
        handleDisplay();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void handleDisplay() {
        mPresenter.handleDisplay();
    }

    @Override
    public void onSetSwitch() {
        mPresenter.setSwitch();
    }

    @Override
    public void onSetDeleteClick() {
        mPresenter.setDeleteClick();
    }

    @Override
    public void onSetEditClick() {
        mPresenter.setEditClick();
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof AccountablePresenter) {
            mPresenter = (AccountablePresenter) presenter;
            mPresenter.bindViews(this, mAspect);
        } else {
            mPresenter.displaySnackbarMessage(getString(R.string.general_error));
        }
    }

    @Override
    public AccountablePresenter getPresenter() {
        return mPresenter;
    }
}
