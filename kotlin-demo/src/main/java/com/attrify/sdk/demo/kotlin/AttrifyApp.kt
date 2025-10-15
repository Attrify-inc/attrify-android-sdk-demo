package com.attrify.sdk.demo.kotlin

import android.app.Application
import android.util.Log
import com.attrify.sdk.Attrify
import com.attrify.sdk.AttrifyConfig
import com.attrify.sdk.AttrifyEventFailure
import com.attrify.sdk.AttrifyEventSuccess
import com.attrify.sdk.AttrifyInitializationCallback
import com.attrify.sdk.OnEventTrackingListener

private const val TAG = "AttrifyApp"

class AttrifyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = AttrifyConfig.Builder("<YOUR_MEASUREMENT_ID>")
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
            .setEventTrackingListener(object : OnEventTrackingListener {
                override fun onEventTrackingFailed(data: AttrifyEventFailure) {
                    Log.e(TAG, "Event: ${data.eventName}, callbackId: ${data.callbackId} tracking failed: ${data.message}, will retry: ${data.willRetry}.", data.cause)
                }

                override fun onEventTrackingSucceeded(data: AttrifyEventSuccess) {
                    Log.i(TAG, "Event: ${data.eventName}, callbackId: ${data.callbackId} tracking succeeded.")
                }
            })
            .build()

        // You can also initialize Attrify without a callback.
        // Attrify.initialize(this, config)
        Attrify.initialize(this, config, object : AttrifyInitializationCallback {
            override fun onInitializationFailure(message: String) {
                Log.e(TAG, "Attrify initialization failed: $message")
            }

            override fun onInitializationSuccess() {
                Log.i(TAG, "Attrify initialized successfully.")
            }
        })
    }
}