package com.enorsus.qrtool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

public interface Save_Image_Interface {
    void Save_Image_File(Context context, String name, Bitmap bitmap, Activity activity);
}
