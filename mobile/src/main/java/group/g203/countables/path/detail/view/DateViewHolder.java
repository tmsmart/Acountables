package group.g203.countables.path.detail.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import group.g203.countables.R;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.path.detail.presenter.DateViewHolderPresenter;

public class DateViewHolder extends RecyclerView.ViewHolder implements BaseView {

    @Bind(R.id.tvDate)
    public TextView tvDate;
    @Bind(R.id.ivRemove)
    public ImageView ivRemove;
    DateViewHolderPresenter mPresenter;

    public DateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public <P extends BasePresenter> void setPresenter(P presenter) {
        mPresenter = (DateViewHolderPresenter) presenter;
    }

    @Override
    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }

}
