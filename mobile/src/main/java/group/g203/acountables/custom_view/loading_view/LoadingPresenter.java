package group.g203.acountables.custom_view.loading_view;

import group.g203.acountables.base.presenter.GeneralPresenter;

public interface LoadingPresenter extends GeneralPresenter {

    void displayLoading();

    void displayContent();

    void displayEmptyView();

    void setEmptyMessage(String message);

    void setEmptyIcon(int iconResource);
}
