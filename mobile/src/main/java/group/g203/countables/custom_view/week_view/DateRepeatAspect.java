package group.g203.countables.custom_view.week_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.presenter.BasePresenter;

public class DateRepeatAspect extends LinearLayout implements DateRepeatView {

    @Bind(R.id.tvMonth)
    public TextView tvMonth;
    @Bind(R.id.dayOne)
    public LinearLayout llDayOne;
    @Bind(R.id.dayTwo)
    public LinearLayout llDayTwo;
    @Bind(R.id.dayThree)
    public LinearLayout llDayThree;
    @Bind(R.id.dayFour)
    public LinearLayout llDayFour;
    @Bind(R.id.dayFive)
    public LinearLayout llDayFive;
    @Bind(R.id.daySix)
    public LinearLayout llDaySix;
    @Bind(R.id.daySeven)
    public LinearLayout llDaySeven;
    @Bind(R.id.llRepeat)
    public LinearLayout llRepeat;
    DateRepeatPresenter mPresenter;

    public DateRepeatAspect(Context context) {
        super(context);
        init();
    }

    public DateRepeatAspect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateRepeatAspect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateRepeatAspect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        View view = inflate(getContext(), R.layout.date_time_repeat_aspect_view, this);
        ButterKnife.bind(view);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onSetActiveAspect() {
        mPresenter.setActiveAspect();
    }

    @Override
    public void onSetReadOnlyAspect() {
        mPresenter.setReadOnlyAspect();
    }

    @Override
    public void onSetRepeatOptionClicks() {
        mPresenter.setRepeatOptionClicks();
    }

    @Override
    public void onSetMonth() {
        mPresenter.setMonth();
    }

    @Override
    public void onSetDays() {
        mPresenter.setDays();
    }

    @Override
    public void onSetRepeatDay() {
        mPresenter.setRepeatDay();
    }

    @Override
    public void onSetRepeatDayWatcher() {
        mPresenter.setRepeatDayWatcher();
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof DateRepeatPresenter) {
            mPresenter = (DateRepeatPresenter) presenter;
        } else {
            mPresenter.displayToast(getContext().getString(R.string.view_error));
        }
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }
}
