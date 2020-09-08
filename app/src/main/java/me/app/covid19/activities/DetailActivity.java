package me.app.covid19.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;

public class DetailActivity extends AppCompatActivity {

    private TextView countryNam, total_cases, total_recovered, total_deaths, today_cases, today_recovered, today_deaths, total_active, lastUpdate;
    private CircleImageView country_flag;

    private int positionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        positionId = intent.getIntExtra("position", 0);

        countryNam = findViewById(R.id.country_name);
        country_flag = findViewById(R.id.country_flag);
        total_cases = findViewById(R.id.total_cases);
        total_deaths = findViewById(R.id.total_deaths);
        total_recovered = findViewById(R.id.total_recovered);
        today_cases = findViewById(R.id.today_cases);
        today_recovered = findViewById(R.id.today_recovered);
        today_deaths = findViewById(R.id.today_deaths);
        total_active = findViewById(R.id.total_active);
        lastUpdate = findViewById(R.id.text2);

        DecimalFormat formatter = new DecimalFormat("###,###,##0");
        countryNam.setText(Countries.countryList.get(positionId).getCountry());
        total_cases.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getCases())));
        today_cases.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getTodayCases())));
        today_recovered.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getTodayRecovered())));
        today_deaths.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getTodayDeaths())));
        total_deaths.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getDeaths())));
        total_recovered.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getRecovered())));
        total_active.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getActive())));
        lastUpdate.setText("Last update: "+getDate(Countries.countryList.get(positionId).getLastUpdate()));
        Picasso.get().load(Countries.countryList.get(positionId).getFlag()).into(country_flag);
    }

    private String getDate(long milliSecond){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm aaa");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }
}
