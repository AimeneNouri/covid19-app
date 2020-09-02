package me.app.covid19.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.app.covid19.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class infoFragment extends Fragment {

    private View InfoView;
    TextView alloSamu, alloYakkada;

    private int REQUEST_CALL = 1;

    public infoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.getContext().setTheme(R.style.AppTheme_Countries);
        InfoView = inflater.inflate(R.layout.fragment_info, container, false);


        alloSamu = InfoView.findViewById(R.id.AlloSamu);
        alloYakkada = InfoView.findViewById(R.id.AlloYakkada);

        alloSamu.setText(Html.fromHtml("<u>141</u>"));
        alloYakkada.setText(Html.fromHtml("<u>080 100 47 47</u>"));

        alloSamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel1 = "tel:" +  alloSamu.getText().toString();

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(tel1));
                    startActivity(intent);
                }else {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }

            }
        });

        alloYakkada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel2 = "tel:" +  alloYakkada.getText().toString();

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel2));
                    startActivity(intent);
                }else {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }
            }
        });

        return InfoView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("permission", "granted");
            }else {
                Log.d("permission", "not granted");
            }
        }
    }
}
