package group.g203.countables.base.utils;

import android.content.Context;
import android.widget.Toast;

public class DisplayUtils {

    public static void displayToast(Context context, String msg, int displayDuration) {
        Toast.makeText(context, msg, displayDuration).show();
    }

}
