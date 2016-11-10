package group.g203.countables.path.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.path.detail.presenter.ReminderPresenter;

public class ReminderFragment extends Fragment implements ReminderView {

    public static final String TAG = "ReminderFrag";

    @Bind(R.id.switchView)
    public SwitchCompat mSwitch;
    @Bind(R.id.llIsSet)
    public LinearLayout mIsSetLayout;
    @Bind(R.id.llReminderInfo)
    public LinearLayout llReminderInfo;
    @Bind(R.id.tvDelete)
    public TextView tvDelete;
    @Bind(R.id.tvEdit)
    public TextView tvEdit;
    @Bind(R.id.llTime)
    public LinearLayout llTime;
    @Bind(R.id.rbAm)
    public AppCompatRadioButton rbAm;
    @Bind(R.id.rbPm)
    public AppCompatRadioButton rbPm;
    @Bind(R.id.etHours)
    public EditText etHours;
    @Bind(R.id.etMins)
    public EditText etMins;

    public View mView;
    ReminderPresenter mPresenter;

    public static ReminderFragment getInstance(String tag, int countableIndex) {
        ReminderFragment frag = new ReminderFragment();
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
        mView = inflater.inflate(R.layout.reminder_layout, container, false);
        ButterKnife.bind(this, mView);
        setPresenter(new ReminderPresenter());
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
        if (presenter instanceof ReminderPresenter) {
            mPresenter = (ReminderPresenter) presenter;
            mPresenter.bindViews(this);
        } else {
            mPresenter.displaySnackbarMessage(getString(R.string.general_error));
        }
    }

    @Override
    public ReminderPresenter getPresenter() {
        return mPresenter;
    }
}
