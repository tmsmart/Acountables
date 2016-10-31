package group.g203.countables.custom_view.loading_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.presenter.BasePresenter;

public class LoadingAspect extends RelativeLayout implements LoadingView {

    private final static int CONTENT_CHILD_POSITION = 3;

    @Bind(R.id.loading_indicator)
    public ProgressBar mIndicator;
    @Bind(R.id.empty_icon)
    public ImageView mEmptyIcon;
    @Bind(R.id.empty_message)
    public TextView mEmptyMessage;
    public View mContent;
    LoadingPresenter mPresenter;

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
        View view = inflate(getContext(), R.layout.loading_aspect_view, this);
        ButterKnife.bind(view);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mContent = this.getChildAt(CONTENT_CHILD_POSITION);
    }

    @Override
    public void onDisplayLoading() {
        mPresenter.displayLoading();
    }

    @Override
    public void onDisplayContent() {
        mPresenter.displayContent();
    }

    @Override
    public void onDisplayEmptyView() {
        mPresenter.displayEmptyView();
    }

    public void onSetEmptyMessage(String message) {
        mPresenter.setEmptyMessage(message);
    }

    public void onSetEmptyIcon(int iconResource) {
        mPresenter.setEmptyIcon(iconResource);
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof LoadingPresenter) {
            mPresenter = (LoadingPresenter) presenter;
        } else {
            mPresenter.displayToast(getContext().getString(R.string.view_error));
        }
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }
}
