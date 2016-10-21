package group.g203.acountables.base.presenter;

import group.g203.acountables.base.view.BaseView;

public interface BasePresenter extends GeneralPresenter {

    void bindModels();

    void unbindModels();

    void bindViews(BaseView... views);

    void unbindViews();

    void unbindModelsAndViews();

    void displayError(String errorMessage);
}
