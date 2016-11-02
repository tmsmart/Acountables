package group.g203.countables.path.detail.view;

import group.g203.countables.base.view.BaseView;

public interface DetailView extends BaseView {

    void displayTimeLog();

    void displayAccountableView();

    void displayReminderView();

    void displayInfoDialog();

    void displayDeleteDialog();

    void displayEditDialog();

    void setInitialCountableInfo();
}
