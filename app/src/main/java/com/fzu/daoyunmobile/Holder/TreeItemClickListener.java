package com.fzu.daoyunmobile.Holder;

import com.fzu.daoyunmobile.Bean.TreeBean;

public interface TreeItemClickListener {
    /**
     * 展开子Item
     *
     * @param bean
     */
    void onExpandChildren(TreeBean bean) throws InterruptedException;

    /**
     * 隐藏子Item
     *
     * @param bean
     */
    void onHideChildren(TreeBean bean) throws InterruptedException;
}
