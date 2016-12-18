package group.g203.countables.base.view;

import group.g203.countables.base.presenter.BasePresenter;

public interface BaseView {

    <P extends BasePresenter> void setPresenter(P presenter);

    <P extends BasePresenter> P getPresenter();

}
