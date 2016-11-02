package group.g203.countables.path.detail.presenter;

import android.support.v7.app.AlertDialog;

import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.path.detail.view.EditDialog;

public interface EditDialogPresenter extends GeneralPresenter {

    void setEditDialogTitle(AlertDialog.Builder builder, String title);

    void setEditDialogNegativeButton(AlertDialog.Builder builder, String buttonText);

    void setEditDialogPositiveButton(AlertDialog.Builder builder, String buttonText,
                                       EditDialog.EditCountableListener listener,
                                       EditDialog dialog);

}
