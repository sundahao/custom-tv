package com.qgd.commons.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

import com.qgd.commons.tv.R;
import com.qgd.commons.tv.util.AnimateFactory;


/**
 * 作者：ethan on 2016/1/14 14:31
 * 邮箱：ethan.chen@fm2020.com
 */
public class TvEditText extends EditText implements View.OnFocusChangeListener {
    private Rect mBound;
    private Drawable mDrawable;
    private Rect mRect;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;
    private int borderSize = 12;

    private boolean scaleable=false;



    public TvEditText(Context context) {
        super(context);
        init();
    }

    public TvEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TvTextView);

        scaleable = a.getBoolean(R.styleable.TvTextView_scaleable, false);
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
    private OnFocusChangeListener mOnFocusChangeListener;
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener=l;
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        if(!scaleable)
            return;
        if (b) {
            AnimateFactory.zoomInView(view);
        } else {
            AnimateFactory.zoomOutView(view);
        }
        if(mOnFocusChangeListener!=null){
            mOnFocusChangeListener.onFocusChange(view,b);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (hasFocus()) {
            super.getDrawingRect(mRect);
            mBound.set(-borderSize + mRect.left, -borderSize + mRect.top , borderSize + mRect.right , borderSize + mRect.bottom);
            mDrawable.setBounds(mBound);
            canvas.save();
            mDrawable.draw(canvas);
            canvas.restore();
        }

        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }
}
