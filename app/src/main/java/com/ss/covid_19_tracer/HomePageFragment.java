package com.ss.covid_19_tracer;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomePageFragment extends Fragment implements View.OnClickListener
{
    private FloatingActionButton covid_positive_btn;
    private FloatingActionButton covid_negative_btn;
    private static final String TAG = "HOME_PAGE";

    private static final String USER_LAT = "USER_LAT";
    private static final String USER_LOG = "USER_LOG";
    private static final String USER_PHONE= "USER_PHONE";

    private String latitude = "";
    private String longitude = "";
    private String phone = "0712345678";

    private DatabaseReference tracerDatabaseReference;

    public HomePageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        Log.d(TAG,"HOME_PAGE");

        Bundle bundle = getArguments();
        if(bundle != null)
        {

            latitude = bundle.getString(USER_LAT);
            longitude = bundle.getString(USER_LOG);

            Log.d(TAG,latitude);
            Log.d(TAG,longitude);

        }
        else
        {
            Log.d(TAG,"DATA BUNDLE NOT FOUND");
        }

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
            loadTracerData(phone,latitude,longitude,"1");
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CovidPositiveFragment()).commit();
        }
        else if((view.getId()) == covid_negative_btn.getId())
        {
            loadTracerData(phone,latitude, longitude,"0");
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CovidNegativeFragment()).commit();
        }
    }

    private void loadTracerData(String user_phone,String user_lat,String user_long,String user_status)
    {
        tracerDatabaseReference = FirebaseDatabase.getInstance().getReference("tracer-19");
        DatabaseReference personRef = tracerDatabaseReference.child("0712345678");

        if(tracerDatabaseReference != null)
        {
            Person person = new Person(user_phone,user_lat,user_long,user_status);

            personRef.setValue(person).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"Process successful !",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"Fatal Error !",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }
}
