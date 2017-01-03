package group.g203.countables.custom_view.wear_recycler_view;

import android.content.Context;
import android.support.wearable.view.CircledImageView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import group.g203.countables.R;

public class WearListAspect extends LinearLayout {

    public CircledImageView mIcon;
    public TextView mName;

    public WearListAspect(Context context) {
        super(context);
        initViews();
    }

    public WearListAspect(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public WearListAspect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public WearListAspect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void initViews() {
        mIcon = (CircledImageView) findViewById(R.id.ivIcon);
        mName = (TextView) findViewById(R.id.tvName);
    }

}
