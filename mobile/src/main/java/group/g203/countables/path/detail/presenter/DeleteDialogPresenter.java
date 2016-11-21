package group.g203.countables.path.detail.presenter;

import android.support.v7.app.AlertDialog;

import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.path.detail.view.DeleteDialog;

public interface DeleteDialogPresenter extends GeneralPresenter {

    void setDeleteDialogTitle(AlertDialog.Builder builder, String title);

    void setDeleteDialogNegativeButton(AlertDialog.Builder builder, String buttonText);

    void setDeleteDialogPositiveButton(AlertDialog.Builder builder, String buttonText,
                                         DeleteDialog.DeleteCountableListener listener,
                                         DeleteDialog dialog);
}
