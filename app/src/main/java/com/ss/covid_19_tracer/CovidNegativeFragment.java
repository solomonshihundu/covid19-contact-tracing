package com.ss.covid_19_tracer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CovidNegativeFragment extends Fragment
{

    private static final String TAG = "NEGATIVE";

    public CovidNegativeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_covid_negative, container, false);

        return view;
    }

}
