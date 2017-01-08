package group.g203.countables.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.List;

import group.g203.countables.R;
import group.g203.countables.base.manager.GsonManager;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.main.view.MainActivity;
import io.realm.Realm;
import io.realm.Sort;

public class WearListenerService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Realm mRealm;
    GoogleApiClient mClient;
    Node mNode;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String[] pathStrings = messageEvent.getPath().split(Constants.FORWARD_SLASH);
        int pathCount = pathStrings.length;
        switch (pathCount) {
            case Constants.ONE:
                String pathKey = pathStrings[0];
                if (pathKey.equals(getString(R.string.mobile_open))) {
                    Intent startIntent = new Intent(this, MainActivity.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                } else if (pathKey.equals(getString(R.string.mobile_bg))) {
                    mClient = new GoogleApiClient.Builder(this)
                            .addApi(Wearable.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();

                    ConnectionResult connectionResult = mClient.blockingConnect();
                    if (connectionResult.isSuccess()) {
                        Wearable.NodeApi.getConnectedNodes(mClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                            @Override
                            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                                if (!CollectionUtils.isEmpty(nodes.getNodes(), false)) {
                                    for (Node node : nodes.getNodes()) {
                                        mNode = node;
                                        onConnected(null);
                                    }
                                } else {
                                    mClient.disconnect();
                                    mClient = null;
                                }
                            }
                        });
                    }
                }
                break;
            case Constants.THREE:
                Integer countableId = Integer.parseInt(pathStrings[1]);
                Intent navIntent = new Intent(this, DetailActivity.class);
                navIntent.setAction(pathStrings[2]);
                navIntent.putExtra(Constants.COUNTABLE_ID, countableId);
                navIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(navIntent);
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mNode != null && mClient != null && mClient.isConnected()) {
            ArrayList<String> countableData = new ArrayList<>(1);

            getRealmInstance().beginTransaction();
            List<Countable> allCountables = getRealmInstance().where(Countable.class).findAll().sort(Constants.INDEX, Sort.ASCENDING);
            getRealmInstance().commitTransaction();

            if (CollectionUtils.isEmpty(allCountables, true)) {
            } else {
                countableData = new ArrayList<>(allCountables.size());
                for (Countable countable : allCountables) {
                    countableData.add(GsonManager.toJson(countable));
                }
            }
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(Constants.FORWARD_SLASH + getString(R.string.all_countable_data));
            putDataMapReq.getDataMap().putStringArrayList(getString(R.string.get_all_countables_key), countableData);
            putDataMapReq.getDataMap().putLong(getString(R.string.data_map_time), System.currentTimeMillis());
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(mClient, putDataReq);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        openAndDisplayConnectionError(getString(R.string.connection_suspension));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        openAndDisplayConnectionError(getString(R.string.connection_error));
    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

    void openAndDisplayConnectionError(String errMsg) {
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startIntent.setAction(errMsg);
        startActivity(startIntent);
    }
}


