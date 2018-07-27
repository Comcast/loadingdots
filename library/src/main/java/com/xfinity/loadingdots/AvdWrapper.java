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
