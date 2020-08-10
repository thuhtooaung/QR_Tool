package com.enorsus.qrtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import java.util.regex.Pattern;

public class Scanner_result extends AppCompatActivity {

    String TYPE,SS_ID,PASSWORD,HIDDEN,URL,PHONE,SMS,SMS_BODY,E_Mail,E_Sub,E_Body,GEO,GEO_Name,C_Name,C_Tel,C_ORG,C_Adr,C_Mail,C_Note,CAL_SUMMARY,DT_START,DT_END,TEXT,Play_Store;
    TextView result_text;
    boolean b_phone,b_wifi,b_url,b_urls,b_mail,b_sms,b_geo,b_play_store,b_me,b_calendar;
    Button result_work;

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_result);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(getDrawable(R.drawable.action_bar_bg));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView tv = findViewById(R.id.tv);
        img = findViewById(R.id.result_img);
        result_text = findViewById(R.id.result_text);
        result_work = findViewById(R.id.result_work);
        TextView cp_rw_dt = findViewById(R.id.cp_rw_dt);

        final String data = getIntent().getStringExtra("data");

        try {
            Matcher(data);
            KeySearcher(data);
            tv.setText(data);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Detect Error",Toast.LENGTH_SHORT).show();
            finish();
        }

        result_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listen(tv.getText().toString().trim());
            }
        });

        cp_rw_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("data",data);
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(clipData);
                }
                Toast.makeText(getApplicationContext(),"Raw Data Copied to Clipboard",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Matcher(String data){
        b_phone = Pattern.matches("tel:.*",data);
        b_wifi = Pattern.matches("WIFI:.*",data);
        b_url = Pattern.matches("http://.*",data);
        b_urls = Pattern.matches("https://.*",data);
        b_mail = Pattern.matches("mailto:.*",data);
        b_sms = Pattern.matches("sms:.*",data);
        b_geo = Pattern.matches("geo:.*",data);
        b_play_store = Pattern.matches("market://.*",data);
        b_me = Pattern.matches("MECARD:.*",data);
        b_calendar = Pattern.matches("BEGIN:.*",data.replace("\n",""));
    }

    private void Listen(String data){
        Uri uri = Uri.parse(data);
        if (b_phone){
            startActivity(new Intent(Intent.ACTION_DIAL,uri));
        }
        else if (b_wifi){
            try {
                if (SS_ID != null && PASSWORD != null) Wifi_Connect(SS_ID,PASSWORD);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage()+"or Invalid Wifi QR Format!",Toast.LENGTH_SHORT).show();
            }
        }
        else if (b_url || b_urls){
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
        else if (b_mail){
            startActivity(new Intent(Intent.ACTION_SENDTO,uri));
        }
        else if (b_sms){
            startActivity(new Intent(Intent.ACTION_SEND,uri));
        }
        else if (b_geo){
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
        else if (b_play_store){
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
        else if (b_me){
            AddContact();
        }
        else if (b_calendar){
            AddCalendarEvent();
        }
        else {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("data",data);
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
            }
            Toast.makeText(getApplicationContext(),"Copied to Clipboard",Toast.LENGTH_SHORT).show();
        }
    }

    private void KeySearcher(String k_data){
        if (b_phone){
            try {
                Phone_Key_Search(k_data);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Phone:");
            stb.append(PHONE);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_phone_black_24dp));
            result_work.setText(R.string.dial);
        }
        else if (b_wifi){
            try {
                Wifi_Key_Search(k_data.substring(5));
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Wifi\n");
            stb.append("SSID:");
            stb.append(SS_ID);
            stb.append("\n");
            stb.append("Password:");
            stb.append(PASSWORD);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_wifi_black_24dp));
            result_work.setText(R.string.connect);
        }
        else if (b_url || b_urls){
            try {
                Url_Key_Search(k_data);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("URL:");
            stb.append(URL);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_link_black_24dp));
            result_work.setText(R.string.go);
        }
        else if (b_mail){
            try {
                Email_Key_Search(k_data);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Send Email\n");
            stb.append("Mail:");
            stb.append(E_Mail);
            stb.append("\n");
            stb.append("Subtitle:");
            stb.append(E_Sub);
            stb.append("\n");
            stb.append("Body:");
            stb.append(E_Body);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_mail_black_24dp));
            result_work.setText(R.string.send_email);
        }
        else if (b_sms){
            try {
                Sms_Key_Search(k_data);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Sms\n");
            stb.append("Phone:");
            stb.append(SMS);
            stb.append("\n");
            stb.append("Body:");
            stb.append(SMS_BODY);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_sms_black_24dp));
            result_work.setText(R.string.send_sms);
        }
        else if (b_geo){
            try {
                Geo_Key_Search(k_data);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Location\n");
            stb.append("Lat,Long:");
            stb.append(GEO);
            stb.append("\n");
            stb.append("Place:");
            stb.append(GEO_Name);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_location_on_black_24dp));
            result_work.setText(R.string.go_to_map);
        }
        else if (b_play_store){
            try {
                Play_Store_Key_Search(k_data);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Get it on Play Store");
            stb.append("\n");
            stb.append("Play Store:");
            stb.append(Play_Store);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_shop_black_24dp));
            result_work.setText(R.string.go_to_market);
        }
        else if (b_me){
            try {
                Contact_Key_Search(k_data.substring(7));
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Contact\n");
            stb.append("Name:");
            stb.append(C_Name);
            stb.append("\n");
            stb.append("Phone:");
            stb.append(C_Tel);
            stb.append("\n");
            stb.append("Address:");
            stb.append(C_Adr);
            stb.append("\n");
            stb.append("GMail:");
            stb.append(C_Mail);
            stb.append("\n");
            stb.append("Organization:");
            stb.append(C_ORG);
            stb.append("\n");
            stb.append("Note:");
            stb.append(C_Note);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_contacts_black_24dp));
            result_work.setText(R.string.add_contact);
        }
        else if (b_calendar){
            try {
                Calendar_Key_Search(k_data);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Invalid QR Code Format or Something was wrong !",Toast.LENGTH_SHORT).show();
            }
            StringBuilder stb = new StringBuilder();
            stb.append("Calendar Event\n");
            stb.append("Title:");
            stb.append(CAL_SUMMARY);
            stb.append("\n");
            stb.append("Start Date:");
            stb.append(DT_START);
            stb.append("\n");
            stb.append("End Date:");
            stb.append(DT_END);
            result_text.setText(stb);
            img.setImageDrawable(getDrawable(R.drawable.ic_event_black_24dp));
            result_work.setText(R.string.add_calendar_event);
        }
        else {
            img.setImageDrawable(getDrawable(R.drawable.ic_content_copy_black_24dp));
            Text_Key_Search(k_data);
            StringBuilder stb = new StringBuilder();
            stb.append("Text\n");
            stb.append("Copy all text");
            result_text.setText(stb);
            result_work.setText(R.string.copy);
        }
    }

    private void Wifi_Key_Search(String x_data){
        String[] kv_arr = x_data.split(";");
        for (String kv_pair : kv_arr) {
            String[] kv = kv_pair.split(":");
            String key = kv[0];
            String value = kv[1];
            switch (key){
                case "S" :  SS_ID = value;
                    break;
                case "T" : TYPE = value;
                    break;
                case "P" : PASSWORD = value;
                    break;
                case "H" : HIDDEN = value;
            }
        }
    }

    private void Url_Key_Search(String x_data){
        URL = x_data;
    }

    private void Phone_Key_Search(String x_data){
        PHONE = x_data.substring(4);
    }

    private void Sms_Key_Search(String x_data){
        SMS = x_data.substring(4,x_data.indexOf("?"));
        SMS_BODY = x_data.substring(x_data.indexOf("body=")+5);
    }

    private void Email_Key_Search(String x_data){
        E_Mail = x_data.substring(x_data.indexOf("mailto:")+7,x_data.indexOf("?"));
        E_Sub = x_data.substring(x_data.indexOf("?subject=")+9,x_data.indexOf("&"));
        E_Body = x_data.substring(x_data.indexOf("&body=")+6);
    }

    private void Geo_Key_Search(String x_data){
        GEO = x_data.substring(x_data.indexOf(":")+1,x_data.indexOf("?"));
        GEO_Name = x_data.substring(x_data.indexOf("q")+2);
    }

    private void Contact_Key_Search(String x_data){
        String[] kv_arr = x_data.split(";");
        for (String kv_pair : kv_arr) {
            String[] kv = kv_pair.split(":");
            String key = kv[0];
            String value = kv[1];
            switch (key){
                case "N" :  C_Name = value;
                    break;
                case "ORG" : C_ORG = value;
                    break;
                case "ADR" : C_Adr = value;
                    break;
                case "TEL" : C_Tel = value;
                    break;
                case "EMAIL" : C_Mail = value;
                    break;
                case "NOTE" : C_Note = value;
            }
        }
    }

    private void Calendar_Key_Search(String x_data){
        String[] kv_arr = x_data.split("\n");
        for (String kv_pair : kv_arr) {
            String[] kv = kv_pair.split(":");
            String key = kv[0];
            String value = kv[1];

            switch (key){
                case "SUMMARY" : CAL_SUMMARY = value;
                    break;
                case "DTSTART" : DT_START = value;
                    break;
                case "DTEND" : DT_END = value;
            }
        }
    }

    private void Text_Key_Search(String x_data){
        TEXT = x_data;
    }

    private void Play_Store_Key_Search(String x_data){
        Play_Store = x_data;
    }


    private void Wifi_Connect(String S, String P){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            Toast.makeText(getApplicationContext(),"Above API 29",Toast.LENGTH_SHORT).show();
            WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
            builder.setSsid(S);
            builder.setWpa2Passphrase(P);
            WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(wifiNetworkSpecifier)
                    .build();
            ConnectivityManager cm = (ConnectivityManager) Scanner_result.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm!=null){
                cm.requestNetwork(networkRequest,new ConnectivityManager.NetworkCallback());
            }
        }else {
            Toast.makeText(getApplicationContext(),"Under API 29",Toast.LENGTH_SHORT).show();
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = String.format("\"%s\"",S);
            conf.preSharedKey = String.format("\"%s\"",P);

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            Objects.requireNonNull(wifiManager).setWifiEnabled(true);
            int netId = wifiManager.addNetwork(conf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId,true);
            wifiManager.reconnect();
        }
        //checkNetState();
    }

    private void checkNetState(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Connect Fail",Toast.LENGTH_SHORT).show();
        }
    }


    private void AddContact(){
        Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,ContactsContract.Contacts.CONTENT_URI);
        intent.setData(Uri.parse("tel:"+C_Tel));
        intent.putExtra(ContactsContract.Intents.Insert.PHONE,C_Tel);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, C_ORG);
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE,C_ORG);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL,C_Adr);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL,C_Mail);
        intent.putExtra(ContactsContract.Intents.Insert.NOTES,C_Note);
        startActivity(intent);
    }


    private void AddCalendarEvent(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE,CAL_SUMMARY);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,DT_START);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,DT_END);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}