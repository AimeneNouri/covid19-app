package me.app.covid19.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterNews;
import me.app.covid19.fragments.newsTablayout.Country;
import me.app.covid19.fragments.newsTablayout.World;
import me.app.covid19.models.News;


/**
 * A simple {@link Fragment} subclass.
 */
public class newsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View newsView;

    private ProgressBar progressBar;

    private AdapterNews adapterNews;
    public static List<News> newsList = new ArrayList<>();
    News news;

    TabLayout tabLayout;
    ViewPager viewPager;

    public newsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newsView = inflater.inflate(R.layout.fragment_news, container, false);

        //recyclerView = newsView.findViewById(R.id.recycler_news);
        //progressBar = newsView.findViewById(R.id.progressBar);

        tabLayout = newsView.findViewById(R.id.tabs);
        viewPager = newsView.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        SetUpViewPager(viewPager);

        return newsView;
    }

    public void SetUpViewPager (ViewPager viewPage){
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager());

        String worldTitle = getString(R.string.world);
        String countryTitle = getString(R.string.mycountry);

        adapter.AddFragmentPage(new World(), worldTitle);
        adapter.AddFragmentPage(new Country(), countryTitle);

        viewPage.setAdapter(adapter);
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter{
        public List<Fragment> fragments = new ArrayList<>();
        private List<String> myPagerTitle = new ArrayList<>();

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void AddFragmentPage(Fragment Frag, String Tittle) {
            fragments.add(Frag);
            myPagerTitle.add(Tittle);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return myPagerTitle.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
