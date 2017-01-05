package group.g203.countables.custom_view.wear_recycler_view;

import android.content.Context;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.main.view.MainActivity;

public class WearListAdapter extends WearableRecyclerView.Adapter<WearListViewHolder> {

    ArrayList<Countable> mData;
    ArrayList<String> mStringData;
    Countable mDetailCountable;
    String mType;
    Context mContext;
    ViewHolderPresenter mPresenter;

    public WearListAdapter(ArrayList<Countable> mData, Context mContext) {
        this.mData = mData;
        this.mType = Constants.MAIN;
        this.mContext = mContext;
    }

    public WearListAdapter(ArrayList<String> mStringData, Countable countable, Context mContext) {
        this.mStringData = mStringData;
        this.mType = Constants.DETAIL;
        this.mContext = mContext;
        this.mDetailCountable = countable;
    }

    @Override
    public WearListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.countable_row_layout, parent, false);
        WearListViewHolder holder = new WearListViewHolder(v, mType);
        if (mType.equals(Constants.MAIN)) {
            holder.setPresenter(((MainActivity)parent.getContext()).getMainPresenter());
        } else {
            holder.setPresenter(((DetailActivity)parent.getContext()).getDetailPresenter());
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(WearListViewHolder holder, int position) {
            setPresenter(holder);
            if (mType.equals(Constants.MAIN)) {
                Countable countable = mData.get(position);
                if (countable != null) {
                    holder.mCountable = countable;
                    mPresenter.handleTextView(holder.mName, mData.get(position).name);
                    mPresenter.handleRowIcon(holder.mIcon, R.mipmap.ic_arrow_right);
                } else {
                    mPresenter.handleTextView(holder.mName, mContext.getString(R.string.common_open_on_phone));
                    mPresenter.handleRowIcon(holder.mIcon, R.drawable.common_full_open_on_phone);
                }
            } else if (mType.equals(Constants.DETAIL)) {
                holder.mCountable = mDetailCountable;
                switch (position) {
                    case 0:
                        mPresenter.handleRowIcon(holder.mIcon, R.mipmap.ic_plus_one);
                        break;
                    case 1:
                        mPresenter.handleRowIcon(holder.mIcon, R.mipmap.ic_accountable);
                        break;
                    case 2:
                        mPresenter.handleRowIcon(holder.mIcon, R.mipmap.ic_notification);
                        break;
                }
                mPresenter.handleTextView(holder.mName, mStringData.get(position));
            }
    }

    @Override
    public int getItemCount() {
        if (!CollectionUtils.isEmpty(mData)) {
            return mData.size();
        } else if (!CollectionUtils.isEmpty(mStringData)) {
            return mStringData.size();
        } else {
            return 0;
        }
    }

    void setPresenter(WearListViewHolder holder) {
        if (mPresenter == null) {
            mPresenter = holder.getPresenter();
        }
    }
}
