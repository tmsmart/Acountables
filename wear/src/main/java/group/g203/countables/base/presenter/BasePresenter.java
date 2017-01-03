package group.g203.countables.base.presenter;

import android.content.Context;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group.g203.countables.R;
import group.g203.countables.base.utils.DisplayUtils;
import group.g203.countables.base.view.BaseActivity;
import group.g203.countables.base.view.GeneralView;
import group.g203.countables.custom_view.wear_recycler_view.DividerItemDecoration;
import group.g203.countables.custom_view.wear_recycler_view.WearListAdapter;
import group.g203.countables.model.Countable;

public class BasePresenter implements GeneralPresenter {

    public GeneralView mGeneralView;
    public ProgressBar mProgress;
    public WearableRecyclerView mRecyclerView;
    public LinearLayout mButtonLayout;
    public CircledImageView mButton;
    public TextView mTopText;
    public TextView mButtonText;
    public WearListAdapter mAdapter;
    public Context mContext;

    @Override
    public void bindModels() {

    }

    @Override
    public void unbindModels() {

    }

    @Override
    public void bindViews(GeneralView... views) {
        mGeneralView = views[0];

        mProgress = ((BaseActivity) mGeneralView).mProgress;
        mRecyclerView = ((BaseActivity) mGeneralView).mRecyclerView;
        mButtonLayout = ((BaseActivity) mGeneralView).mButtonLayout;
        mButton = ((BaseActivity) mGeneralView).mButton;
        mTopText = ((BaseActivity) mGeneralView).mTopText;
        mButtonText = ((BaseActivity) mGeneralView).mButtonText;

        mContext = mButtonLayout.getContext();
    }

    @Override
    public void unbindViews() {

    }

    @Override
    public void unbindModelsAndViews() {

    }

    @Override
    public void displayToast(String message) {
        DisplayUtils.displayToast(mContext, message, Toast.LENGTH_SHORT);
    }

    public void handleContentDisplay() {
        // if not connected, display connect icon to open in app

        // if empty display, display empty to refresh

        // if not empty, then display list
    }

    public void setUpRecyclerView(ArrayList<Countable> countables) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new WearListAdapter(countables, mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, R.drawable.rv_divider));
    }

    public void setUpRecyclerView(ArrayList<String> options, Countable detailCountable) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new WearListAdapter(options, detailCountable, mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, R.drawable.rv_divider));
    }

    public void setGoToPhoneView() {
        mButtonLayout.setVisibility(View.VISIBLE);
        mTopText.setText(R.string.no_countables);
        mButton.setImageResource(R.drawable.common_full_open_on_phone);
        mButtonText.setText(R.string.common_open_on_phone);
    }
}
