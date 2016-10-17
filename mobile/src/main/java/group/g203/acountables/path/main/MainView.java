package group.g203.acountables.path.main;


import group.g203.acountables.base.view.BaseView;

public interface MainView extends BaseView {

    void displayEmptyView();

    void displayCountables();

    void addCountable();

    void deleteCountable();

    void undoCountableDelete();

    void displayInfoDialog();

    void displaySortDialog();

    void enableAddButton();

    void disableAddButton();

    void resetAddCountableView();
}
