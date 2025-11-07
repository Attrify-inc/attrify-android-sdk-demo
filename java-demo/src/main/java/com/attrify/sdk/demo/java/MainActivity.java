package com.attrify.sdk.demo.java;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.attrify.sdk.Attrify;
import com.attrify.sdk.AttrifyEvent;
import com.attrify.sdk.demo.java.databinding.ActivityMainBinding;
import com.attrify.sdk.predefined.AttrifyEventName;
import com.attrify.sdk.predefined.AttrifyEventParameterName;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
    }

    private void initView() {
        binding.btnStart.setOnClickListener(v -> {
            // Events that are tracked between the initialize and start methods are saved and not uploaded until start is called.
            // NOTE: If the SDK is not initialized, the start call will be ignored.
            Attrify.start();
        });

        binding.btnUpdateConsent.setOnClickListener(v -> {
            // You can update the consent settings for GDPR, CCPA, and COPPA at any time (you should call this method after initialize).
            Attrify.updateConsent(
                true,  // gdpr
                false, // ccpa
                null   // coppa - The parameters can be null, in which case the current consent setting will be used.
            );
        });

        binding.btnTrackSimpleEvent.setOnClickListener(v -> {
            // 1. Create an event. You can use predefined event names.
            AttrifyEvent event = new AttrifyEvent(AttrifyEventName.VIEW_HOMEPAGE);
            // 2. Track the event. It's all done.
            Attrify.trackEvent(event);
        });
        
        binding.btnTrackAdvancedEvent.setOnClickListener(v -> {
            // You can also track custom events.
            AttrifyEvent event = new AttrifyEvent("<YOUR_EVENT_NAME>");

            // Add event parameters.
            event.addParam(AttrifyEventParameterName.CURRENCY, "USD");
            event.addParam(AttrifyEventParameterName.VALUE, 10.99);

            // Add your own event parameters.
            event.addParam("<YOUR_EVENT_PARAMETER_NAME>", "<YOUR_EVENT_PARAMETER_VALUE>");

            // The value supports several types, including:
            // - String and primitive types or the corresponding boxed types (e.g., Int, Double, Boolean)
            // - Collection (e.g., List, Set)
            // - Map
            // - Array
            // - JSONArray and JSONObject
            // - null
            List<Map<String, Object>> items = List.of(Map.of(AttrifyEventParameterName.ITEM_ID, "itemId"));
            event.addParam(AttrifyEventParameterName.ITEMS, items);

            // You can set the transaction ID.
            event.setTransactionId("transactionId");
            // You can set the item ID.
            event.setItemId("itemId");

            // Set the callback ID, so that you can identify the event in the callback.
            event.setCallbackId("callbackId");

            Attrify.trackEvent(event);
        });
        
        binding.btnTrackDedupEvent.setOnClickListener(v -> {
            for (int i = 0; i < 3; i++) {
                AttrifyEvent event = new AttrifyEvent("<YOUR_EVENT_NAME>");
                // You can set the deduplication ID, so that only one event with the same ID will be tracked.
                event.setDeduplicationId("dedup_id");
                Attrify.trackEvent(event);
            }
        });
    }
}
