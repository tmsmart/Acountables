package group.g203.countables.path.detail.presenter;

import android.support.v7.app.AlertDialog;

import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.path.detail.view.EditTimeDialog;

public interface TimeDialogPresenter extends GeneralPresenter {

    void setDialogTitle(AlertDialog.Builder builder, String title);

    void setDialogNegativeButton(AlertDialog.Builder builder, String buttonText);

    void setDialogPositiveButton(AlertDialog.Builder builder, String buttonText,
                                     EditTimeDialog.TimeDialogListener listener,
                                     EditTimeDialog dialog);
}
