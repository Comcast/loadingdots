package com.xfinity.loadingdots;

import android.graphics.drawable.Animatable;
import android.os.Handler;

/**
 * Wrapper class to provide animation event listening for AnimatedVectorDrawables on API 21 and 22.
 * Taken from : https://stackoverflow.com/questions/28812604/is-there-a-way-to-listen-for-animation-end-in-animatedvectordrawables
 */
class AvdWrapper {
    private Handler handler;
    private Animatable animatable;
    private Callback callback;
    private Runnable animationRunnable = new Runnable() {

        @Override
        public void run() {
            if (callback != null) {
                callback.onAnimationEnd();
            }
        }
    };

    interface Callback {
        void onAnimationEnd();
        void onAnimationStopped();
    }

    AvdWrapper(Animatable animatable,
               Handler handler, Callback callback) {
        this.animatable = animatable;
        this.handler = handler;
        this.callback = callback;
    }

    // Duration of the animation
    void start(long duration) {
        animatable.start();
        handler.postDelayed(animationRunnable, duration);
    }

    void stop() {
        animatable.stop();
        handler.removeCallbacks(animationRunnable);

        if (callback != null) {
            callback.onAnimationStopped();
        }
    }
}
