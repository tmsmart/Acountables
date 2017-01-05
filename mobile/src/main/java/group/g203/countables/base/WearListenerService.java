package group.g203.countables.base;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.main.view.MainActivity;

public class WearListenerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String[] pathStrings = messageEvent.getPath().split(Constants.FORWARD_SLASH);
        int pathCount = pathStrings.length;
        switch (pathCount) {
            case Constants.ONE:
                Intent startIntent = new Intent(this, MainActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startIntent);
                break;
            case Constants.THREE:
                Integer countableId = Integer.parseInt(pathStrings[1]);
                startIntent = new Intent(this, DetailActivity.class);
                startIntent.setAction(pathStrings[2]);
                startIntent.putExtra(Constants.COUNTABLE_ID, countableId);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(startIntent);
                break;
        }
    }
}


