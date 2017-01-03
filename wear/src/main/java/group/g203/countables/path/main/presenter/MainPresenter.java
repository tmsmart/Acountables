package group.g203.countables.path.main.presenter;

import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.custom_view.wear_recycler_view.ViewHolderPresenter;
import group.g203.countables.model.Countable;

public class MainPresenter extends BasePresenter implements ViewHolderPresenter {

    @Override
    public void handleContentDisplay() {
        mProgress.setVisibility(View.GONE);

        // if not connected, display connect icon to open in app
        // if empty display, display empty to refresh
        // if not empty, then display list

        setUpRecyclerView(getTestCountables());
        //setGoToPhoneView();
    }

    private static ArrayList<Countable> getTestCountables() {
        ArrayList<Countable> testCountables = new ArrayList<>();

        Countable c1 = new Countable();
        c1.name = "Test";
        c1.id = 0;
        c1.index = 0;
        c1.loggedDates = null;
        c1.timesCompleted = 0;
        c1.lastModified = null;
        c1.isAccountable = false;
        c1.isReminderEnabled = false;
        c1.anchorDates = null;
        c1.dayRepeater = 0;
        testCountables.add(c1);

        Countable c2 = new Countable();
        c2.name = "Test2";
        c2.id = 0;
        c2.index = 0;
        c2.loggedDates = null;
        c2.timesCompleted = 0;
        c2.lastModified = null;
        c2.isAccountable = false;
        c2.isReminderEnabled = false;
        c2.anchorDates = null;
        c2.dayRepeater = 0;
        testCountables.add(c2);

        testCountables.add(null);

        return testCountables;
    }

    @Override
    public void handleRowIcon(CircledImageView view, int imageId) {
        view.setImageResource(imageId);
    }

    @Override
    public void handleTextView(TextView textView, String text) {
        textView.setText(text);
    }
}