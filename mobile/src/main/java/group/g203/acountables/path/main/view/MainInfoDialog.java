package group.g203.acountables.path.main.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import group.g203.acountables.R;
import group.g203.acountables.base.Constants;
import group.g203.acountables.base.presenter.BasePresenter;
import group.g203.acountables.base.view.BaseDialog;
import group.g203.acountables.path.main.presenter.MainInfoDialogPresenter;

public class MainInfoDialog extends BaseDialog {

    public static final String TAG = "MainInfoDialog";

    MainInfoDialogPresenter mPresenter;

    public static MainInfoDialog getInstance() {
        return new MainInfoDialog();
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        setPresenterFromActivity();
        onSetTitle(getString(R.string.welcome_title));
        setPositiveButton(getString(R.string.got_it));
        onSetDialogMessage(getString(R.string.use_interface) + Constants.NEWLINE
                + Constants.NEWLINE + getString(R.string.on_countable_screen) + Constants.NEWLINE
                + Constants.NEWLINE + getString(R.string.interact_countable));
        this.dialogTag = TAG;
        final AlertDialog infoDialog = builder.create();
        return infoDialog;
    }

    @Override
    public void setPositiveButton(String buttonText) {
        mPresenter.setInfoDialogPositiveButton(buttonText);
    }

    @Override
    public void setNegativeButton(String buttonText) {
    }

    @Override
    public void onSetTitle(String title) {
        mPresenter.setInfoDialogTitle(title);
    }

    public void onSetDialogMessage(String msg) {
        mPresenter.setInfoDialogMessage(msg);
    }

    @Override
    public void setPresenterFromActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            setPresenter(((MainActivity) getActivity()).getPresenter());
        }
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof MainInfoDialogPresenter) {
            mPresenter = (MainInfoDialogPresenter) presenter;
        } else {
            mPresenter.displayToast(getString(R.string.dialog_error));
            this.dismiss();
        }
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }
}