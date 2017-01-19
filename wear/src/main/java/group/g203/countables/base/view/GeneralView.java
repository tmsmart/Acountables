package group.g203.countables.base.view;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface GeneralView {
    <P extends GeneralPresenter> void setPresenter(P presenter);
}
