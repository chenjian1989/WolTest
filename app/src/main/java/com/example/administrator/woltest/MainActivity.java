package com.example.administrator.woltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText ip = null;
    private EditText macAddr = null;
    private EditText port = null;
    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.wake_button);
        ip = (EditText) findViewById(R.id.ip);
        macAddr = (EditText) findViewById(R.id.mac_addr);
        port = (EditText) findViewById(R.id.port);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lip = ip.getText().toString();
                String lmacAddr = macAddr.getText().toString();
                int lport;
                try {
                    lport = Integer.parseInt(port.getText().toString());
                } catch (Exception e) {
                    lport = -1;
                }
                if (TextUtils.isEmpty(lip)) {
                    Toast.makeText(MainActivity.this, "please input ip!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validationIp(lip)) {
                    Toast.makeText(MainActivity.this, "Invalid IP address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e("hello", lip);
                if (TextUtils.isEmpty(lmacAddr)) {
                    Toast.makeText(MainActivity.this, "please input mac!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validationMac(lmacAddr)) {
                    return;
                }
                Log.e("hello", lmacAddr);
                if (lport <= 0) {
                    Toast.makeText(MainActivity.this, "please input port!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e("hello", lport + "");
                new WakeThread(lip, lmacAddr, lport).start();
                Toast.makeText(MainActivity.this, "send wake package", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validationIp(String ip) {
        /*正则表达式*/
        String gs_ip = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|[1-9])\\."
                + "(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{2}|\\d)\\."
                + "(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{2}|\\d)\\."
                + "(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{2}|\\d)";//限定输入格式

        Pattern p = Pattern.compile(gs_ip);
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    private boolean validationMac(String macStr) {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            Toast.makeText(MainActivity.this, "Invalid MAC address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Invalid hex digit in MAC address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
