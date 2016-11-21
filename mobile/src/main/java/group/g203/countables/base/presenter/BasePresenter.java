package group.g203.countables.base.presenter;

import group.g203.countables.base.view.BaseView;

public interface BasePresenter extends GeneralPresenter {

    void bindModels();

    void unbindModels();

    void bindViews(BaseView... views);

    void unbindViews();

    void unbindModelsAndViews();

    void displaySnackbarMessage(String message);
}
