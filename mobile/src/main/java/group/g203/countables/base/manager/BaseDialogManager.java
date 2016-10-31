package group.g203.countables.base.manager;

import android.support.v4.app.FragmentManager;

import group.g203.countables.base.view.BaseDialog;

public class BaseDialogManager {

    public static void displayFragmentDialog(BaseDialog dialog, FragmentManager fm) {
        dialog.show(fm, dialog.dialogTag);
    }

}
