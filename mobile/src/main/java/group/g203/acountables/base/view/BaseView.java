package group.g203.acountables.base.view;

import group.g203.acountables.base.presenter.BaseViewPresenter;

public interface BaseView {

    <P extends BaseViewPresenter> void setPresenter(P presenter);

}