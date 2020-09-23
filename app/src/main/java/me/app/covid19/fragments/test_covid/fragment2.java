package me.app.covid19.fragments.test_covid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.app.covid19.R;

public class fragment2 extends Fragment {

    View fragment2;

    public fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment2 = inflater.inflate(R.layout.fragment_fragment2, container, false);

        return fragment2;
    }
}
