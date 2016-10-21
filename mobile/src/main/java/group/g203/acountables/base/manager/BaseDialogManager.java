package group.g203.acountables.base.manager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import group.g203.acountables.base.utils.CollectionUtils;
import group.g203.acountables.base.view.BaseDialog;

public class BaseDialogManager {
    private static final String DIALOGS_KEY = "dialogsKey";
    private final AtomicInteger mAtomicId;
    private int mDialogsId;
    private static BaseDialogManager instance;
    private static Map<Integer, List<String>> mTagMap;

    public BaseDialogManager() {
        mAtomicId = new AtomicInteger();
    }

    public static BaseDialogManager getInstance() {
        if (instance == null) {
            instance = new BaseDialogManager();
            mTagMap = new HashMap<>();
        }
        return instance;
    }

    public List<String> getVisibleDialogTags(Bundle savedInstanceState) {
        mDialogsId = savedInstanceState.getInt(DIALOGS_KEY);
        List<String> dialogTags = null;
        if (mTagMap.containsKey(mDialogsId)) {
            dialogTags = mTagMap.get(mDialogsId);
        }
        mTagMap.clear();
        return dialogTags;
    }

    public void saveVisibleDialogTags(List<? extends BaseDialog> dialogs, Bundle outState) {
        List<String> dialogTags = new ArrayList<>();
        for (BaseDialog dialog : dialogs) {
            if (!dialog.isHidden()) {
                dialogTags.add(dialog.dialogTag);
            }
        }

        mDialogsId = mAtomicId.incrementAndGet();
        mTagMap.put(mDialogsId, dialogTags);
        outState.putInt(DIALOGS_KEY, mDialogsId);
    }

    public static void displayFragmentDialog(BaseDialog dialog, FragmentManager fm) {
        dialog.show(fm, dialog.dialogTag);
    }

    public static void removeFromFragmentManager(BaseDialog dialog, FragmentManager fm) {
        fm.beginTransaction().remove(dialog).commit();
    }

    public static void removeFromFragmentManager(List<? extends BaseDialog> dialogs, FragmentManager fm) {
        if (!CollectionUtils.isEmpty(dialogs)) {
            for (BaseDialog dialog : dialogs) {
                removeFromFragmentManager(dialog, fm);
            }
        }
    }
}
