package group.g203.countables.path.main.presenter;

import android.widget.ImageView;
import android.widget.TextView;

import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.model.Countable;

public interface CountableViewHolderPresenter extends GeneralPresenter {

    void handleReminderIcon(ImageView imageView, Countable countable);

    void handleAccountableIcon(ImageView imageView, Countable countable);

    void setLastCompletedText(TextView textView, Countable countable);

    void setCompletedCount(TextView textView, Countable countable);

    void setCountableTitle(TextView textview, String title);
}
