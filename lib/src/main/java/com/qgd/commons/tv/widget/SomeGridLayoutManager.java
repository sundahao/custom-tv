package com.qgd.commons.tv.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：ethan on 2016/2/17 13:20
 * 邮箱：ethan.chen@fm2020.com
 */
public class SomeGridLayoutManager extends GridLayoutManager {

    private final Context context;

    public SomeGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        this.context = context;
    }

    public SomeGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        this.context = context;
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return null;
    }
}
