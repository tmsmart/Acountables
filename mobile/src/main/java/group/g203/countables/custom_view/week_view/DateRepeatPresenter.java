package group.g203.countables.custom_view.week_view;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface DateRepeatPresenter extends GeneralPresenter {

    void setActiveAspect();

    void setReadOnlyAspect(boolean makeUnaccountable);

    void setRepeatOptionClicks();

    void setMonth();

    void setDays();

    void setRepeatDay();

    void setRepeatDayWatcher();

}
