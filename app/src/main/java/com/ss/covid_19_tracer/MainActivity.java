package com.ss.covid_19_tracer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MAIN_ACTIVITY";

    private static final String USER_LAT = "USER_LAT";
    private static final String USER_LOG = "USER_LOG";
    private static final String USER_PHONE = "USER_PHONE";

    private String user_phone;
    private String user_latitude;
    private String user_longitude;

    private FusedLocationProviderClient fusedLocationClient;

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
            getCurrentLocation();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomePageFragment()).commit();
        }

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
       ///     requestPermission();
            return;
        }
    }

    private void getCurrentLocation()
    {
        Log.d(TAG,"GETTING CURRENT LOCATION......");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG,"LOCATION PERMISSION GRANTED......");

            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {

                            Location location = task.getResult();

                            if(location != null)
                            {
                                Log.d(TAG,"LOCATION DATA AVAILABLE");
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                                try
                                {
                                    List<Address> addressList  = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                                    user_latitude = Double.toString(addressList.get(0).getLatitude());
                                    user_longitude = Double.toString(addressList.get(0).getLongitude());

                                   Bundle bundle = new Bundle();

                                   bundle.putString(USER_LAT,user_latitude);
                                   bundle.putString(USER_LOG,user_longitude);

                                   HomePageFragment homePageFragment = new HomePageFragment();
                                   homePageFragment.setArguments(bundle);

                                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            homePageFragment).commit();

                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();

                                }
                            }

                        }
                    });

        } else {
            Log.d(TAG,"LOCATION PERMISSION DENIED......");
            requestLocationPermission();
        }
    }


        private void showAlert ( int messageId)
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

        private void requestPermission ()
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE}, 100);
            }
        }

        private void requestLocationPermission ()
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
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

}
