package com.qgd.commons.tv.widget.effects;

import android.view.View;

import com.qgd.commons.tv.widget.animation.ObjectAnimator;

public class SlitScale extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "rotationY", 90,88,88,45,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0,0.4f,0.8f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleX", 0,0.9f, 0.1f, 0.9f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view,"scaleY",0,0.9f, 0.1f, 0.9f, 1).setDuration(mDuration)
        );
    }
}
