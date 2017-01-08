package group.g203.countables.path.main.presenter;

import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;

import java.util.ArrayList;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.view.BaseActivity;
import group.g203.countables.custom_view.wear_recycler_view.ViewHolderPresenter;
import group.g203.countables.custom_view.wear_recycler_view.WearListAdapter;
import group.g203.countables.model.Countable;

public class MainPresenter extends BasePresenter implements ViewHolderPresenter {

    @Override
    public void handleContentDisplay() {
        onGoogleApiConnected();
    }

    @Override
    public void handleRowIcon(CircledImageView view, int imageId) {
        view.setImageResource(imageId);
    }

    @Override
    public void onGoogleApiConnected() {
        if (mClient != null) {
            Wearable.DataApi.addListener(mClient, ((BaseActivity) mGeneralView));
            Wearable.NodeApi.getConnectedNodes(mClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                @Override
                public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                    if (!CollectionUtils.isEmpty(nodes.getNodes())) {
                        for (Node node : nodes.getNodes()) {
                            mNode = node;
                        }
                        sendMessageToPhone(mContext.getString(R.string.mobile_bg));
                    } else {
                        mClient.disconnect();
                        mClient = null;
                        setNotConnectedView();
                    }
                }
            });
        }
    }

    @Override
    public void handleTextView(TextView textView, String text) {
        textView.setText(text);
    }

    public void dataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            int eventType = event.getType();
            if (eventType == DataEvent.TYPE_CHANGED || eventType == DataEvent.TYPE_DELETED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(Constants.FORWARD_SLASH + mContext.getString(R.string.all_countable_data)) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ArrayList<String> countableData = dataMap.getStringArrayList(mContext.getString(R.string.get_all_countables_key));
                    if (CollectionUtils.isEmpty(countableData)) {
                        setGoToPhoneView();
                    } else {
                        ArrayList<Countable> countables = new ArrayList<>(countableData.size());
                        for (String data : countableData) {
                            try {
                                countables.add(new Countable(data));
                            } catch (JSONException e) {
                                displayToast(mContext.getString(R.string.wear_data_error));
                            }
                        }
                        countables.add(null);
                        if (mRecyclerView.getVisibility() != View.VISIBLE) {
                            setUpRecyclerView(countables);
                        } else {
                            WearListAdapter adapter = new WearListAdapter(countables, mContext);
                            mRecyclerView.swapAdapter(adapter, false);
                        }
                    }
                }
            }
        }
    }
}