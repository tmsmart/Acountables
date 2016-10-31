package group.g203.countables.path.main.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.path.main.presenter.MainInfoDialogPresenter;

public class MainInfoDialog extends BaseDialog {

    public static final String TAG = "MainInfoDialog";

    MainInfoDialogPresenter mPresenter;

    public static MainInfoDialog getInstance() {
        MainInfoDialog dialog = new MainInfoDialog();
        dialog.dialogTag = TAG;
        return dialog;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        setPresenterFromActivity();
        onSetTitle(builder, getString(R.string.welcome_title));
        setPositiveButton(builder, getString(R.string.got_it));
        onSetDialogMessage(getString(R.string.use_interface) + Constants.NEWLINE
                + Constants.NEWLINE + getString(R.string.on_countable_screen) + Constants.NEWLINE
                + Constants.NEWLINE + getString(R.string.interact_countable));
        this.dialogTag = TAG;
        final AlertDialog infoDialog = builder.create();
        return infoDialog;
    }

    @Override
    public void setPositiveButton(AlertDialog.Builder builder, String buttonText) {
        mPresenter.setInfoDialogPositiveButton(builder, buttonText);
    }

    @Override
    public void setNegativeButton(AlertDialog.Builder builder, String buttonText) {
    }

    @Override
    public void onSetTitle(AlertDialog.Builder builder, String title) {

    }

    public void onSetDialogMessage(String msg) {
        mPresenter.setInfoDialogMessage(builder, msg);
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