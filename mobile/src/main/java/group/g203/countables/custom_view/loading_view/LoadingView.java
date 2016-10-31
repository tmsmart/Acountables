package group.g203.countables.custom_view.loading_view;

import group.g203.countables.base.view.BaseView;

public interface LoadingView extends BaseView {

    void onDisplayLoading();

    void onDisplayContent();

    void onDisplayEmptyView();

    void onSetEmptyMessage(String message);

    void onSetEmptyIcon(int iconResource);
}
