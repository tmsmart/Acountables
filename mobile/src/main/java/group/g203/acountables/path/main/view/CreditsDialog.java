package group.g203.acountables.path.main.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import group.g203.acountables.R;
import group.g203.acountables.base.presenter.BasePresenter;
import group.g203.acountables.base.view.BaseDialog;
import group.g203.acountables.path.main.presenter.CreditsDialogPresenter;

public class CreditsDialog extends BaseDialog {

    public static final String TAG = "CreditsDialog";

    CreditsDialogPresenter mPresenter;

    public static CreditsDialog getInstance() {
       return new CreditsDialog();
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.credits_dialog_layout, null);
        builder.setView(view);
        setPresenterFromActivity();
        onSetTitle(getString(R.string.credits));
        setPositiveButton(getString(R.string.ok));
        this.dialogTag = TAG;
        final AlertDialog creditsDialog = builder.create();
        return creditsDialog;
    }

    @Override
    public void setPositiveButton(String buttonText) {
        mPresenter.setCreditsDialogPositiveButton(buttonText);
    }

    @Override
    public void setNegativeButton(String buttonText) {
    }

    @Override
    public void onSetTitle(String title) {
        mPresenter.setCreditsDialogTitle(title);
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof CreditsDialogPresenter) {
            mPresenter = (CreditsDialogPresenter) presenter;
        } else {
            mPresenter.displayToast(getString(R.string.dialog_error));
            this.dismiss();
        }
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }

    @Override
    public void setPresenterFromActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            setPresenter(((MainActivity) getActivity()).getPresenter());
        }
    }
}
