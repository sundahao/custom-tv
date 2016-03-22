package com.qgd.commons.tv.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 作者：ethan on 2015/12/10 17:09
 * 邮箱：ethan.chen@fm2020.com
 */
public class AnimateFactory {
    /**
     * 缩放动画,用于缩放控件
     *
     * @param startScale 控件的起始尺寸倍率
     * @param endScale   控件的终点尺寸倍率
     * @return
     */
    public static Animation zoomAnimation(float startScale, float endScale, int duration) {
        ScaleAnimation anim = new ScaleAnimation(startScale, endScale, startScale, endScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setFillAfter(true);
        anim.setDuration(duration);
        return anim;
    }

    public static Animation zoomAnimation(float startScaleX, float endScaleX, float startScaleY, float endScaleY, int duration) {
        ScaleAnimation anim = new ScaleAnimation(startScaleX, endScaleX, startScaleY, endScaleY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setFillAfter(true);
        anim.setDuration(duration);
        return anim;
    }

    public static Animation shakeAnimate() {
        TranslateAnimation mAnimate = new TranslateAnimation(0, 5, 0, 0);
        mAnimate.setInterpolator(new CycleInterpolator(50));
        mAnimate.setDuration(600);
        return mAnimate;
    }

    public static void zoomInView(View v) {
        zoomInView(v, 1.1f);
    }

    public static void zoomOutView(View v) {
        zoomOutView(v, 1.1f);
    }

    public static void zoomInView(View v, float zoomSize) {
        if (v != null) {
            v.startAnimation(AnimateFactory.zoomAnimation(1.0f, zoomSize, 200));
        }
    }


    private static float fixSize = 24.0f;

    public static void zoomInViewFix(View v) {
        if (v != null)
            zoomInViewFix(v, v.getMeasuredWidth(), v.getMeasuredHeight());
    }


    public static void zoomInViewFix(View v, int width, int height) {
        if (v != null) {
            if (width == 0) {
                //                int ww = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
                //                int hh = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                //                v.measure(ww, hh);
                //                width = v.getMeasuredWidth();
                v.startAnimation(AnimateFactory.zoomAnimation(1.0f, 1.1f, 200));
                return;
            }
            float scaleWidth = (width + fixSize) / width * 1.0f;
            //float scaleHeight = (height + fixSize) / height * 1.0f;
            v.startAnimation(AnimateFactory.zoomAnimation(1.0f, scaleWidth, 200));
            //v.startAnimation(AnimateFactory.zoomAnimation(1.0f, scaleWidth, 1.0f, scaleHeight, 200));
        }
    }

    public static void zoomOutViewFix(View v) {
        if (v != null)
            zoomOutViewFix(v, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    public static void zoomOutViewFix(View v, int width, int height) {
        if (v != null) {
            if (width == 0) {
                //                int ww = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                //                int hh = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                //                v.measure(ww, hh);
                //                width = v.getWidth();
                v.startAnimation(AnimateFactory.zoomAnimation(1.1f, 1.0f, 200));
                return;
            }
            float scaleWidth = (width + fixSize) / width * 1.0f;
            //float scaleHeight = (height + fixSize) / height * 1.0f;
            v.startAnimation(AnimateFactory.zoomAnimation(scaleWidth, 1.0f, 200));

            //v.startAnimation(AnimateFactory.zoomAnimation(scaleWidth, 1.0f, scaleHeight, 1.0f, 200));
        }
    }


    private static void setSize(View v, float zoomSize) {
        int h = v.getMeasuredHeight();
        int w = v.getMeasuredWidth();
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.width = (int) (w * zoomSize);
        lp.height = (int) (h * zoomSize);
        v.setLayoutParams(lp);
        v.invalidate();
    }

    public static void zoomOutView(View v, float zoomSize) {
        if (v != null) {
            v.startAnimation(AnimateFactory.zoomAnimation(zoomSize, 1.0f, 200));
        }
    }


    public static void startFlick(View view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 取消View闪烁效果
     */
    public static void stopFlick(View view) {

        if (null == view) {

            return;

        }
        view.clearAnimation();

    }

    public static void zoomInView(View v, float zoomSize, int duration) {
        if (v != null) {
            v.startAnimation(AnimateFactory.zoomAnimation(1.0f, zoomSize, duration));
        }
    }

    public static void zoomOutView(View v, float zoomSize, int duration) {
        if (v != null) {
            v.startAnimation(AnimateFactory.zoomAnimation(zoomSize, 1.0f, duration));
        }
    }

}
