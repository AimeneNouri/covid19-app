package me.app.covid19.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.app.covid19.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class newsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View newsView;

    public newsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newsView = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = newsView.findViewById(R.id.recycler_news);

        return newsView;
    }
}
