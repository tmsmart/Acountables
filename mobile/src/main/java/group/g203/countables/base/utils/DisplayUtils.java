package group.g203.countables.base.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class DisplayUtils {

    public static void displayToast(Context context, String msg, int displayDuration) {
        Toast.makeText(context, msg, displayDuration).show();
    }

    public static void displaySimpleSnackbar(View view, String msg, int displayDuration, Snackbar.Callback callback) {
        Snackbar.make(view, msg, displayDuration)
                .setCallback(callback)
                .show();
    }

    public static void displayActionSnackbar(View view, String msg,
                                             String actionText, int displayDuration,
                                             int colorInt, View.OnClickListener listener,
                                             Snackbar.Callback callback) {
        Snackbar.make(view, msg, displayDuration)
                .setAction(actionText, listener)
                .setActionTextColor(colorInt)
                .setCallback(callback)
                .show();
    }
}
