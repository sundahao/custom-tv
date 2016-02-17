package com.qgd.commons.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.qgd.commons.tv.R;
import com.qgd.commons.tv.util.AnimateFactory;

/**
 * 作者：ethan on 2015/12/29 17:13
 * 邮箱：ethan.chen@fm2020.com
 */
public class TvLinearLayout extends LinearLayout implements View.OnFocusChangeListener {
    private Drawable mBorderDrawable;
    private Rect mBound;
     private Rect mRect;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;
    private int borderSize = 11;

    private boolean mScaleable = true;


    public TvLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TvLinearLayout);

        mScaleable = a.getBoolean(R.styleable.TvLinearLayout_scaleable, true);

        int borderResId = a.getResourceId(R.styleable.TvLinearLayout_borderDrawable, R.drawable.white_light_10);

        mBorderDrawable = getResources().getDrawable(borderResId);


        borderSize=a.getDimensionPixelSize(R.styleable.TvLinearLayout_borderSize,borderSize);

        a.recycle();
    }

    protected void init() {

        setFocusable(true);
        setClickable(true);
        setWillNotDraw(false);
        mRect = new Rect();
        mBound = new Rect();
        mBorderDrawable = getResources().getDrawable(R.drawable.white_light_10);
        super.setOnFocusChangeListener(this);
    }

    private OnFocusChangeListener mOnFocusChangeListener;

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }
    public boolean isScaleable() {
        return mScaleable;
    }

    public void setScaleable(boolean mScaleable) {
        this.mScaleable = mScaleable;
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, b);
        }

        if (!mScaleable)
            return;
        if (b) {
            AnimateFactory.zoomInView(view);
        } else {
            AnimateFactory.zoomOutView(view);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        if (hasFocus()) {
            super.getDrawingRect(mRect);
            mBound.set(-borderSize + mRect.left, -borderSize + mRect.top, borderSize + mRect.right, borderSize + mRect.bottom);
            mBorderDrawable.setBounds(mBound);
            canvas.save();
            mBorderDrawable.draw(canvas);
            canvas.restore();
        }
        super.draw(canvas);
    }
}
