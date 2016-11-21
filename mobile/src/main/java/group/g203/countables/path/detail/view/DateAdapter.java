package group.g203.countables.path.detail.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group.g203.countables.R;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.model.DateField;
import group.g203.countables.path.detail.presenter.DateViewHolderPresenter;
import io.realm.RealmList;

public class DateAdapter extends RecyclerView.Adapter<DateViewHolder> {

    private RealmList<DateField> mDataModels;
    DateViewHolderPresenter mPresenter;

    public DateAdapter(RealmList<DateField> dates) {
        mDataModels = dates;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_row_layout, parent, false);
        DateViewHolder holder = new DateViewHolder(v);
        holder.setPresenter(((DetailActivity)parent.getContext()).getPresenter());
        return holder;
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        setPresenter(holder);
        mPresenter.handleDateColor(holder.tvDate);
        mPresenter.setDateFormat(holder.tvDate, mDataModels.get(position).date);
    }

    @Override
    public int getItemCount() {
        return (CollectionUtils.isEmpty(mDataModels, true)) ? 0 : mDataModels.size();
    }

    public void setData(RealmList<DateField> mDataModels) {
        this.mDataModels = mDataModels;
    }

    void setPresenter(DateViewHolder holder) {
        if (mPresenter == null) {
            mPresenter = holder.getPresenter();
        }
    }
}
