package group.g203.countables.path.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.path.detail.presenter.DeleteDialogPresenter;

public class DeleteDialog extends BaseDialog {

    public final static String TAG = "DeleteDialog";

    DeleteDialogPresenter mPresenter;
    DeleteCountableListener mListener;

    public static DeleteDialog getInstance(String tag) {
        DeleteDialog dialog = new DeleteDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DIALOG_TAG, tag);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        setPresenterFromActivity();
        onSetTitle(builder, getString(R.string.delete_countable_title));
        setPositiveButton(builder, getString(R.string.delete_countable));
        setNegativeButton(builder, getString(R.string.cancel));
        this.dialogTag = getArguments().getString(Constants.DIALOG_TAG);
        final AlertDialog creditsDialog = builder.create();
        return creditsDialog;
    }

    @Override
    public void setPositiveButton(AlertDialog.Builder builder, String buttonText) {
        mPresenter.setDeleteDialogPositiveButton(builder, buttonText, mListener, this);
    }

    @Override
    public void setNegativeButton(AlertDialog.Builder builder, String buttonText) {
        mPresenter.setDeleteDialogNegativeButton(builder, buttonText);
    }

    @Override
    public void onSetTitle(AlertDialog.Builder builder, String title) {
        mPresenter.setDeleteDialogTitle(builder, title);
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof DeleteDialogPresenter) {
            mPresenter = (DeleteDialogPresenter) presenter;
        }
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }

    @Override
    public void setPresenterFromActivity() {
        if (getActivity() != null && getActivity() instanceof DetailActivity) {
            setPresenter(((DetailActivity) getActivity()).getPresenter());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (DeleteCountableListener) context;
    }

    public interface DeleteCountableListener {
        public void onDeleteCountableClick(DeleteDialog dialog);
    }
}
