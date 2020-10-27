/*
 * Copyright 2017 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This product includes software developed at Comcast (http://www.comcast.com/).
 */
package com.xfinity.loadingdots;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class LoadingDots extends AppCompatImageView {
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

    //We shouldn't need this since line 98 is wrapped in an if conditional, but lint refuses to recognize that, so also
    // annotate
    @TargetApi(Build.VERSION_CODES.M)
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
