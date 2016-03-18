package com.qgd.commons.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.qgd.commons.tv.R;
import com.qgd.commons.tv.util.AnimateFactory;
import com.qgd.commons.tv.util.DimensionConvert;
import com.qgd.commons.tv.util.FocusSoundUtil;


/**
 * 作者：ethan on 2015/12/28 11:09
 * 邮箱：ethan.chen@fm2020.com
 */
public class TvTextView extends TextView implements View.OnFocusChangeListener {


    private Rect mBound;
    private Drawable mBorderDrawable;
    private Rect mRect;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;
    private int borderSize = 15;

    private boolean mScaleable = true;
    private String mKeyNumber;

    private Paint mPaint;
    private OnFocusChangeListener mOnFocusChangeListener;

    public TvTextView(Context context) {
        super(context);
        init();
    }

    public TvTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TvTextView);

        mScaleable = a.getBoolean(R.styleable.TvTextView_scaleable, true);
        mKeyNumber = a.getString(R.styleable.TvTextView_number);
        int berderResId = a.getResourceId(R.styleable.TvTextView_borderDrawable, R.drawable.white_light);
        mBorderDrawable = getResources().getDrawable(berderResId);

        int numberColor = a.getColor(R.styleable.TvTextView_numberColor, Color.WHITE);
        mPaint.setColor(numberColor);

        borderSize=a.getDimensionPixelSize(R.styleable.TvTextView_borderSize,borderSize);

        a.recycle();


    }
    public void setNumber(String number){
        mKeyNumber=number;
        postInvalidate();
    }

    public boolean isScaleable() {
        return mScaleable;
    }

    public void setScaleable(boolean mScaleable) {
        this.mScaleable = mScaleable;
    }

    protected void init() {

        setFocusable(true);
        setClickable(true);
        setWillNotDraw(false);
        mRect = new Rect();
        mBound = new Rect();
        if(mBorderDrawable==null)
            mBorderDrawable = getResources().getDrawable(R.drawable.white_light_10);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(DimensionConvert.px2dip(getContext(),25));

        super.setOnFocusChangeListener(this);
        FocusSoundUtil.initSoundEffect(this.getContext() );

    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener=l;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(mOnFocusChangeListener!=null){
            mOnFocusChangeListener.onFocusChange(view,b);
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
        super.draw(canvas);

        if (hasFocus()) {
            super.getDrawingRect(mRect);
            mBound.set(-borderSize + mRect.left, -borderSize + mRect.top, borderSize + mRect.right-1, borderSize + mRect.bottom-1);
            mBorderDrawable.setBounds(mBound);
            canvas.save();
            if (mBorderDrawable != null)
                mBorderDrawable.draw(canvas);
            canvas.restore();
        }
    }


    protected void drawCenterNumberText(Canvas canvas, String text, Paint textPaint) {
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
        canvas.drawText(text, xPos, yPos, textPaint);
    }

    protected void drawBottomNumberText(Canvas canvas, String text, Paint textPaint) {
        float strWidth = textPaint.measureText(text);
        int xPos = (canvas.getWidth() / 2) - (int) strWidth / 2;
        int yPos = (int) ((canvas.getHeight()) + ((textPaint.descent() + textPaint.ascent()) / 2) / 2);
        canvas.drawText(text, xPos, yPos, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mKeyNumber!=null&&!"".equals(mKeyNumber) ) {
            super.getDrawingRect(mRect);
            drawBottomNumberText(canvas, "" + mKeyNumber, mPaint);
        }

        super.onDraw(canvas);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        FocusSoundUtil.dispatchKeyEvent(this, event);
        return super.dispatchKeyEvent(event);
    }

}
