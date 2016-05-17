package com.example.patrik.signallogger;


/**
 * This is the main file for the application. Buttons are implemented and whats happening after
 * a click on these buttons.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.WifiManager;


import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Date;


import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int i = 0;
    public static List dBm_list = new ArrayList();
    public static List longitude_list = new ArrayList();
    public static List latitude_list = new ArrayList();
    public static List index_list = new ArrayList();
    public static Date date = new Date();
    public static boolean stop = true;
    //public static int log_counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creates objects of buttons
        Button loggbutton = (Button) findViewById(R.id.LoggButton);
        Button initbutton = (Button) findViewById(R.id.initButton);
        Button gpsbutton = (Button) findViewById(R.id.gpsButton);

        //  What is done when someone press the gpsbutton
        assert gpsbutton != null;
        gpsbutton.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                   gps();
                }
            }
        );
        //  What is done when someone press the initbutton
        assert initbutton != null;
        initbutton.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    signalstr();
                }
            }
        );
        //  What is done when someone press the loggbutton
        assert loggbutton != null;
        loggbutton.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    logging();
                }
            }
        );
    }

/**
 *Logging function for 50 values. Latitude, longitude and signal strenght are saved in different
 *  lists.
 * 50 measurements are done and between every measurement there is a delay, 0.1 seconds.
 */
    public void logging() {

        final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        final WifiManager.WifiLock wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL,
                "MyWifiLock");
        wifiLock.acquire();
        long time = date.getTime();

        for (int log_counter =0 ; log_counter<=50; log_counter++  ){
                // Checks so the phone holds the connectivity to the wifi
                if (!wifiLock.isHeld()) {
                    wifiLock.acquire();
                }

                // Level of current connection
                String name = wifiManager.getConnectionInfo().getSSID();
                int rssi = wifiManager.getConnectionInfo().getRssi();


                GPSTracker gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {
                    // Measure both latitude longitude and altitude
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    double altitude = gps.getAltitude();
                    // Puts longitude and latitude in lists
                    latitude_list.add(latitude);
                    longitude_list.add(longitude);
                    // Prints Latitude, longitude and altitude, network name, signal strength and
                    // which number of logg in a box on the screen
                    TextView ValueText = (TextView) findViewById(R.id.dBmText);
                    String strI = "Lat: " + latitude + "\nLong: " + longitude + "\nAltitud: " +
                            altitude + "\nNetwork name: " + name + "\nSignal strength: " + rssi +
                            " dBm" + "\nLogg counter: "+i ;
                    ValueText.setText(strI);
                }

                else {

                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            // Delay so it measures every single 0.1 second
            try {Thread.sleep(100);

            }
            catch (Exception e){
                System.out.println("Exception caught");}
            }
        System.out.println(dBm_list);
        System.out.println(latitude_list);
        System.out.println(longitude_list);
        System.out.println(index_list);


        //}


    }

    /**
     * Method getting only coordinates.
     */
    public void gps(){
        GPSTracker gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            double altitude = gps.getAltitude();


            TextView ValueText = (TextView) findViewById(R.id.dBmText);
            String strI = "Lat: " + latitude + "\nLong: " + longitude + "\nAltitud: " + altitude ;
            System.out.println(strI);
            ValueText.setText(strI);

        }

        else {

            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    /**
     * Method for signal strength.
     */
    public void signalstr(){
        i++;
        final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        final WifiManager.WifiLock wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL,
                "MyWifiLock");
        wifiLock.acquire();
        // get the name of the connected wifi name
        String name = wifiManager.getConnectionInfo().getSSID();
        // signal strength in dBm
        int rssi = wifiManager.getConnectionInfo().getRssi();
        // Prints network name, signal strength and which number of logg in a box on the screen
        TextView ValueText = (TextView) findViewById(R.id.dBmText);
        String strI = "Network name: " + name + "\nSignal strength: " + rssi + " dBm" +
                "\nLogg counter: " + i;
        System.out.println(strI);
        ValueText.setText(strI);

    }
}
