package group.g203.countables.path.main.presenter;

import android.support.v7.app.AlertDialog;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface CreditsDialogPresenter extends GeneralPresenter {

    void setCreditsDialogTitle(AlertDialog.Builder builder, String title);

    void setCreditsDialogPositiveButton(AlertDialog.Builder builder, String buttonText);

}
