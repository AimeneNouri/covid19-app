package me.app.covid19.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;
import me.app.covid19.models.Utils;

public class DetailActivity extends AppCompatActivity {

    private TextView countryNam, total_cases, total_recovered, total_deaths, today_cases, today_recovered, today_deaths, total_active, lastUpdate, text5, total_Critical, daily_confirmed_cases;
    private ImageView country_flag, backButton;
    PieChart pieChart;

    private int positionId;
    private int TotalCases;
    private String TotalRecovered;
    private String TotalDeaths;
    private String TotalActive;
    RelativeLayout relativeLayout1, relativeLayout2;

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
        total_Critical = findViewById(R.id.total_Critical);
        relativeLayout1 = findViewById(R.id.layout);
        relativeLayout2 = findViewById(R.id.layout1);
        backButton = findViewById(R.id.backButton);
        text5 = findViewById(R.id.text5);
        daily_confirmed_cases = findViewById(R.id.daily_confirmed_cases);
        pieChart = findViewById(R.id.piechart);

        relativeLayout1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        backButton.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        relativeLayout2.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_droit));
        lastUpdate.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        text5.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DecimalFormat formatter = new DecimalFormat("###,###,##0");
        countryNam.setText(Countries.countryList.get(positionId).getCountry());
        total_cases.setText(formatter.format(Countries.countryList.get(positionId).getCases()));
        today_cases.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getTodayCases())));
        today_recovered.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getTodayRecovered())));
        today_deaths.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getTodayDeaths())));
        total_deaths.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getDeaths())));
        total_recovered.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getRecovered())));
        total_active.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getActive())));
        total_Critical.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getCritical())));
        daily_confirmed_cases.setText(formatter.format(Double.parseDouble(Countries.countryList.get(positionId).getCasesPerOneMillion())));
        lastUpdate.setText(getDate(Countries.countryList.get(positionId).getLastUpdate()));
        Picasso.get().load(Countries.countryList.get(positionId).getFlag()).into(country_flag);

        TotalCases = Countries.countryList.get(positionId).getCases();
        TotalActive = Countries.countryList.get(positionId).getActive();
        TotalDeaths = Countries.countryList.get(positionId).getDeaths();
        TotalRecovered = Countries.countryList.get(positionId).getRecovered();

        pieChart.addPieSlice(new PieModel("Cases", TotalCases, Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(TotalRecovered), Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(TotalDeaths), Color.parseColor("#EF5350")));
        pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(TotalActive), Color.parseColor("#29B6F6")));
        pieChart.startAnimation();

    }

    private String getDate(long milliSecond){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
