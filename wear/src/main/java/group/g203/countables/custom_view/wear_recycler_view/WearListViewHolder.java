package group.g203.countables.custom_view.wear_recycler_view;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;
import android.widget.TextView;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.GeneralPresenter;
import group.g203.countables.base.view.GeneralView;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.DetailActivity;

public class WearListViewHolder extends WearableRecyclerView.ViewHolder implements GeneralView {

    private static final float ALPHA_CONST = 1f;
    private static final int LOG_INDEX = 0;
    private static final int ACCT_INDEX = 1;
    private static final int REMINDER_INDEX = 2;

    public Countable mCountable;
    Context mContext;
    String mType;
    CircledImageView mIcon;
    TextView mName;
    ViewHolderPresenter mPresenter;

    public WearListViewHolder(View itemView, String type) {
        super(itemView);
        mContext = itemView.getContext();
        mIcon = (CircledImageView) itemView.findViewById(R.id.ivIcon);
        mName = (TextView) itemView.findViewById(R.id.tvName);
        this.mType = type;
        if (mType.equals(Constants.MAIN)) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCountable != null) {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra(Constants.COUNTABLE, mCountable);
                        mContext.startActivity(intent);
                    } else {
                        // open on phone
                    }
                }
            });
        } else if (mType.equals(Constants.DETAIL)) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public <P extends GeneralPresenter> void setPresenter(P presenter) {
        mPresenter = (ViewHolderPresenter) presenter;
    }

    public <P extends GeneralPresenter> P getPresenter() {
        return (P) mPresenter;
    }

}
