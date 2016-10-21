package group.g203.acountables.base.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public abstract class BaseDialog extends DialogFragment implements BaseDialogView {
    public AlertDialog alertDialog;
    public AlertDialog.Builder builder;
    public String dialogTag;
    public DialogInterface.OnDismissListener onDismissListener;

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        builder = dialogBuilder;
        alertDialog = dialogBuilder.create();
        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
