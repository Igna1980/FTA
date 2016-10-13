package com.bamobile.fdtks.animations;



import android.view.View;
import android.view.animation.AlphaAnimation;

public class ViewAnimations {

    public int mAnimationDuration = 1000;

    public void fadeOutFast(final View view) {
        AlphaAnimation alpha = new AlphaAnimation(0f, 0f);
        alpha.setDuration(1);
        alpha.setFillAfter(true);
        view.startAnimation(alpha);
    }

    public void fadeOut(final View view) {
        AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
        alpha.setDuration(mAnimationDuration);
        alpha.setFillAfter(true);
        view.startAnimation(alpha);
    }

    public void fadeIn(View view) {
        fadeIn(view, 0);
    }

    public void fadeIn(View view, int delay) {
        view.setVisibility(View.VISIBLE);
        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        alpha.setDuration(mAnimationDuration);
        alpha.setFillAfter(true);
        alpha.setStartOffset(delay);
        view.startAnimation(alpha);
    }
}
