package me.app.covid19.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import me.app.covid19.R;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private ImageView Search, Setting;

    private BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Search = findViewById(R.id.searchButton);
        mBottomNav = (BottomNavigationView) findViewById(R.id.main_tabs);
        Setting = findViewById(R.id.settingsButton);

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserInfoIntent = new Intent(MainActivity.this, UserInfo.class);
                startActivity(UserInfoIntent);
            }
        });
    }
}
