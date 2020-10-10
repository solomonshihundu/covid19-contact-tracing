package com.ss.covid_19_tracer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity
{
    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                toNextActivity(MainActivity.class);
            }

        },SPLASH_TIMEOUT);
    }

    private void toNextActivity(Class myActivity)
    {
        Intent intent = new Intent(this, myActivity);
        if(intent != null)
        {
            startActivity(intent);
            finish();
        }

    }
}
