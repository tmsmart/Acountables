package group.g203.countables.base.presenter;


import group.g203.countables.base.view.BaseView;

public interface BasePresenter {

    void displayToast(String message);

    void bindModels();

    void unbindModels();

    void bindViews(BaseView... views);

    void unbindViews();

    void unbindModelsAndViews();

}
