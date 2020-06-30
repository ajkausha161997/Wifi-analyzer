package com.example.ajay.wifianalyzer;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorActivity extends AppCompatActivity {

    Timer T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "View your Wifi signal strength logs", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addSignalItems();
    }

    private void addSignalItems() {

        final ArrayList<Signal> signals = new ArrayList<>();

        final ListView signalListView = (ListView) findViewById(R.id.list);

        final String filename = "WiFi";

        try {
            FileInputStream fin = openFileInput(filename);
            BufferedReader bb=new BufferedReader(new InputStreamReader(fin));
            String temp=bb.readLine();

            int i = R.drawable.history;

            while(temp!=null){

                String val[] = temp.split(" ");

                signals.add(new Signal(i,val[0]+" "+val[1],val[2]));
                signalListView.setAdapter(new SignalAdapter(MonitorActivity.this, signals));
                temp=bb.readLine();
            }
        }
        catch(Exception e){
        }

        final long msec = 3000;

        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int img=0, dbm, level;

                        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        dbm = wifiInfo.getRssi();
                        level = WifiManager.calculateSignalLevel(dbm, 5);
                        switch (level) {
                            case 0:
                                img = R.drawable.icon_wifi0;
                                break;
                            case 1:
                                img = R.drawable.icon_wifi1;
                                break;
                            case 2:
                                img = R.drawable.icon_wifi2;
                                break;
                            case 3:
                                img = R.drawable.icon_wifi3;
                                break;
                            case 4:
                                img = R.drawable.icon_wifi;
                                break;
                        }

                        Calendar cal = Calendar.getInstance();
                        Date currentLocalTime = cal.getTime();
                        DateFormat date = new SimpleDateFormat("HH:mm:ss");

                        String time = date.format(currentLocalTime);

                        signals.add(new Signal(img,dbm+" dBm",time));
                        signalListView.setAdapter(new SignalAdapter(MonitorActivity.this, signals));

                        String string = dbm+" dBm "+time;

                        try {

                            FileOutputStream outputStream = openFileOutput(filename, MODE_APPEND);
                            outputStream.write((string+"\n").getBytes());
                            outputStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }, 0, msec);

    }
}
