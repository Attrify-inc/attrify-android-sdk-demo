package com.attrify.sdk.demo.java;

import android.app.Application;
import android.util.Log;
import com.attrify.sdk.Attrify;
import com.attrify.sdk.AttrifyConfig;
import com.attrify.sdk.AttrifyEventFailure;
import com.attrify.sdk.AttrifyEventSuccess;
import com.attrify.sdk.AttrifyInitializationCallback;
import com.attrify.sdk.OnEventTrackingListener;

public class AttrifyApp extends Application {
    private static final String TAG = "AttrifyApp";

    @Override
    public void onCreate() {
        super.onCreate();
        AttrifyConfig config = new AttrifyConfig.Builder(BuildConfig.MEASUREMENT_ID)
            // Attrify only collects sensitive data if all consent settings (ccpa, coppa, gdpr) are true.
            // The default is **true** for all consent settings, you can set them to false if needed.
            // .setConsentCcpa(false)
            // .setConsentCoppa(false)
            // .setConsentGdpr(false)
            // .consentNone()
            .consentAll()
            // Set the event deduplication limit.
            .setEventDedupLimit(10)
            // Enable debug mode for detailed logging.
            // You can filter the log by tag prefix "Attrify_".
            .setDebugMode(true)
            .setEventTrackingListener(new OnEventTrackingListener() {
                @Override
                public void onEventTrackingFailed(AttrifyEventFailure data) {
                    Log.e(TAG, "Event: " + data.eventName + ", callbackId: " + data.callbackId + " tracking failed: " + data.message + ", will retry: " + data.willRetry + ".", data.cause);
                }

                @Override
                public void onEventTrackingSucceeded(AttrifyEventSuccess data) {
                    Log.i(TAG, "Event: " + data.eventName + ", callbackId: " + data.callbackId + " tracking succeeded.");
                }
            })
            .build();


        // You can also initialize Attrify without a callback.
        // Attrify.initialize(this, config)
        Attrify.initialize(this, config, new AttrifyInitializationCallback() {
            @Override
            public void onInitializationFailure(String message) {
                Log.e(TAG, "Attrify initialization failed: " + message);
            }

            @Override
            public void onInitializationSuccess() {
                Log.i(TAG, "Attrify initialized successfully.");
            }
        });
    }
}
