package com.qgd.commons.tv.widget;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by evil on 1/3/16.
 */
public interface TvTabPagerIndicatorPageAdapterInterface {
    public int getTabCount();
    public Object getTabItem(int position);
    public long getTabItemId(int position);
    public View getTabView(int position, View convertView, ViewGroup parent);

}
