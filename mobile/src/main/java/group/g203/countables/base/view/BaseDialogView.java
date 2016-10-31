package group.g203.countables.base.view;

import android.support.v7.app.AlertDialog;

public interface BaseDialogView extends BaseInnerView {

    void setPositiveButton(AlertDialog.Builder builder, String buttonText);

    void setNegativeButton(AlertDialog.Builder builder, String buttonText);

    void onSetTitle(AlertDialog.Builder builder, String title);
}
