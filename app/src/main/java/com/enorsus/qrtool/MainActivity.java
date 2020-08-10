package com.enorsus.qrtool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    int REQUEST_IMG_PICK = 1;
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton image_scanner = findViewById(R.id.image_scanner);
        ImageButton scanner = findViewById(R.id.scanner);
        ImageButton generator = findViewById(R.id.generator);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(getDrawable(R.drawable.action_bar_bg));
        getSupportActionBar().setElevation(0);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},201);

        image_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image_pick_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(image_pick_intent,REQUEST_IMG_PICK);
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.initiateScan();
            }
        });

        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Generator.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMG_PICK){
            if (resultCode == RESULT_OK && data != null){
                Intent intent = new Intent(MainActivity.this,Image_scanner.class);
                intent.putExtra("data",data.getDataString());
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(),"Result Error or null data",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if (result != null){
                if (result.getContents() == null){
                    Toast.makeText(getApplicationContext(),"Scan Canceled",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent it = new Intent(MainActivity.this,Scanner_result.class);
                    it.putExtra("data",result.getContents());
                    startActivity(it);
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Something was wrong!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about_us){
            if (c == 0){
                c = 1;
                item.setIcon(R.drawable.ic_em_ch);
            }
            else {
                c = 0;
                item.setIcon(R.drawable.ic_exclaimationmark);
            }
            startActivity(new Intent(MainActivity.this,About.class));
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}