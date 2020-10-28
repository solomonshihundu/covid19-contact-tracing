package com.ss.covid_19_tracer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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

    public static  String USER_LAT = "USER_LAT";
    public static  String USER_LOG = "USER_LOG";

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.lightCoral));
        getSupportActionBar().setElevation(100);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
        {
            getCurrentLocation();
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

                                    USER_LAT = Double.toString(addressList.get(0).getLatitude());
                                    USER_LOG = Double.toString(addressList.get(0).getLongitude());

                                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            new ContactFragment()).commit();

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
