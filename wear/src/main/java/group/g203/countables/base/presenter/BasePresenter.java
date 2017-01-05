package group.g203.countables.base.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

import group.g203.countables.R;
import group.g203.countables.base.utils.CollectionUtils;
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
    public GoogleApiClient mClient;
    public Node mNode;
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
        mClient = ((BaseActivity) mGeneralView).mClient;
        mNode  = ((BaseActivity) mGeneralView).mNode;

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
        // to be overridden
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
        mButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOpenOnPhoneMessage(mContext.getString(R.string.mobile_open));
            }
        });
        mTopText.setText(R.string.no_countables);
        mButton.setImageResource(R.drawable.common_full_open_on_phone);
        mButtonText.setText(R.string.common_open_on_phone);
    }

    public void setNotConnectedView() {
        mButtonLayout.setVisibility(View.VISIBLE);
        mTopText.setText("");
        mButton.setImageResource(R.mipmap.ic_sync_off);
        mButton.setCircleColor(ContextCompat.getColor(mContext, R.color.white));
        mButton.setImageTint(ContextCompat.getColor(mContext, R.color.app_gray));
        mButtonText.setText(R.string.no_handheld_connection);
        mButtonText.setTextColor(ContextCompat.getColor(mContext, R.color.app_gray));
    }

    public void onGoogleApiConnected() {
        Wearable.NodeApi.getConnectedNodes(mClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                if (!CollectionUtils.isEmpty(nodes.getNodes())) {
                    for (Node node : nodes.getNodes()) {
                        mNode = node;
                    }
                } else {
                    mClient.disconnect();
                    mClient = null;
                }
            }
        });
    }

    public void sendOpenOnPhoneMessage(String messageKey) {
        if (mNode != null && mClient != null && mClient.isConnected()) {
            Wearable.MessageApi.sendMessage(mClient, mNode.getId(), messageKey, null).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (sendMessageResult.getStatus().isSuccess()) {
                                Intent intent = new Intent(mContext, ConfirmationActivity.class);
                                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                                mContext.startActivity(intent);
                            } else {
                                displayToast(mContext.getString(R.string.data_error));
                            }
                        }
                    }
            );
        } else {
            displayToast(mContext.getString(R.string.data_error));
        }
    }
}
