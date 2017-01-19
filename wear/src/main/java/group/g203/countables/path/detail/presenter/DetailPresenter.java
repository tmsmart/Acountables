package group.g203.countables.path.detail.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.utils.DisplayUtils;
import group.g203.countables.custom_view.wear_recycler_view.ViewHolderPresenter;
import group.g203.countables.custom_view.wear_recycler_view.WearListAdapter;
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
        onGoogleApiConnected();
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

    public void updateLogCount(final Countable countable) {
        try {
            JSONObject jsonObj = new JSONObject(countable.jsonString);

            final int updatedCount = jsonObj.getInt(Constants.TIMES_COMPLETED) + 1;
            String updatedLastMod = new Date().toString();

            jsonObj.put(Constants.TIMES_COMPLETED, updatedCount);
            jsonObj.put(Constants.LAST_MODIFIED, updatedLastMod);

            countable.jsonString = jsonObj.toString();

            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(Constants.FORWARD_SLASH + countable.id);
            putDataMapReq.getDataMap().putString(mContext.getString(R.string.wear_countable_key), countable.jsonString);
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(mClient, putDataReq);
            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                    if (dataItemResult.getStatus().isSuccess()) {
                        DisplayUtils.displayToast(mContext, mContext.getString(R.string.completed_msg) + " " + updatedCount, Toast.LENGTH_SHORT);
                    } else {
                        DisplayUtils.displayToast(mContext, mContext.getString(R.string.data_transfer_error), Toast.LENGTH_SHORT);
                    }
                }
            });
        } catch (JSONException e) {
            displayToast(mContext.getString(R.string.wear_data_error));
        }
    }

    ArrayList<String> getCountableOptions() {
        ArrayList<String> options = new ArrayList<>(OPTS_COUNT);
        options.add(mContext.getString(R.string.log_completion));
        options.add(mContext.getString(R.string.set_accountability));
        options.add(mContext.getString(R.string.set_reminder));
        return options;
    }

    public void dataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            int eventType = event.getType();
            if (eventType == DataEvent.TYPE_CHANGED || eventType == DataEvent.TYPE_DELETED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(Constants.FORWARD_SLASH + mContext.getString(R.string.all_countable_data)) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ArrayList<String> countableData = dataMap.getStringArrayList(mContext.getString(R.string.get_all_countables_key));
                    if (CollectionUtils.isEmpty(countableData) ) {
                        finishActivity(((DetailActivity) mGeneralView));
                    } else {
                        ArrayList<Countable> countables = new ArrayList<>(countableData.size());
                        for (String data : countableData) {
                            try {
                                countables.add(new Countable(data));
                            } catch (JSONException e) {
                                displayToast(mContext.getString(R.string.wear_data_error));
                            }
                        }

                        Countable detailCountable = getDetailCountable(countables);

                        if (detailCountable == null) {
                            finishActivity(((DetailActivity) mGeneralView));
                        } else if (detailCountable.timesCompleted != mAdapter.mDetailCountable.id || detailCountable.index != mAdapter.mDetailCountable.index) {
                            WearListAdapter adapter = new WearListAdapter(getCountableOptions(), detailCountable, mContext);
                            mRecyclerView.swapAdapter(adapter, false);
                        }
                    }
                }
            }
        }
    }

    private Countable getDetailCountable(List<Countable> countables) {
        Countable detailCountable = null;
        for (Countable countable : countables) {
            if (mAdapter.mDetailCountable.id == countable.id) {
                detailCountable = countable;
            }
        }
        return detailCountable;
    }

    private void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
        }
    }
}
