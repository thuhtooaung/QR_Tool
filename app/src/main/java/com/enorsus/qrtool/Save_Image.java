package com.enorsus.qrtool;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

public class Save_Image implements Save_Image_Interface{
    @Override
    public void Save_Image_File(Context context, String name, Bitmap bitmap, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            OutputStream fos;
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES);
            Uri image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            try {
                fos = contentResolver.openOutputStream(Objects.requireNonNull(image_uri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else {
            OutputStream fOut = null;
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/QRTool";
            File qr_tool = new File(path);
            if (!qr_tool.exists()){
                qr_tool.mkdir();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/QRTool",name +".jpg");
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, name);
            values.put(MediaStore.Images.Media.DESCRIPTION,name);
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
            values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
            values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
            values.put("_data", file.getAbsolutePath());
            ContentResolver cr = context.getContentResolver();

            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Toast.makeText(context,"OK",Toast.LENGTH_SHORT).show();

            try {
                activity.onBackPressed();
            }
            catch (Exception e){
                Log.i("ON BACK PRESSED",e.toString());
            }
        }

    }
}
