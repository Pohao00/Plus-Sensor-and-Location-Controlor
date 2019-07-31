package com.example.hueyan.ch14_1_2;



import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Timer;

public class MainActivity extends Service {
    private static final int STOP = 0x1111;
    private static final int PRINT = 0x2222;
    int timer2 = 0;

    private LocationManager manager;
    private Location currentLocation;
    private String best;
    String longt;
    String time;
    String latit;
    String customer_id;
    int num = 0, num2 = 0;
    InputStream is = null;
    String result = null;
    String line = null;
    int code;
    Timer timer = new Timer(true);
    int delay = 0; // delay for 0 sec.
    int period = 5000; // repeat every 10 sec.
    TimerThread t_thread = new TimerThread();
    Thread TimerThread2;
    Thread TimerThread3;
    boolean stop = false, flag;

    private class TimerThread extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } 
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!stop) {
                    Message msg = new Message();
                    msg.what = PRINT;
                    threadHandler.sendMessage(msg);
                    timer2++;
                    //=========================================================================================================================================
                } 
                else
                    break;
            }
        }
        public void setStop() {
            stop = true;
        }
    }

    private class TimerThread2 extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } 
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!stop) {
                    insert2();
                } 
                else
                    break;
            }
        }
    }
    private class TimerThread3 extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(6000);
                } 
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!stop) {
                    delete();
                } 
                else
                    break;
            }
        }
    }

    final Handler threadHandler = new Handler() {
        // ???Message??????????Log?
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRINT:
                    Log.i("Now is :", timer2 + " sec.");
                    break;
                case STOP:
                    t_thread.setStop();
                    Log.i("STOP:", "Timer is stopped.");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("Start:", "Start!");
        TimerThread2 = new TimerThread2();
        //TimerThread3 = new TimerThread3();
        customer_id = intent.getStringExtra("key1");

        t_thread.start();
        TimerThread2.start();
        //TimerThread3.start();
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("????")
                    .setMessage("GPS?????????.\n"
                            +"????????????GPS?")
                    .setPositiveButton("??", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("???", null).create().show();
        } insert2();
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        // Service?????STOP Message????Thread
        Message msg = new Message();
        msg.what = STOP;
        threadHandler.sendMessage(msg);
                //TimerThread2.destroy();
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
//======================================================================================================================================
    public void insert2()
    {
        Criteria criteria = new Criteria();
        best = manager.getBestProvider(criteria, true);
        int minTime = 1000;
        float minDistance = 0;
        if (best != null)
        {
            currentLocation = manager.getLastKnownLocation(best);
            manager.requestLocationUpdates(best, minTime, minDistance, listener);
        } 
        else
        {
            currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                        minTime, minDistance, listener);
        }

        getLocationInfo(currentLocation);
        //updatePosition(); // ????

        SimpleDateFormat nowdate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        nowdate.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String sdate = nowdate.format(new java.util.Date());
        time = sdate;
        insert();

    }
    public void getLocationInfo(Location location) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        StringBuffer str = new StringBuffer();
        str.append("?????(Provider): "+location.getProvider());
        str.append("\n??(Latitude): " + Double.toString(location.getLatitude()));
        str.append("\n??(Longitude): " + Double.toString(location.getLongitude()));
        str.append("\n??(Altitude): " + Double.toString(location.getAltitude()));
        latit =  Double.toString(location.getLatitude()).toString();
        longt = Double.toString(location.getLongitude()).toString();
    //return str.toString();
    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) { }
        @Override
        public void onProviderDisabled(String provider) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void insert()
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("latit", latit));
        nameValuePairs.add(new BasicNameValuePair("longt",longt));
        nameValuePairs.add(new BasicNameValuePair("time",time));
        nameValuePairs.add(new BasicNameValuePair("numb",Double.toString(num)));
        nameValuePairs.add(new BasicNameValuePair("customer_id",customer_id));
        num++;
        if(num==1)
            num=0;
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://140.120.14.118/AndroidConnectDB/insert.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());//?N???~?Xe???r??
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if(code == 1)
            {
                Toast.makeText(getBaseContext(), "Inserted Successfully",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Sorry, Try Again666",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
        }
    }
    public void delete()
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("numb",customer_id.toString()));
        num2++;
        if(num2 == 2)
            num2 = 0;

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://140.120.14.118/AndroidConnectDB/delete.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());//?N???~?Xe???r??
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));
            if(code == 1)
            {

                Toast.makeText(getBaseContext(), "Delete Successfully",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
        }
    }
}
