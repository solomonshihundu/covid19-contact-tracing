package com.ss.covid_19_tracer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MAIN_ACTIVITY";
    private LocationManager mLocationManagerGPS;
    private LocationListener mLocationListenerGPS;

    private static final String USER_LAT = "USER_LAT";
    private static final String USER_LOG = "USER_LOG";
    private static final String USER_PHONE = "USER_PHONE";

    private String user_latitude;
    private String user_longitude;
    private String user_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 206, 102, 102)));
        getSupportActionBar().setElevation(100);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
        {
    //        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
    //                new HomePageFragment()).commit();

        }

        searchPositionGPS();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getPhoneNumber()
    {
        Log.d(TAG,"ACQUIRING PHONE NUM");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG,"SMS,PHONE AND STATE PERMISSIONS GRANTED");
            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
             user_phone = tMgr.getLine1Number();
             Log.d(TAG,"PHONE: "+user_phone);
            return;
        } else {
            Log.d(TAG,"SMS,PHONE AND STATE PERMISSIONS DENIED, REQUESTING NOW");
            requestPermission();
            return;
        }
    }

    private int searchPositionGPS()
    {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG,"PERMISSION_GRANTED");

                mLocationManagerGPS = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                mLocationListenerGPS = new LocationListener()
                {
                    public void onLocationChanged(Location location)
                    {
                        user_latitude = Double.toString(location.getLatitude());
                        user_longitude = Double.toString(location.getLongitude());

                        Log.d(TAG,"LAT: "+user_latitude);
                        Log.d(TAG,"LONG: "+user_longitude);

                    }

                    public void onStatusChanged(String provider, int status, Bundle extras)
                    {
                    }

                    public void onProviderEnabled(String provider)
                    {
                    }

                    public void onProviderDisabled(String provider)
                    {
                        showAlert(R.string.GPS_disabled);
                    }
                };

                mLocationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, mLocationListenerGPS);

                return 0;

            }
            else
                {
                    requestLocationPermission();
                    return 1;
            }
    }


    private void showAlert(int messageId)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId).setCancelable(false).setPositiveButton(R.string.btn_yes,
                new DialogInterface.OnClickListener() {
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

    private void requestPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE}, 100);
        }
    }

    private void requestLocationPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
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
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 100:

                TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                user_phone = tMgr.getLine1Number();
                Log.d(TAG,"PHONE: "+user_phone);
                break;


            case 1:

                mLocationManagerGPS = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                mLocationListenerGPS = new LocationListener()
                {
                    public void onLocationChanged(Location location)
                    {
                        user_latitude = Double.toString(location.getLatitude());
                        user_longitude = Double.toString(location.getLongitude());
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras)
                    {
                    }

                    public void onProviderEnabled(String provider)
                    {
                    }

                    public void onProviderDisabled(String provider)
                    {
                        showAlert(R.string.GPS_disabled);
                    }
                };

                mLocationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, mLocationListenerGPS);
                break;


        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        if (mLocationManagerGPS != null)
        {
            mLocationManagerGPS.removeUpdates(mLocationListenerGPS);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
