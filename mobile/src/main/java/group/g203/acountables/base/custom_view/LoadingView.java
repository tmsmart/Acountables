package group.g203.acountables.base.custom_view;

import group.g203.acountables.base.view.BaseView;

public interface LoadingView extends BaseView {

    public void startLoading();

    public void finishLoading();

    public void setEmptyIcon();

    public void setEmptyMessage();
}
