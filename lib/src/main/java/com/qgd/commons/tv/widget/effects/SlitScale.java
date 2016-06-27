package com.qgd.commons.tv.widget.effects;

import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.qgd.commons.tv.widget.animation.ObjectAnimator;

public class SlitScale extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        mDuration=700;

        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "rotationY", 120,88,68,45,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0,0.4f,0.8f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view,"scaleY",0.3f, 1).setDuration(mDuration)
        );
        getAnimatorSet().setInterpolator( new AccelerateInterpolator());

     }
}
