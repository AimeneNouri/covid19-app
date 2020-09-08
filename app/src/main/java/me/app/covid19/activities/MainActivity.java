package me.app.covid19.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import me.app.covid19.R;
import me.app.covid19.fragments.home;
import me.app.covid19.fragments.infoFragment;
import me.app.covid19.fragments.newsFragment;
import me.app.covid19.fragments.current_locationFragment;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private BottomNavigationView mBottomNav;

    private FrameLayout frameLayout;

    private home home;
    private infoFragment infoFragment;
    private newsFragment newsFragment;
    private current_locationFragment current_locationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mBottomNav = (BottomNavigationView) findViewById(R.id.main_tabs);
        frameLayout = findViewById(R.id.main_frame);


        home = new home();
        newsFragment = new newsFragment();
        infoFragment = new infoFragment();
        current_locationFragment = new current_locationFragment();

        setFragment(home);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        setFragment(home);
                        return true;

                    case R.id.news:
                        setFragment(newsFragment);
                        return true;

                    case R.id.info:
                        setFragment(infoFragment);
                        return true;

                    case R.id.statistic:
                        setFragment(current_locationFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
