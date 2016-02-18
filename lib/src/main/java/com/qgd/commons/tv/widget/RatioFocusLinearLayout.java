package com.qgd.commons.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * 作者：ethan on 2016/2/17 15:59
 * 邮箱：ethan.chen@fm2020.com
 */
public class RatioFocusLinearLayout extends LinearLayout {
    private int selectedIndex = -1;
    private boolean enable = false;


    public RatioFocusLinearLayout(Context context) {
        super(context);
        init();
    }

    public RatioFocusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatioFocusLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        this.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (indexOfChild(newFocus) >= 0 && indexOfChild(oldFocus) < 0) {//进来
                    int i = 0;
                } else if (indexOfChild(newFocus) < 0 && indexOfChild(oldFocus) >= 0) {//出去
                    int k = 0;
                    for (int i = 0; i < getChildCount(); i++) {
                        View v = getChildAt(i);
                        if (v != oldFocus) {
                            v.setFocusable(false);
                        }
                    }

                } else if (indexOfChild(newFocus) < 0 && indexOfChild(oldFocus) < 0) {//外面
                    int i = 0;
                } else if (indexOfChild(newFocus) >= 0 && indexOfChild(oldFocus) >= 0) {//里面
                    newFocus.setFocusable(true);
                } else {
                    int i = 0;
                }

            }
        });
    }

    @Override
    public void requestChildFocus(View child, View focused) {

        super.requestChildFocus(child, focused);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setFocusable(true);
        }


    }
}
