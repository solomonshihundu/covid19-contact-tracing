package com.ss.covid_19_tracer;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "CONTACT_FRAGMENT";
    private Button proceedToHomePage;
    private Button callHotline;
    private EditText contactEditTxt;
    private Bundle bundle;

    public static String USER_PHONE = "USER_PHONE";

    public ContactFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        proceedToHomePage = view.findViewById(R.id.proceedBtn);
        proceedToHomePage.setOnClickListener(this);

        callHotline = view.findViewById(R.id.callBtn);
        callHotline.setOnClickListener(this);

        contactEditTxt = view.findViewById(R.id.contactEditTxt);

        bundle = new Bundle();

        return view;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.proceedBtn)
        {
            USER_PHONE = contactEditTxt.getText().toString().trim();

            if (TextUtils.isEmpty(USER_PHONE)) {
                Toast.makeText(getContext(), "Enter Phone Number ", Toast.LENGTH_SHORT).show();
            }
            else
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePageFragment()).commit();


        }
        else if (view.getId() == R.id.callBtn)
        {
            if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermission();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + bundle.getString("*719#")));

            getContext().startActivity(intent);

        }

    }

    private void requestPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + bundle.getString("mobilePhone")));

                    getContext().startActivity(intent);

                    return;

                }
                break;
        }
    }
}
