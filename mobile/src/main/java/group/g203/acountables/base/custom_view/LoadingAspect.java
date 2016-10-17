package group.g203.acountables.base.custom_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import group.g203.acountables.R;
import group.g203.acountables.base.presenter.BaseViewPresenter;

public class LoadingAspect extends RelativeLayout implements LoadingView {

    public LoadingAspect(Context context) {
        super(context);
        init();
    }

    public LoadingAspect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingAspect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingAspect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.loading_aspect_view, this);
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void finishLoading() {

    }

    @Override
    public void setEmptyIcon() {

    }

    @Override
    public void setEmptyMessage() {

    }

    @Override
    public <P extends BaseViewPresenter> void setPresenter(P presenter) {

    }
}
