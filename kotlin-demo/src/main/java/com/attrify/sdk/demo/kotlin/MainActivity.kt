package com.attrify.sdk.demo.kotlin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.attrify.sdk.Attrify
import com.attrify.sdk.AttrifyEvent
import com.attrify.sdk.demo.kotlin.databinding.ActivityMainBinding
import com.attrify.sdk.predefined.AttrifyEventName
import com.attrify.sdk.predefined.AttrifyEventParameterName

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    private fun initView() {
        binding.btnStart.setOnClickListener {
            // Events that are tracked between the initialize and start methods are saved and not uploaded until start is called.
            // NOTE: If the SDK is not initialized, the start call will be ignored.
            Attrify.start()
        }

        binding.btnUpdateConsent.setOnClickListener {
            // You can update the consent settings for GDPR, CCPA, and COPPA at any time (you should call this method after initialize).
            Attrify.updateConsent(
                gdpr = true,
                ccpa = false,
                // The parameters can be null, in which case the current consent setting will be used.
                coppa = null
            )
        }

        binding.btnTrackSimpleEvent.setOnClickListener {
            // 1. Create an event. You can use predefined event names.
            val event = AttrifyEvent(AttrifyEventName.VIEW_HOMEPAGE)
            // 2. Track the event. It's all done.
            Attrify.trackEvent(event)
        }
        binding.btnTrackAdvancedEvent.setOnClickListener {
            // You can also track custom events.
            val event = AttrifyEvent("<YOUR_EVENT_NAME>")

            // Add event parameters.
            event.addParam(AttrifyEventParameterName.CURRENCY, "USD")
            event.addParam(AttrifyEventParameterName.VALUE, 10.99)

            // Add your own event parameters.
            event.addParam("<YOUR_EVENT_PARAMETER_NAME>", "<YOUR_EVENT_PARAMETER_VALUE>")

            // The value supports several types, including:
            // - String and primitive types or the corresponding boxed types (e.g., Int, Double, Boolean)
            // - Collection (e.g., List, Set)
            // - Map
            // - Array
            // - JSONArray and JSONObject
            // - null
            val items = listOf(mapOf(AttrifyEventParameterName.ITEM_ID to "itemId"))
            event.addParam(AttrifyEventParameterName.ITEMS, items)

            // You can set the transaction ID.
            event.transactionId = "transactionId"
            // You can set the item ID.
            event.itemId = "itemId"

            // Set the callback ID, so that you can identify the event in the callback.
            event.callbackId = "callbackId"

            Attrify.trackEvent(event)
        }
        binding.btnTrackDedupEvent.setOnClickListener {
            repeat(3) {
                val event = AttrifyEvent("<YOUR_EVENT_NAME>")
                // You can set the deduplication ID, so that only one event with the same ID will be tracked.
                event.deduplicationId = "dedup_id"
                Attrify.trackEvent(event)
            }
        }
    }
}