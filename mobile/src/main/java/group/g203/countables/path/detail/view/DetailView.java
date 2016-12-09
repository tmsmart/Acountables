package group.g203.countables.path.detail.view;

import group.g203.countables.base.view.BaseView;

public interface DetailView extends BaseView {

    void displayTimeLog();

    void displayAccountableView();

    void displayReminderView();

    void setTimeLogClick();

    void setAccountableClick();

    void setReminderClick();

    void displayInfoDialog();

    void displayDeleteDialog();

    void displayEditDialog();

    void setInitialCountableInfo(int navIndex);
}
