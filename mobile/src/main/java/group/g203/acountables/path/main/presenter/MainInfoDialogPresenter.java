package group.g203.acountables.path.main.presenter;

import group.g203.acountables.base.presenter.GeneralPresenter;

public interface MainInfoDialogPresenter extends GeneralPresenter {

    void setInfoDialogTitle(String title);

    void setInfoDialogPositiveButton(String buttonText);

    void setInfoDialogMessage(String message);

}
