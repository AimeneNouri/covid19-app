package me.app.covid19.fragments.test_covid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.app.covid19.R;

public class fragment1 extends Fragment {

    View fragment1;

    public fragment1() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment1 = inflater.inflate(R.layout.fragment_fragment1, container, false);

        return fragment1;
    }
}
