package group.g203.countables.path.main.view;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.base.view.ItemTouchHelperViewHolder;
import group.g203.countables.path.main.presenter.CountableViewHolderPresenter;

public class CountableViewHolder extends RecyclerView.ViewHolder implements BaseView, ItemTouchHelperViewHolder {

    @Bind(R.id.tv_countable_title)
    public TextView tvTitle;
    @Bind(R.id.iv_drag)
    public ImageView ivDrag;
    @Bind(R.id.tv_last_completed)
    public TextView tvLastCompleted;
    @Bind(R.id.tv_completed_count)
    public TextView tvCompletedCount;
    @Bind(R.id.iv_accountable)
    public ImageView ivAccountable;
    @Bind(R.id.iv_reminder)
    public ImageView ivReminder;
    CountableViewHolderPresenter mPresenter;

    public CountableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        mPresenter = (CountableViewHolderPresenter) presenter;
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.bright_faded_app_green));
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
