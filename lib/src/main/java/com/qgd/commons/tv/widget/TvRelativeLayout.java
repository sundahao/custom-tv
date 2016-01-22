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

    private final Drawable mBorderDrawable;
    private Rect mBound;
    private Drawable mDrawable;
    private Rect mRect;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;
    private int borderSize ;

    private boolean mScaleable = true;


    public TvRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TvTextView);

        mScaleable = a.getBoolean(R.styleable.TvTextView_scaleable, true);

        int borderResId = a.getResourceId(R.styleable.TvRelativeLayout_borderDrawable, R.drawable.select_border2);
        mBorderDrawable = getResources().getDrawable(borderResId);

//        borderSize=a.getDimensionPixelSize(R.styleable.TvRelativeLayout_borderSize,R.dimen.default_border_size);
        borderSize=11;
        a.recycle();
    }

    protected void init() {

        setFocusable(true);
        setClickable(true);
        setWillNotDraw(false);
        mRect = new Rect();
        mBound = new Rect();
        mDrawable = getResources().getDrawable(R.drawable.select_border2);
        this.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
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
            mDrawable.setBounds(mBound);
            canvas.save();
            mDrawable.draw(canvas);
            canvas.restore();
        }

        super.draw(canvas);
    }

}