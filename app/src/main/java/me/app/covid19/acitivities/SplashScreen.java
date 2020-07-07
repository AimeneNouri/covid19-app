package me.app.covid19.acitivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import me.app.covid19.MainActivity;
import me.app.covid19.R;

public class SplashScreen extends AppCompatActivity {

    public static int TIME_OUT = 4000;
    private TextView textView;
    private ImageView LogoImage;

    private Button Login_Button, Register_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LogoImage = findViewById(R.id.corona_logo);
        textView = findViewById(R.id.textView);
        Login_Button = findViewById(R.id.login_button);
        Register_Button = findViewById(R.id.register_button);

        textView.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.exobold));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();*/
            }
        },TIME_OUT);

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        Register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
