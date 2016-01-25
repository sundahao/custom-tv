package com.qgd.commons.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * 作者：ethan on 2015/12/17 09:57
 * 邮箱：ethan.chen@fm2020.com
 */
//跑马灯滚动
public class AlwaysMarqueeTextView extends TextView{
    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
