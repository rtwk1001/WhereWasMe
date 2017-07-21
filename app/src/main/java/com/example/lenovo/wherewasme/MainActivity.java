package com.example.lenovo.wherewasme;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    TextView txtOutputLat, txtOutputLon, address,time;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon,add;
    Button showaddress,showmap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtOutputLat = (TextView) findViewById(R.id.textView);
        txtOutputLon = (TextView) findViewById(R.id.text2);
        address = (TextView) findViewById(R.id.text3);
        time= (TextView) findViewById(R.id.time);
   showaddress=(Button)findViewById(R.id.showadd);
        showmap=(Button)findViewById(R.id.showmap);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        time.setText(date);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!statusOfGPS){
            displayPromptForEnablingGPS(MainActivity.this);
        }
        showaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address.setVisibility(View.VISIBLE);

            }

            });
        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + lat + "," + lon));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Only if initiating from a Broadcast Receiver
                String mapsPackageName = "com.google.android.apps.maps";
                if (Utility.isPackageExisted(context, mapsPackageName)) {
                    i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity");
                    i.setPackage(mapsPackageName);
                }
                context.startActivity(i);
                }

        });
        buildGoogleApiClient();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onConnected(Bundle bundle) {


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());

        }

        updateUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

public static void displayPromptForEnablingGPS(final Activity activity)
{

    final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
    final String message = "Do you want open GPS setting?";

    builder.setMessage(message)
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface d, int id) {
                            activity.startActivity(new Intent(action));
                            d.dismiss();
                        }
                    })
            .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface d, int id) {
                            d.cancel();
                        }
                    });
    builder.create().show();
}
    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            String StateName = address.get(0).getAdminArea();
            String CityName = address.get(0).getAddressLine(1);
            String CountryName = address.get(0).getCountryName();
            String now = address.get(0).getLocality();
            add=StateName+","+CityName+","+CountryName+","+now;
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateUI();
    }


    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lenovo.wherewasme/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    void updateUI() {
        txtOutputLat.setText("Latitude: "+lat);
        txtOutputLon.setText("Longitude: "+lon);
        address.setText("Your adress- "+add);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lenovo.wherewasme/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}