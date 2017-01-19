package group.g203.countables.path.detail.presenter;

import android.widget.TextView;

import java.util.Date;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface DateViewHolderPresenter extends GeneralPresenter {

    void setDateFormat(TextView textView, Date date);

    void handleDateColor(TextView textView);

}
