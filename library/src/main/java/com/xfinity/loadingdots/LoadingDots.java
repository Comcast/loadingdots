package com.xfinity.loadingdots;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("unused")
public class LoadingDots extends android.support.v7.widget.AppCompatImageView {
    private Animatable animation;

    public LoadingDots(Context context) {
        super(context);
    }

    public LoadingDots(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingDots(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageResource(R.drawable.animated_vector_loading_dots);

        animation = (Animatable) getDrawable();
        if (getVisibility() == View.VISIBLE) {
            animation.start();
        }

        if (animation instanceof AnimatedVectorDrawableCompat) {
            final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) animation;
            animatedVectorDrawableCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(final Drawable drawable) {
                    if (drawable instanceof Animatable) {
                        if (animation.isRunning()) {
                            animation.stop();
                        } else {
                            //This sucks, but it's the only way to make the compat lib repeat the animation
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    animation.start();
                                }
                            });
                        }
                    }
                }
            });
        } else if (animation instanceof AnimatedVectorDrawable) {
            final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) animation;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                animatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        if (drawable instanceof Animatable2) {
                            post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    animatedVectorDrawable.start();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            animation.start();
        } else {
            animation.stop();
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        if (animation != null) {
            throw new IllegalStateException("Changing the LoadingDots image resource is not supported");
        } else {
            super.setImageResource(resId);
        }
    }

    @Override
    public void setImageDrawable(Drawable background) {
        if (animation != null) {
            throw new IllegalStateException("Changing the LoadingDots image drawable is not supported");
        } else {
            super.setImageDrawable(background);
        }
    }
}
