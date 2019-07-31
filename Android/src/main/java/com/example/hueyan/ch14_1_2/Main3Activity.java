package com.example.hueyan.ch14_1_2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main3Activity extends Activity implements LocationListener {
    private static final String MAP_URL = "file:///android_asset/googleMap.html";
    private WebView webView;
    EditText customer_id2;
    TextView LatText,LogText;
    private Button submit;
    private boolean webviewReady = false;
    private Location mostRecentLocation = null;
    String id,name;
    Button myButton4;
    private void getLocation() {//���o�˸m��GPS��m���
        LocationManager locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria,true);
        //In order to make sure the device is getting the location, request updates.
        locationManager.requestLocationUpdates(provider, 1, 0, this);
        mostRecentLocation = locationManager.getLastKnownLocation(provider);
    }

    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        findViews();
        setListeners();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        LatText = (TextView) findViewById(R.id.LatText);
        LogText = (TextView) findViewById(R.id.LogText);
        customer_id2 = (EditText) findViewById(R.id.customer_id2);

        getLocation();
        setupWebView();
        myButton4 = (Button) findViewById(R.id.button4);
        myButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (myButton4.getText().equals("關掉自動定位")) {
                    myButton4.setText("開啟自動定位");

                } 
                else {
                    myButton4.setText("關掉自動定位");
                }
            }
        });
        if (mostRecentLocation!=null && myButton4.getText().toString() == "關掉自動定位") {
            LatText.setText("" + mostRecentLocation.getLatitude());
            LogText.setText("" + mostRecentLocation.getLongitude());
            final String centerURL = "javascript:centerAt(" +
                    mostRecentLocation.getLatitude() + "," +
                    mostRecentLocation.getLongitude()+ ")";
            if (webviewReady) webView.loadUrl(centerURL);
        }
    }
    private Button button_get_record;

    private void findViews() {
        button_get_record = (Button)findViewById(R.id.submit);
    }

    private void setListeners() {
        button_get_record.setOnClickListener(getDBRecord);
    }

    private Button.OnClickListener getDBRecord = new Button.OnClickListener() {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams view_layout = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            try {
                String result = DBConnector.executeQuery("SELECT * FROM sample WHERE customer_id='" + customer_id2.getText().toString() + "' ");

                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    TableRow tr = new TableRow(Main3Activity.this);
                    tr.setLayoutParams(row_layout);
                    tr.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView user_acc = new TextView(Main3Activity.this);
                    user_acc.setText(jsonData.getString("latit"));
                    id=jsonData.getString("latit");

                    TextView user_pwd = new TextView(Main3Activity.this);
                    user_pwd.setText(jsonData.getString("longt"));
                    name=jsonData.getString("longt");
                }
            } 
            catch(Exception e) {
                // Log.e("log_tag", e.toString());
            }

            if (webviewReady) {
                LatText.setText("" + id);
                LogText.setText("" + name);

                final String markURL = "javascript:mark(" + id + "," + name + ")";
                webView.loadUrl(markURL);

                final String centerURL = "javascript:centerAt(" + id + "," + name + ")";
                webView.loadUrl(centerURL);
            }
        }
    };

    /** Sets up the WebView object and loads the URL of the page **/
    private void setupWebView() {
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        //Wait for the page to load then send the location information
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                //webView.loadUrl(centerURL);
                webviewReady = true;
            }

        });
        webView.loadUrl(MAP_URL);
    }

    @Override
    public void onLocationChanged(Location location) {//�w���m���ܮɷ|���檺��k
        // TODO Auto-generated method stub
        if (location !=null && myButton4.getText().equals("關掉自動定位") ) {
            LatText.setText("" + location.getLatitude());
            LogText.setText("" + location.getLongitude());
            final String centerURL = "javascript:centerAt(" +
                    location.getLatitude() + "," +
                    location.getLongitude()+ ")";
            if (webviewReady) webView.loadUrl(centerURL);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(Main3Activity.this, Main2Activity.class);//回到選單頁
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
