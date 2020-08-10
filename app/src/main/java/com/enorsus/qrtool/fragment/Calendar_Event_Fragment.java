package com.enorsus.qrtool.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.enorsus.qrtool.Img_Alert;
import com.enorsus.qrtool.R;
import com.enorsus.qrtool.model.DateTime;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.util.Calendar;
import java.util.Objects;
public class Calendar_Event_Fragment extends Fragment {
    Bitmap bitmap;

    DateTime dateTime,dateTimeEnd;

    public Calendar_Event_Fragment() {
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
        View v = inflater.inflate(R.layout.fragment_calendar_event, container, false);
        final EditText title = v.findViewById(R.id.cal_name);
        final TextView dt_picker = v.findViewById(R.id.cal_pick_date);
        final TextView time_picker = v.findViewById(R.id.cal_pick_time);
        final TextView dt_picker_end = v.findViewById(R.id.cal_pick_date_end);
        final TextView time_picker_end = v.findViewById(R.id.cal_pick_time_end);
        Button btn = v.findViewById(R.id.cal_qr_gen);
        final ImageView iv = v.findViewById(R.id.rs_img_7);
        final ImageButton img_btn = v.findViewById(R.id.cal_save);
        dateTime = new DateTime();
        dateTimeEnd = new DateTime();

        dt_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dt_picker,0);
            }
        });

        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(time_picker,0);
            }
        });

        dt_picker_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dt_picker_end,1);
            }
        });

        time_picker_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(time_picker_end,1);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stb = new StringBuilder();
                stb.append("BEGIN:VEVENT\nSUMMARY:");
                stb.append(title.getText().toString().trim());
                stb.append("\nDTSTART:");
                stb.append(dateTime.getYear());
                stb.append(dateTime.getMonth());
                stb.append(dateTime.getDay());
                stb.append("T");
                stb.append(dateTime.getHour());
                stb.append(dateTime.getMinute());
                stb.append(dateTime.getSecond());
                stb.append("\nDTEND:");
                stb.append(dateTimeEnd.getYear());
                stb.append(dateTimeEnd.getMonth());
                stb.append(dateTimeEnd.getDay());
                stb.append("T");
                stb.append(dateTimeEnd.getHour());
                stb.append(dateTimeEnd.getMinute());
                stb.append(dateTimeEnd.getSecond());
                stb.append("\nEND:VEVENT");
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

    private void showDatePickerDialog(final TextView textView, final int x){
        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                StringBuilder stb = new StringBuilder();
                stb.append(dayOfMonth);
                stb.append("/");
                stb.append(month);
                stb.append("/");
                stb.append(year);
                textView.setText(stb);
                if (x == 0){
                    dateTime.setYear(String.valueOf(year));
                    if (String.valueOf(month).length() == 1){
                        dateTime.setMonth(0+String.valueOf(month));
                    }
                    else {
                        dateTime.setMonth(String.valueOf(month));
                    }
                    if (String.valueOf(dayOfMonth).length() == 1){
                        dateTime.setDay(0+String.valueOf(dayOfMonth));
                    }
                    else {
                        dateTime.setDay(String.valueOf(dayOfMonth));
                    }
                }
                else {
                    dateTimeEnd.setYear(String.valueOf(year));
                    if (String.valueOf(month).length() == 1){
                        dateTimeEnd.setMonth(0+String.valueOf(month));
                    }
                    else {
                        dateTimeEnd.setMonth(String.valueOf(month));
                    }
                    if (String.valueOf(dayOfMonth).length() == 1){
                        dateTimeEnd.setDay(0+String.valueOf(dayOfMonth));
                    }
                    else {
                        dateTimeEnd.setDay(String.valueOf(dayOfMonth));
                    }
                }
            }
        },year,month,day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final TextView textView, final int x){
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                StringBuilder stb = new StringBuilder();
                stb.append(hourOfDay);
                stb.append(":");
                stb.append(minute);
                textView.setText(stb);
                if (x == 0){
                    if (String.valueOf(hourOfDay).length() == 1){
                        dateTime.setHour(0+String.valueOf(hourOfDay));
                    }
                    else {
                        dateTime.setHour(String.valueOf(hourOfDay));
                    }
                    if (String.valueOf(minute).length() == 1){
                        dateTime.setMinute(0+String.valueOf(minute));
                    }
                    else {
                        dateTime.setMinute(String.valueOf(minute));
                    }
                    dateTime.setSecond(String.valueOf(25));
                }
                else {
                    if (String.valueOf(hourOfDay).length() == 1){
                        dateTimeEnd.setHour(0+String.valueOf(hourOfDay));
                    }
                    else {
                        dateTimeEnd.setHour(String.valueOf(hourOfDay));
                    }
                    if (String.valueOf(minute).length() == 1){
                        dateTimeEnd.setMinute(0+String.valueOf(minute));
                    }
                    else {
                        dateTimeEnd.setMinute(String.valueOf(minute));
                    }
                    dateTimeEnd.setSecond(String.valueOf(25));
                }
            }
        },hour,minute,false);
        timePickerDialog.show();
    }
}