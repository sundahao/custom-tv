package com.qgd.commons.tv.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Administrator on 2016/2/18 0018.
 */
public class FocusRecyleView extends RecyclerView {
    public FocusRecyleView(Context context) {
        super(context);
        init();
    }

    public FocusRecyleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusRecyleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
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
