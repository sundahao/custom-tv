package com.qgd.commons.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.qgd.commons.tv.R;
import com.qgd.commons.tv.util.AnimateFactory;


/**
 * 作者：ethan on 2015/12/29 17:13
 * 邮箱：ethan.chen@fm2020.com
 */
public class TvRelativeLayout extends RelativeLayout implements View.OnFocusChangeListener {

    private Drawable mBorderDrawable;
    private Rect mBound;
    private Rect mRect;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;
    private int borderSize = 15;

    private boolean mScaleable = true;


    public TvRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TvTextView);

        mScaleable = a.getBoolean(R.styleable.TvRelativeLayout_scaleable, true);

        int borderResId = a.getResourceId(R.styleable.TvRelativeLayout_borderDrawable, R.drawable.white_light);
        mBorderDrawable = getResources().getDrawable(borderResId);
        borderSize=a.getDimensionPixelSize(R.styleable.TvRelativeLayout_borderSize,borderSize);

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
    public boolean isScaleable() {
        return mScaleable;
    }

    public void setScaleable(boolean mScaleable) {
        this.mScaleable = mScaleable;
    }
    private OnFocusChangeListener mOnFocusChangeListener;

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, b);
        }

        if (!mScaleable)
            return;
        if (b) {
            AnimateFactory.zoomInViewFix(view);
        } else {
            AnimateFactory.zoomOutViewFix(view);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        if (hasFocus()) {
            super.getDrawingRect(mRect);
            mBound.set(-borderSize + mRect.left, -borderSize + mRect.top, borderSize + mRect.right-1, borderSize + mRect.bottom-1);
            mBorderDrawable.setBounds(mBound);
            canvas.save();
            mBorderDrawable.draw(canvas);
            canvas.restore();
        }

        super.draw(canvas);
    }

}
