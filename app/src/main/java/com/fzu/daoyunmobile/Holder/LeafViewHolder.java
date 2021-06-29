package com.fzu.daoyunmobile.Holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.fzu.daoyunmobile.Activities.RegisterActivity;
import com.fzu.daoyunmobile.Activities.SelectFacultyActivity;
import com.fzu.daoyunmobile.Bean.TreeBean;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;

public class LeafViewHolder extends BaseTelecomHolder {

    private Context mContext;
    private View view;

    public TextView leaf1;

    public LeafViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final TreeBean rdb, final int pos) {
        leaf1 = view.findViewById(R.id.leaf_1);
        leaf1.setText(rdb.getLeaf1());
        leaf1.setOnClickListener(v -> {

            AlertDialogUtil.showConfirmClickAlertDialogTwoButtonWithLister("确定院校选择" + rdb.getTrunk1() + rdb.getLeaf1() + "?", (Activity) mContext, (dialog, i) -> {
                Activity selectAct = ((Activity) mContext);


                Intent intent = new Intent();
                //设置学校
                intent.putExtra("school", rdb.getTrunk1());
                //设置院系
                intent.putExtra("academy", rdb.getLeaf1());

                intent.putExtra("schoolID", SelectFacultyActivity.idDict.get(rdb.getTrunk1()));
                intent.putExtra("academyID", SelectFacultyActivity.idDict.get(rdb.getLeaf1()));
                // 设置返回码和返回携带的数据
                selectAct.setResult(Activity.RESULT_OK, intent);
                selectAct.finish();
            });

        });
    }
}