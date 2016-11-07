package group.g203.countables.path.detail.view;

import group.g203.countables.base.view.BaseView;

public interface AccountableView extends BaseView {

    void handleDisplay();

    void onSetSwitch();

    void onSetDeleteClick();

    void onSetEditClick();
}
