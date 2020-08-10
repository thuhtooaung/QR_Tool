package com.enorsus.qrtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.Objects;

public class Image_scanner extends AppCompatActivity {

    Bitmap bitmap;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_scanner);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(getDrawable(R.drawable.action_bar_bg));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv = findViewById(R.id.image_view);
        Button scan = findViewById(R.id.image_scan);

        String data = getIntent().getStringExtra("data");

        showBitmap(data);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Image_scanner.this,Scanner_result.class);
                intent.putExtra("data",scan_Image_Bitmap(bitmap));
                startActivity(intent);
                finish();
            }
        });
    }


    private void showBitmap(String data){
        if (data != null){
            Uri uri = Uri.parse(data);
            // *** Get Bitmap form image uri *** //
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                try {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), uri));
                    iv.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    iv.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // *** End of Get Bitmap form image uri *** //
        }
    }

    private String scan_Image_Bitmap(Bitmap bmp){
        String contents = null;

        int[] intArray = new int[bmp.getWidth()*bmp.getHeight()];
        //copy pixel data from the bitmap bmp to int array
        bmp.getPixels(intArray, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        LuminanceSource luminanceSource = new RGBLuminanceSource(bmp.getWidth(),bmp.getHeight(),intArray);

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(binaryBitmap);
            contents = result.getText();
        }
        catch (Exception e){
            Log.i("Reader",e.toString());
        }
        return contents;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}