package group.g203.countables.path.main.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.main.presenter.InfoDialogPresenter;

public class InfoDialog extends BaseDialog {

    public static final String TAG = "InfoDialog";

    InfoDialogPresenter mPresenter;

    public static InfoDialog getInstance(String tag, String type) {
        InfoDialog dialog = new InfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INFO_TYPE, type);
        bundle.putString(Constants.DIALOG_TAG, tag);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        setPresenterFromActivity();
        onSetTitle(builder, getArguments().getString(Constants.INFO_TYPE));
        setPositiveButton(builder, getString(R.string.got_it));
        onSetDialogMessage(getArguments().getString(Constants.INFO_TYPE));
        this.dialogTag = getArguments().getString(Constants.DIALOG_TAG);
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
    public void onSetTitle(AlertDialog.Builder builder, String dialogType) {
        String title = Constants.EMPTY_STRING;
        if (dialogType.equals(Constants.MAIN_INFO)) {
            title = getString(R.string.welcome_title);
        } else if (dialogType.equals(Constants.DETAIL_INFO)) {
            title = getString(R.string.countable_detail_info);
        }
        mPresenter.setInfoDialogTitle(builder, title);
    }

    public void onSetDialogMessage(String dialogType) {
        String msg = Constants.EMPTY_STRING;
        if (dialogType.equals(Constants.MAIN_INFO)) {
            msg = getString(R.string.main_screen_info) + Constants.NEWLINE
                    + Constants.NEWLINE + getString(R.string.on_countable_screen) + Constants.NEWLINE
                    + Constants.NEWLINE + getString(R.string.interact_countable);
        } else if (dialogType.equals(Constants.DETAIL_INFO)) {
            msg = getString(R.string.detail_screen_info) + Constants.NEWLINE
                    + Constants.NEWLINE + getString(R.string.add_completion) + Constants.NEWLINE
                    + Constants.NEWLINE + getString(R.string.make_accountable) + Constants.NEWLINE
                    + Constants.NEWLINE + getString(R.string.set_reminder);
        }
        mPresenter.setInfoDialogMessage(builder, msg);
    }

    @Override
    public void setPresenterFromActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            setPresenter(((MainActivity) getActivity()).getPresenter());
        } else if (getActivity() != null && getActivity() instanceof DetailActivity) {
            setPresenter(((DetailActivity) getActivity()).getPresenter());
        }
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof InfoDialogPresenter) {
            mPresenter = (InfoDialogPresenter) presenter;
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