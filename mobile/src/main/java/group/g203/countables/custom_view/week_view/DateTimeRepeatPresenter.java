package group.g203.countables.custom_view.week_view;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface DateTimeRepeatPresenter extends GeneralPresenter {

    void setActiveAspect();

    void setReadOnlyAspect();

    void setRepeatOptionClicks();

    void setMonth();

    void setDays();

    void setRepeatDay();

    void setTime();

    void setRepeatDayWatcher();

}
