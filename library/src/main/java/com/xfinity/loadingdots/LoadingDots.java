package com.xfinity.loadingdots;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("unused")
public class LoadingDots extends android.support.v7.widget.AppCompatImageView {
    private Animatable animation;

    //for API 21 and 22
    private int duration;
    private AvdWrapper avdWrapper;
    private Handler handler = new Handler();
    AvdWrapper.Callback callback = new AvdWrapper.Callback() {
        @Override
        public void onAnimationEnd() {
            startAnimation();
        }

        @Override
        public void onAnimationStopped() {
            // Okay
        }
    };

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
        if (Build.VERSION.SDK_INT >= 21) {
            AnimatedVectorDrawable drawable = new AnimatedVectorDrawable();
            Drawable animAsDrawable = ContextCompat.getDrawable(getContext(), R.drawable.animated_vector_loading_dots);
            setImageDrawable(animAsDrawable);
        } else {
            setImageResource(R.drawable.animated_vector_loading_dots);
        }

        animation = (Animatable) getDrawable();
        if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            duration = getContext().getResources().getInteger(R.integer.loading_dot_animation_duration) * 5;
            avdWrapper = new AvdWrapper(animation,  handler, callback);
        }

        if (getVisibility() == View.VISIBLE) {
            startAnimation();
        }

        if (animation instanceof AnimatedVectorDrawableCompat) {
            final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) animation;
            animatedVectorDrawableCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    if (drawable instanceof Animatable) {
                        if (animation.isRunning()) {
                            stopAnimation();
                        } else {
                            //This sucks, but it's the only way to make the compat lib repeat the animation
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    startAnimation();
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
                                    startAnimation();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void startAnimation() {
        if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            avdWrapper.start(duration);
        } else {
            animation.start();
        }
    }

    private void stopAnimation() {
        if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            avdWrapper.stop();
        } else {
            animation.stop();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startAnimation();
        } else {
            stopAnimation();
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
