package group.g203.countables.path.main.presenter;

import android.support.v7.app.AlertDialog;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface InfoDialogPresenter extends GeneralPresenter {

    void setInfoDialogTitle(AlertDialog.Builder builder,  String title);

    void setInfoDialogPositiveButton(AlertDialog.Builder builder, String buttonText);

    void setInfoDialogMessage(AlertDialog.Builder builder, String message);

}
