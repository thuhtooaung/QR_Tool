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

import java.util.Objects;

public class Wifi_Fragment extends Fragment {
    Bitmap bitmap;

    public Wifi_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wifi, container, false);
        final EditText ss_id = v.findViewById(R.id.wifi_ss_id);
        final EditText wifi_pwd = v.findViewById(R.id.wifi_pwd);
        final ImageView iv = v.findViewById(R.id.rs_img_1);
        final ImageButton iv_btn = v.findViewById(R.id.wifi_save);
        Button btn = v.findViewById(R.id.wifi_qr_gen);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Objects.requireNonNull(Objects.requireNonNull(ss_id.getText()).toString().trim());
                String password = wifi_pwd.getText().toString().trim();
                StringBuilder stb = new StringBuilder();
                stb.append("WIFI:S:");
                stb.append(name);
                stb.append(";T:WPA;");
                stb.append("P:");
                stb.append(password);
                stb.append(";H:false;;");
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                try {
                    bitmap = barcodeEncoder.encodeBitmap(stb.toString(), BarcodeFormat.QR_CODE,400,400);
                    iv_btn.setVisibility(View.VISIBLE);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                iv.setImageBitmap(bitmap);
            }
        });

        iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_Alert img_alert = new Img_Alert();
                img_alert.Show_Image_Name_Request(getContext(),bitmap,getActivity());
            }
        });
        return v;
    }
}