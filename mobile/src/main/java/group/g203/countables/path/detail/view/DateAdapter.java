package group.g203.countables.path.detail.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

import group.g203.countables.R;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.path.detail.presenter.DateViewHolderPresenter;

public class DateAdapter extends RecyclerView.Adapter<DateViewHolder> {

    private ArrayList<Date> mDates;
    DateViewHolderPresenter mPresenter;

    public DateAdapter(ArrayList<Date> dates) {
        mDates = dates;
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
        mPresenter.setDateFormat(holder.tvDate, mDates.get(position));
        mPresenter.handleDateColor(holder.tvDate);
    }

    @Override
    public int getItemCount() {
        return (CollectionUtils.isEmpty(mDates, true)) ? 0 : mDates.size();
    }

    public void setData(ArrayList<Date> mDataModels) {
        this.mDates = mDataModels;
    }

    void setPresenter(DateViewHolder holder) {
        if (mPresenter == null) {
            mPresenter = holder.getPresenter();
        }
    }
}
