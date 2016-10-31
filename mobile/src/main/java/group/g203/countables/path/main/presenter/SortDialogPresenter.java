package group.g203.countables.path.main.presenter;

import android.support.v7.app.AlertDialog;

import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.path.main.view.SortDialog;

public interface SortDialogPresenter extends GeneralPresenter {

    void setSortDialogTitle(AlertDialog.Builder builder, String title);

    void setSortDialogItems(SortDialog sortDialog, AlertDialog.Builder builder, int arrayResource);
}
