package com.qgd.commons.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/3/2 0002.
 */
public class AlwaysTopLinearLayout extends LinearLayout {
    private  int position;
    public AlwaysTopLinearLayout(Context context) {
        this(context , null);
    }

    public AlwaysTopLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public AlwaysTopLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setChildrenDrawingOrderEnabled(true);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        int pos = indexOfChild(focused);
        //最后一个
        if (direction == View.FOCUS_RIGHT && pos == (getChildCount() - 1)) {
            return null;
        } else {
            return super.focusSearch(focused, direction);
        }
    }

    public void setCurrentPosition(int pos) {
        this.position = pos;
    }

    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(enabled);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View v = getFocusedChild();
        int pos = indexOfChild(v);
        if (pos >= 0 && pos < childCount)
            setCurrentPosition(pos);
        if (i == childCount - 1) {//这是最后一个需要刷新的item
            return position;
        }
        if (i == position) {//这是原本要在最后一个刷新的item
            return childCount - 1;
        }
        return i;//正常次序的item
    }

}
