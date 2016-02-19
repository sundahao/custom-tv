package com.qgd.commons.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.qgd.commons.tv.R;

/**
 * 作者：ethan on 2016/2/19 10:44
 * 邮箱：ethan.chen@fm2020.com
 */
public class DashedLineView extends View {

    private Paint paint;
    private Path path;
    PathEffect effects;
    private static final String namespace ="http://schemas.android.com/apk/res/android";

    public DashedLineView(Context context) {
        super(context);
        init(context,null);
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DashedLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        paint = new Paint();
        path = new Path();
        effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.white));
        paint.setPathEffect(effects);
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashedLineView);
            int color = a.getColor(R.styleable.DashedLineView_lineColor, Color.WHITE);
            paint.setColor(color);

        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        if (measuredHeight <= measuredWidth) {
            path.moveTo(0, getMeasuredHeight()/2);
            path.lineTo(getMeasuredWidth(), getMeasuredHeight()/2);
        }else{
            path.moveTo(getMeasuredWidth()/2,0);
            path.lineTo(getMeasuredWidth()/2,getMeasuredHeight());
        }
        canvas.drawPath(path, paint);
    }


}
