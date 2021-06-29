package com.fzu.daoyunmobile.Holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fzu.daoyunmobile.Bean.TreeBean;
import com.fzu.daoyunmobile.R;

public class TrunkHolder extends BaseTelecomHolder {

    private Context mContext;
    private View view;

    public TrunkHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    //设置bindView 获取pos
    public void bindView(final TreeBean rdb, final int position, final TreeItemClickListener listener) {
        ((TextView) view.findViewById(R.id.trunk_1)).setText(rdb.getTrunk1());
        //父布局OnClick监听
        view.setOnClickListener(view -> {
            if (listener != null) {
                if (rdb.isExpand()) {
                    try {
                        listener.onHideChildren(rdb);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rdb.setExpand(false);
                } else {
                    try {
                        listener.onExpandChildren(rdb);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rdb.setExpand(true);
                }
            }
        });
    }
}