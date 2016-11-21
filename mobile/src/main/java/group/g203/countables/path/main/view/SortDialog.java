package group.g203.countables.path.main.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.base.view.BaseDialogView;
import group.g203.countables.path.main.presenter.SortDialogPresenter;

public class SortDialog extends BaseDialog implements BaseDialogView {

    public static final String TAG = "SortDialog";

    SortDialogPresenter mPresenter;

    public static SortDialog getInstance(String tag) {
        SortDialog dialog = new SortDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DIALOG_TAG, tag);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        setPresenterFromActivity();
        onSetTitle(builder, getString(R.string.sort_title));
        onSetSortDialogItems(R.array.sort_options);
        this.dialogTag = getArguments().getString(Constants.DIALOG_TAG);
        final AlertDialog sortDialog = builder.create();
        return sortDialog;
    }

    @Override
    public void setPositiveButton(AlertDialog.Builder builder, String buttonText) {

    }

    @Override
    public void setNegativeButton(AlertDialog.Builder builder, String buttonText) {

    }

    @Override
    public void onSetTitle(AlertDialog.Builder builder, String title) {
        mPresenter.setSortDialogTitle(builder, title);
    }

    @Override
    public void setPresenterFromActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            setPresenter(((MainActivity) getActivity()).getPresenter());
        }
    }

    public void onSetSortDialogItems(int arrayResource) {
        mPresenter.setSortDialogItems(this, builder, arrayResource);
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof SortDialogPresenter) {
            mPresenter = (SortDialogPresenter) presenter;
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
