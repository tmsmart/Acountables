package group.g203.countables.path.main.view;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import group.g203.countables.R;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.base.view.ItemTouchHelperAdapter;
import group.g203.countables.base.view.OnStartDragListener;
import group.g203.countables.model.Countable;
import group.g203.countables.path.main.presenter.CountableViewHolderPresenter;
import group.g203.countables.path.main.presenter.MainPresenter;
import io.realm.OrderedRealmCollection;

public class CountableAdapter extends RecyclerView.Adapter<CountableViewHolder> implements ItemTouchHelperAdapter {

    private OrderedRealmCollection<Countable> mDataModels;
    CountableViewHolderPresenter mPresenter;
    private OnStartDragListener mDragStartListener;

    public CountableAdapter(OrderedRealmCollection<Countable> countableData, OnStartDragListener dragStartListener) {
        mDataModels = countableData;
        mDragStartListener = dragStartListener;
    }

    @Override
    public CountableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.countable_cardview, parent, false);
        CountableViewHolder holder = new CountableViewHolder(v);
        holder.setPresenter(((MainActivity)parent.getContext()).getPresenter());
        return holder;
    }

    @Override
    public void onBindViewHolder(final CountableViewHolder holder, int position) {
        setPresenter(holder);
        mPresenter.setCountableTitle(holder.tvTitle, mDataModels.get(position).name);
        mPresenter.handleAccountableIcon(holder.ivAccountable, mDataModels.get(position));
        mPresenter.handleReminderIcon(holder.ivReminder, mDataModels.get(position));
        mPresenter.setLastCompletedText(holder.tvLastCompleted, mDataModels.get(position));
        mPresenter.setCompletedCount(holder.tvCompletedCount, mDataModels.get(position));

        holder.ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (CollectionUtils.isEmpty(mDataModels, true)) ? 0 : mDataModels.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        boolean increaseIndices = (fromPosition > toPosition) ? true : false;
        ((MainPresenter)mPresenter).reorderCountablesViaDrag(mDataModels.get(fromPosition), fromPosition, toPosition, increaseIndices);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        ((MainPresenter)mPresenter).deleteCountableViaSwipe(mDataModels.get(position));
        notifyItemRemoved(position);
    }

    public void setData(OrderedRealmCollection<Countable> mDataModels) {
        this.mDataModels = mDataModels;
    }

    void setPresenter(CountableViewHolder holder) {
        if (mPresenter == null) {
            mPresenter = holder.getPresenter();
        }
    }
}
