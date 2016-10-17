package group.g203.acountables.base.presenter;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BasePresenterManager {

    private static final String PRESENTER_KEY = "presenterKey";
    private final AtomicInteger mAtomicId;
    private int mPresenterId;
    private static BasePresenterManager instance;
    private static Map<Integer, BaseViewPresenter> mPresenterMap;

    public BasePresenterManager() {
        mAtomicId = new AtomicInteger();
    }

    public static BasePresenterManager getInstance() {
        if (instance == null) {
            instance = new BasePresenterManager();
            mPresenterMap = new HashMap<>();
        }
        return instance;
    }

    public <P extends BaseViewPresenter> P getPresenter(Bundle savedInstanceState) {
        mPresenterId = savedInstanceState.getInt(PRESENTER_KEY);
        P presenter = null;
        if (mPresenterMap.containsKey(mPresenterId)) {
            presenter = (P) mPresenterMap.get(mPresenterId);
        }
        mPresenterMap.clear();
        return presenter;
    }

    public <P extends BaseViewPresenter> void savePresenter(P presenter, Bundle outState) {
        mPresenterId = mAtomicId.incrementAndGet();
        mPresenterMap.put(mPresenterId, presenter);
        outState.putLong(PRESENTER_KEY, mPresenterId);
    }
}
