package com.ss.covid_19_tracer;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class GPSLocation
{

    private LocationManager mLocationManagerGPS;
    private LocationListener mLocationListenerGPS;

    private LocationManager mLocationManagerNetwork;
    private LocationListener mLocationListenerNetwork;

    private Button btnGPS;
    private Button btnNetwork;
    private Button viewFromMap;

    private SharedPreferences sharedPreferences;
    private static final String USER_LAT = "USER_LAT";
    private static final String USER_LOG = "USER_LOG";
    private static final String LOCATION_DATA = "LOCATION_DATA";

    private String latitude;
    private String longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Huduma White Box");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorAccent));

        sharedPreferences = getSharedPreferences(LOCATION_DATA,MODE_PRIVATE);

        btnGPS = findViewById(R.id.btnGPSLoc);
        btnNetwork = findViewById(R.id.btnNetworkLoc);
        viewFromMap = findViewById(R.id.mapBtn);

        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getPositionGPS();
            }
        });

        btnNetwork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getPositionNetwork();
            }
        });

        viewFromMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                longitude = txtViewLongNetwork.getText().toString();
                latitude = txtViewLatNetwork.getText().toString();

                if(longitude.equals(null) && latitude.equals(null))
                {
                    return;
                }
                else
                    uploadLocationData(latitude,longitude);


                toNextActivity(MapsActivity.class);

            }
        });
    }

    public void uploadLocationData(String user_lat,String user_log)
    {
        if(sharedPreferences != null)
        {
            SharedPreferences.Editor accountEditor = sharedPreferences.edit();

            if(accountEditor != null)
            {
                accountEditor.putString(USER_LAT, user_lat);
                accountEditor.putString(USER_LOG, user_log);
                accountEditor.apply();

                //call google maps to display location
            }
        }
    }

    private void toNextActivity(Class myActivity)
    {
        Intent intent = new Intent(getApplicationContext(), myActivity);
        startActivity(intent);
        finish();
    }


    private void getPositionGPS()
    {
        mLocationManagerGPS = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListenerGPS = new LocationListener() {
            public void onLocationChanged(Location location) {
                txtViewLatGPS.setText(Double.toString(location.getLatitude()));
                txtViewLongGPS.setText(Double.toString(location.getLongitude()));
                txtViewAltGPS.setText(Double.toString(location.getAltitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                showAlert(R.string.GPS_disabled);
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                btnGPS.setEnabled(false);
                mLocationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, mLocationListenerGPS);
            }
        }
    }

    private void getPositionNetwork() {
        mLocationManagerNetwork = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListenerNetwork = new LocationListener() {
            public void onLocationChanged(Location location) {
                txtViewLatNetwork.setText(Double.toString(location.getLatitude()));
                txtViewLongNetwork.setText(Double.toString(location.getLongitude()));
                txtViewAltNetwork.setText(Double.toString(location.getAltitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                showAlert(R.string.Network_disabled);
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                btnNetwork.setEnabled(false);
                mLocationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 0, mLocationListenerNetwork);
            }
        }
    }

    private void showAlert(int messageId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId).setCancelable(false).setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }).show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_watch_permissions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                }
            }).setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManagerGPS != null) {
            mLocationManagerGPS.removeUpdates(mLocationListenerGPS);
        }

        if (mLocationManagerNetwork != null) {
            mLocationManagerNetwork.removeUpdates(mLocationListenerNetwork);
        }

        txtViewLatGPS.setText(null);
        txtViewLongGPS.setText(null);
        txtViewAltGPS.setText(null);

        txtViewLatNetwork.setText(null);
        txtViewLongNetwork.setText(null);
        txtViewAltNetwork.setText(null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!btnGPS.isEnabled()) {
            btnGPS.setEnabled(true);
        }

        if (!btnNetwork.isEnabled()) {
            btnNetwork.setEnabled(true);
        }

    }

}