package com.enorsus.qrtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.enorsus.qrtool.fragment.Calendar_Event_Fragment;
import com.enorsus.qrtool.fragment.Contact_Fragment;
import com.enorsus.qrtool.fragment.Email_Fragment;
import com.enorsus.qrtool.fragment.Location_Fragment;
import com.enorsus.qrtool.fragment.Message_Fragment;
import com.enorsus.qrtool.fragment.Phone_Fragment;
import com.enorsus.qrtool.fragment.Play_Store_Fragment;
import com.enorsus.qrtool.fragment.Text_Fragment;
import com.enorsus.qrtool.fragment.Url_fragment;
import com.enorsus.qrtool.fragment.Wifi_Fragment;

import java.util.Objects;

public class Generate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(getDrawable(R.drawable.action_bar_bg));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int position = getIntent().getIntExtra("position",-1);

        if (position == 0){
            LoadFragment(new Wifi_Fragment());
        }
        else if (position == 1){
            LoadFragment(new Url_fragment());
        }
        else if (position == 2){
            LoadFragment(new Phone_Fragment());
        }
        else if (position == 3){
            LoadFragment(new Email_Fragment());
        }
        else if (position == 4){
            LoadFragment(new Message_Fragment());
        }
        else if (position == 5){
            LoadFragment(new Contact_Fragment());
        }
        else if (position == 6){
            LoadFragment(new Calendar_Event_Fragment());
        }
        else if (position == 7){
            LoadFragment(new Play_Store_Fragment());
        }
        else if (position == 8){
            LoadFragment(new Location_Fragment());
        }
        else{
            LoadFragment(new Text_Fragment());
        }
    }

    private void LoadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}