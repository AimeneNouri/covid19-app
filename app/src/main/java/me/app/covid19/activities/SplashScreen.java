package me.app.covid19.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import me.app.covid19.R;
import me.app.covid19.activities.onboard.onboardCloseContact;

public class SplashScreen extends AppCompatActivity {

    public static int TIME_OUT = 3100;
    private ImageView LogoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LogoImage = findViewById(R.id.corona_logo);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, onboardCloseContact.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        },TIME_OUT);

    }
}
