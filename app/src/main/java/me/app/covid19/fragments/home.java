package me.app.covid19.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.app.covid19.R;
import me.app.covid19.acitivities.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    private ImageView settingsButton, searchButton;
    private View HomeView;

    public home() {/* Required empty public constructor*/}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeView = inflater.inflate(R.layout.homefragment, container, false);

        settingsButton = HomeView.findViewById(R.id.settingsButton);
        searchButton = HomeView.findViewById(R.id.searchButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getContext(), UserInfo.class);
                startActivity(infoIntent);
            }
        });

        return HomeView;
    }
}
