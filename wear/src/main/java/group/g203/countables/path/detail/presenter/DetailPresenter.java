package group.g203.countables.path.detail.presenter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.custom_view.wear_recycler_view.ViewHolderPresenter;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.DetailActivity;

public class DetailPresenter extends BasePresenter implements ViewHolderPresenter {

    private final static int OPTS_COUNT = 3;

    @Override
    public void handleContentDisplay() {
        mProgress.setVisibility(View.GONE);
        if (mClient == null && !mClient.isConnected() && mNode == null) {
            setNotConnectedView();
        } else {
            Bundle bundle = ((DetailActivity) mGeneralView).getIntent().getExtras();
            if (bundle.containsKey(Constants.COUNTABLE)) {
                Countable countable = bundle.getParcelable(Constants.COUNTABLE);
                setUpRecyclerView(getCountableOptions(), countable);
            }
        }
    }

    @Override
    public void handleRowIcon(CircledImageView view, int imageId) {
        view.setImageResource(imageId);
        view.setImageTint(ContextCompat.getColor(mContext, R.color.white));
    }

    @Override
    public void handleTextView(TextView textView, String text) {
        textView.setText(text);
    }

    ArrayList<String> getCountableOptions() {
        ArrayList<String> options = new ArrayList<>(OPTS_COUNT);
        options.add(mContext.getString(R.string.log_completion));
        options.add(mContext.getString(R.string.set_accountability));
        options.add(mContext.getString(R.string.set_reminder));
        return options;
    }
}
