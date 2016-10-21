package group.g203.acountables.base.manager;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import group.g203.acountables.base.presenter.BasePresenter;

public class BasePresenterManager {

    private static final String PRESENTER_KEY = "presenterKey";
    private final AtomicInteger mAtomicId;
    private int mPresenterId;
    private static BasePresenterManager instance;
    private static Map<Integer, BasePresenter> mPresenterMap;

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

    public <P extends BasePresenter> P getPresenter(Bundle savedInstanceState) {
        mPresenterId = savedInstanceState.getInt(PRESENTER_KEY);
        P presenter = null;
        if (mPresenterMap.containsKey(mPresenterId)) {
            presenter = (P) mPresenterMap.get(mPresenterId);
        }
        mPresenterMap.clear();
        return presenter;
    }

    public <P extends BasePresenter> void savePresenter(P presenter, Bundle outState) {
        mPresenterId = mAtomicId.incrementAndGet();
        mPresenterMap.put(mPresenterId, presenter);
        outState.putInt(PRESENTER_KEY, mPresenterId);
    }
}
