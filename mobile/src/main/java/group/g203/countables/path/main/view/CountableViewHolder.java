package group.g203.countables.path.main.view;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.base.view.ItemTouchHelperViewHolder;
import group.g203.countables.path.detail.view.DetailActivity;
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

    public CountableViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                int countableIndex = getAdapterPosition();
                intent.putExtra(Constants.COUNTABLE_INDEX, countableIndex);
                itemView.getContext().startActivity(intent);
            }
        });
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
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.faded_bright_app_green));
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
