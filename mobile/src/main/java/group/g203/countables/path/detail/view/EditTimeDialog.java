package group.g203.countables.path.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.path.detail.presenter.TimeDialogPresenter;

public class EditTimeDialog extends BaseDialog {

    public static final String TAG = "EditTimeDialog";
    public static final int DELETE_SCHEDULE = 0;
    public static final int DELETE_REMINDER = 1;
    public static final int EDIT_SCHEDULE = 2;
    public static final int EDIT_REMINDER = 3;

    public int dialogCode;
    TimeDialogPresenter mPresenter;
    TimeDialogListener mListener;

    public static EditTimeDialog getInstance(String tag, String dialogTitle) {
        EditTimeDialog dialog = new EditTimeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DIALOG_TAG, tag);
        bundle.putString(Constants.DIALOG_TITLE, dialogTitle);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        setPresenterFromActivity();
        onSetTitle(builder, getArguments().getString(Constants.DIALOG_TITLE));
        setPositiveButton(builder, determineActionString());
        setNegativeButton(builder, getString(R.string.cancel));
        this.dialogTag = getArguments().getString(Constants.DIALOG_TAG);
        dialogCode = determineDialogCode();
        final AlertDialog infoDialog = builder.create();
        return infoDialog;
    }

    @Override
    public void setPositiveButton(AlertDialog.Builder builder, String buttonText) {
        mPresenter.setDialogPositiveButton(builder, buttonText, mListener, this);
    }

    @Override
    public void setNegativeButton(AlertDialog.Builder builder, String buttonText) {
        mPresenter.setDialogNegativeButton(builder, buttonText);
    }

    @Override
    public void onSetTitle(AlertDialog.Builder builder, String title) {
        mPresenter.setDialogTitle(builder, title);
    }

    @Override
    public void setPresenterFromActivity() {
        if (getActivity() != null && getActivity() instanceof DetailActivity) {
            setPresenter(((DetailActivity) getActivity()).getPresenter());
        }
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof TimeDialogPresenter) {
            mPresenter = (TimeDialogPresenter) presenter;
        }
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (EditTimeDialog.TimeDialogListener) context;
    }

    String determineActionString() {
        String dialogTitle = getArguments().getString(Constants.DIALOG_TITLE);
        if (dialogTitle.contains(getString(R.string.delete))) {
            return getString(R.string.delete);
        } else {
            return getString(R.string.yes_edit);
        }
    }

    int determineDialogCode() {
        String dialogTitle = getArguments().getString(Constants.DIALOG_TITLE);
        if (dialogTitle.equals(getString(R.string.delete_schedule))) {
            return 0;
        } else if (dialogTitle.equals(getString(R.string.delete_reminder))) {
            return 1;
        } else if (dialogTitle.equals(getString(R.string.edit_schedule))) {
            builder.setMessage(getString(R.string.edit_schedule_msg));
            return 2;
        } else {
            builder.setMessage(getString(R.string.edit_reminder_msg));
            return 3;
        }
    }

    public interface TimeDialogListener {
        void onEditAccountableClick(EditTimeDialog dialog);
        void onEditReminderClick(EditTimeDialog dialog);
        void onDeleteAccountableClick(EditTimeDialog dialog);
        void onDeleteReminderClick(EditTimeDialog dialog);
    }
}
