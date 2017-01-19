package group.g203.countables.base.presenter;

import group.g203.countables.base.view.GeneralView;

public interface GeneralPresenter {

    void displayToast(String message);

    void bindModels();

    void unbindModels();

    void bindViews(GeneralView... views);

    void unbindViews();

    void unbindModelsAndViews();

    void handleContentDisplay();

}
