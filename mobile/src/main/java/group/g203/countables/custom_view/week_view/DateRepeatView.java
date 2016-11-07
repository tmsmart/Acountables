package group.g203.countables.custom_view.week_view;

import group.g203.countables.base.view.BaseView;

public interface DateRepeatView extends BaseView {

    void onSetActiveAspect();

    void onSetReadOnlyAspect();

    void onSetRepeatOptionClicks();

    void onSetMonth();

    void onSetDays();

    void onSetRepeatDay();

    void onSetRepeatDayWatcher();

}
