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

public class Message_Fragment extends Fragment {
    Bitmap bitmap;

    public Message_Fragment() {
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
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        final EditText ph = v.findViewById(R.id.msg_ph);
        final EditText bd = v.findViewById(R.id.msg_body);
        Button btn = v.findViewById(R.id.msg_qr_gen);
        final ImageView iv = v.findViewById(R.id.rs_img_5);
        final ImageButton img_btn = v.findViewById(R.id.msg_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stb = new StringBuilder();
                stb.append("sms:");
                stb.append(ph.getText().toString().trim());
                stb.append("?body=");
                stb.append(bd.getText().toString().trim());
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                try {
                    bitmap = barcodeEncoder.encodeBitmap(stb.toString(), BarcodeFormat.QR_CODE,400,400);
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