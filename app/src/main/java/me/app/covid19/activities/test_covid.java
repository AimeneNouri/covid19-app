package me.app.covid19.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import me.app.covid19.fragments.test_covid.fragment1;
import me.app.covid19.fragments.test_covid.fragment2;

import me.app.covid19.R;

public class test_covid extends AppCompatActivity {

    private ImageView backButton;
    private Button yes, no;

    private fragment1 fragment1;
    private fragment2 fragment2;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_covid);

        backButton = findViewById(R.id.backButton);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        frameLayout = findViewById(R.id.main_frame);

        fragment1 = new fragment1();
        fragment2 = new fragment2();

        setFragment(fragment1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(fragment2);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(fragment2);
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
