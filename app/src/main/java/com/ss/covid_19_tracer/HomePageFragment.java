package com.ss.covid_19_tracer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomePageFragment extends Fragment implements View.OnClickListener
{
    private FloatingActionButton covid_positive_btn;
    private FloatingActionButton covid_negative_btn;
    private static final String TAG = "HOME_PAGE";

    public HomePageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        covid_negative_btn = view.findViewById(R.id.covid_negative_btn);
        covid_negative_btn.setOnClickListener(this);

        covid_positive_btn = view.findViewById(R.id.covid_positive_btn);
        covid_positive_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        if((view.getId()) == covid_positive_btn.getId())
        {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CovidPositiveFragment()).commit();
        }
        else if((view.getId()) == covid_negative_btn.getId())
        {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CovidNegativeFragment()).commit();
        }
    }
}
