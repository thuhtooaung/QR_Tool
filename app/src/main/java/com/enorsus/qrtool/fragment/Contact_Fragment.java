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

public class Contact_Fragment extends Fragment {
    Bitmap bitmap;

    public Contact_Fragment() {
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
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        final EditText name = v.findViewById(R.id.ct_name);
        final EditText org = v.findViewById(R.id.ct_org);
        final EditText ph = v.findViewById(R.id.ct_ph);
        final EditText add = v.findViewById(R.id.ct_addr);
        final EditText email = v.findViewById(R.id.ct_mail);
        final EditText note = v.findViewById(R.id.ct_note);
        final ImageView iv = v.findViewById(R.id.rs_img_6);
        Button btn = v.findViewById(R.id.ct_qr_gen);
        final ImageButton img_btn = v.findViewById(R.id.ct_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stb = new StringBuilder();
                stb.append("MECARD:N:");
                stb.append(name.getText().toString().trim());
                stb.append(";ORG:");
                stb.append(org.getText().toString().trim());
                stb.append(";ADR:");
                stb.append(add.getText().toString().trim());
                stb.append(";TEL:");
                stb.append(ph.getText().toString().trim());
                stb.append(";EMAIL:");
                stb.append(email.getText().toString().trim());
                stb.append(";NOTE:");
                stb.append(note.getText().toString().trim());
                stb.append(";;");
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