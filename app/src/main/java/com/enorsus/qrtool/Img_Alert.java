package com.enorsus.qrtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Img_Alert {
    public void Show_Image_Name_Request(final Context context, final Bitmap bitmap, final Activity activity){
        AlertDialog.Builder adb = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        adb.setView(R.layout.get_image_name);
        adb.setCancelable(false);
        final AlertDialog ad = adb.create();
        ad.show();
        final EditText img_name = ad.findViewById(R.id.img_name);
        Button get_img_name = ad.findViewById(R.id.get_img_name);
        Button get_img_cancel = ad.findViewById(R.id.get_img_cancel);
        get_img_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save_Image si = new Save_Image();
                si.Save_Image_File(context,img_name.getText().toString().trim(),bitmap,activity);
                ad.dismiss();
            }
        });
        get_img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }
}
