package com.enorsus.qrtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Objects;

public class Generator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(getDrawable(R.drawable.action_bar_bg));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);


        ArrayList<String> arr = new ArrayList<>();
        arr.add("Wifi");
        arr.add("URL");
        arr.add("Phone");
        arr.add("Email");
        arr.add("Message");
        arr.add("Contact");
        arr.add("Calendar Event");
        arr.add("Play Store");
        arr.add("Location");
        arr.add("Text");

        ArrayList<String> images = new ArrayList<>();
        images.add(String.valueOf(R.drawable.ic_wifi_black_24dp));
        images.add(String.valueOf(R.drawable.ic_link_black_24dp));
        images.add(String.valueOf(R.drawable.ic_phone_black_24dp));
        images.add(String.valueOf(R.drawable.ic_mail_black_24dp));
        images.add(String.valueOf(R.drawable.ic_sms_black_24dp));
        images.add(String.valueOf(R.drawable.ic_contacts_black_24dp));
        images.add(String.valueOf(R.drawable.ic_event_black_24dp));
        images.add(String.valueOf(R.drawable.ic_shop_black_24dp));
        images.add(String.valueOf(R.drawable.ic_location_on_black_24dp));
        images.add(String.valueOf(R.drawable.ic_content_copy_black_24dp));
        RecyclerAdapter adapter = new RecyclerAdapter(Generator.this,arr,images);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}