/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 Segment.io, Inc.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.segment.analytics.sample;

import android.app.Application;
import android.util.Log;

import com.segment.analytics.Analytics;
import com.segment.analytics.Middleware;
import com.segment.analytics.ValueMap;
import com.segment.analytics.integrations.BasePayload;
import com.segment.analytics.integrations.TrackPayload;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class SampleApp extends Application {

    private static final String WRITE_KEY = "1syZUgdBm93wtkv1V8xvQqGTAUl";
    private static final String SOURCE_KEY = "ADR-1syZUdG3II6rZKHb0bnym0Ds1oo";

    @Override
    public void onCreate() {
        super.onCreate();

        ViewPump.init(
                ViewPump.builder()
                        .build());

        // Initialize a new instance of the Analytics client.
        Analytics.Builder builder =
                new Analytics.Builder(this, WRITE_KEY, SOURCE_KEY)
                        .experimentalNanosecondTimestamps()
                        .trackApplicationLifecycleEvents()
                        .flushQueueSize(1)
                        .recordScreenViews();

        // Set the initialized instance as a globally accessible instance.
        Analytics.setSingletonInstance(builder.build());

        // Now anytime you call Analytics.with, the custom instance will be returned.
        Analytics analytics = Analytics.with(this);

        analytics.identify("dom-android-sdk-confirm");
        analytics.track("android_view");
    }
}
