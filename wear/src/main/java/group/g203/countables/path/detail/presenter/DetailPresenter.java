package group.g203.countables.path.detail.presenter;

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
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.DisplayUtils;
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

    }
}
