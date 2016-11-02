package group.g203.countables.path.detail.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.path.detail.presenter.EditDialogPresenter;

public class EditDialog extends BaseDialog {

    public static final String TAG = "EditDialog";

    EditDialogPresenter mPresenter;
    EditCountableListener mListener;
    String mName;
    @Bind(R.id.etCountableName)
    EditText mCountableName;

    public static EditDialog getInstance(String tag, String countableName) {
        EditDialog dialog = new EditDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DIALOG_TAG, tag);
        bundle.putString(Constants.COUNTABLE_NAME, countableName);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_dialog_layout, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        mCountableName.setText(getArguments().getString(Constants.COUNTABLE_NAME));
        mCountableName.setSelection(mCountableName.getText().length());
        setPresenterFromActivity();
        onSetTitle(builder, getString(R.string.edit_countable_name));
        setPositiveButton(builder, getString(R.string.edit_name));
        setNegativeButton(builder, getString(R.string.cancel));
        this.dialogTag = getArguments().getString(Constants.DIALOG_TAG);
        final AlertDialog infoDialog = builder.create();
        return infoDialog;
    }

    @Override
    public void setPositiveButton(AlertDialog.Builder builder, String buttonText) {
        mPresenter.setEditDialogPositiveButton(builder, buttonText, mListener, this);
    }

    @Override
    public void setNegativeButton(AlertDialog.Builder builder, String buttonText) {
        mPresenter.setEditDialogNegativeButton(builder, buttonText);
    }

    @Override
    public void onSetTitle(AlertDialog.Builder builder, String title) {
        mPresenter.setEditDialogTitle(builder, title);
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof EditDialogPresenter) {
            mPresenter = (EditDialogPresenter) presenter;
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
        mListener = (EditCountableListener) context;
    }

    public interface EditCountableListener {
        public void onEditCountableClick(EditDialog dialog);
    }
}
