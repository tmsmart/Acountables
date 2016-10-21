package group.g203.countables.path.main.presenter;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface MainInfoDialogPresenter extends GeneralPresenter {

    void setInfoDialogTitle(String title);

    void setInfoDialogPositiveButton(String buttonText);

    void setInfoDialogMessage(String message);

}
