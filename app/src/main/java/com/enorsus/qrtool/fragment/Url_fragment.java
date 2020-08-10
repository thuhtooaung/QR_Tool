package com.enorsus.qrtool.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.enorsus.qrtool.Img_Alert;
import com.enorsus.qrtool.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Url_fragment extends Fragment {
    Bitmap bitmap;

    public Url_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.url_fragment, container, false);
        final EditText url = v.findViewById(R.id.url_qr);
        Button btn = v.findViewById(R.id.url_qr_gen);
        final ImageView iv = v.findViewById(R.id.rs_img_2);
        final ImageButton img_btn = v.findViewById(R.id.url_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                String str = url.getText().toString().trim();
                try {
                    bitmap = barcodeEncoder.encodeBitmap(str, BarcodeFormat.QR_CODE,400,400);
                    iv.setImageBitmap(bitmap);
                    img_btn.setVisibility(View.VISIBLE);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_Alert img_alert = new Img_Alert();
                img_alert.Show_Image_Name_Request(getContext(),bitmap,getActivity());
            }
        });
        return v;
    }
}