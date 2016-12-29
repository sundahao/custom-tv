/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.qgd.commons.tv.R;
import com.qgd.commons.tv.util.DimensionConvert;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yangke on 2016-07-01.
 */
public class MarqueeSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private float fps = 30;
    private String text = "";
    private int textColor = Color.WHITE;
    private int backgroundColor = Color.BLACK;
    private float spaceWidth = 150;
    private float textSize = 32;
    private boolean autoStart = true;
    private long startDelay = 0;

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint backgroundPaint = new Paint();
    private float textWidth;
    private float textBaseline;
    private AtomicBoolean stopped = new AtomicBoolean(true);
    private int posX = 0;
    private boolean drawed = false;
    private int step = DimensionConvert.dip2px(getContext(),1);

    public MarqueeSurfaceView(Context context) {
        this(context, null, 0);
    }

    public MarqueeSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeSurfaceView);
            fps = typedArray.getFloat(R.styleable.MarqueeSurfaceView_fps, 30);
            text = typedArray.getString(R.styleable.MarqueeSurfaceView_text);
            textSize = typedArray.getDimension(R.styleable.MarqueeSurfaceView_textSize, 32);
            textColor = typedArray.getColor(R.styleable.MarqueeSurfaceView_textColor, Color.WHITE);
            backgroundColor = typedArray.getColor(R.styleable.MarqueeSurfaceView_backgroundColor, Color.BLACK);
            spaceWidth = typedArray.getDimension(R.styleable.MarqueeSurfaceView_spaceWidth, 150);
            autoStart = typedArray.getBoolean(R.styleable.MarqueeSurfaceView_autoStart, true);
            startDelay = typedArray.getInt(R.styleable.MarqueeSurfaceView_startDelay, 0);
            typedArray.recycle();
        }

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        backgroundPaint.setColor(backgroundColor);

        calculateTextWidth();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        drawed = false;
        calculateTextWidth();
    }

    public void startMarquee() {
        if (stopped.getAndSet(false)) {
            new Thread(this).start();
        }
    }

    public void stopMarquee() {
        stopped.set(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        calculateTextBaseline();

        doUpdateView();

        if (autoStart) {
            startMarquee();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopMarquee();
    }

    @Override
    public void run() {
        long period = (long) (1000 / fps);
        long delay = startDelay;
        while (!stopped.get()) {
            try {
                if (!drawed) {
                    delay = startDelay;
                    doUpdateView();
                } else {
                    if (delay > 0) {
                        delay -= period;
                    } else {
                        doUpdateView();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
            }
        }
    }

    private void doUpdateView() {
        if (!drawed) {
            posX = 0;
            drawed = true;
            drawContent(getHolder());
        } else {
            if (textWidth > getWidth()) {
                posX -= step;
                if (posX < -textWidth - spaceWidth) {
                    posX = 0;
                }
            }
            drawContent(getHolder());
        }
    }

    private void drawContent(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }

        try {
            int canvasWidth = getWidth();
            canvas.drawRect(0, 0, canvasWidth, getHeight(), backgroundPaint);
            canvas.drawText(text, posX, textBaseline, textPaint);

            if (textWidth > canvasWidth && posX + textWidth + spaceWidth < canvasWidth) {
                canvas.drawText(text, posX + textWidth + spaceWidth, textBaseline, textPaint);
            }
        } finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void calculateTextBaseline() {
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textBaseline = getHeight() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
    }

    private void calculateTextWidth() {
        textWidth = text == null ? 0 : textPaint.measureText(text);
    }
}
