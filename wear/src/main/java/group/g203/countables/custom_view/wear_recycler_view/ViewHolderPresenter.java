package group.g203.countables.custom_view.wear_recycler_view;

import android.support.wearable.view.CircledImageView;
import android.widget.TextView;

import group.g203.countables.base.presenter.GeneralPresenter;

public interface ViewHolderPresenter extends GeneralPresenter {

    void handleRowIcon(CircledImageView view, int imageId);

    void handleTextView(TextView textView, String text);
}
