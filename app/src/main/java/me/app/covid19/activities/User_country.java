package me.app.covid19.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterCountries;
import me.app.covid19.models.Country;
import retrofit2.http.Url;

public class User_country extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    PieChart pieChart;

    private static String countryCode = "", countryName = "", countryIso = "";
    private TextView countryNam, total_cases, total_recovered, total_deaths, today_cases, today_recovered, today_deaths, total_active, lastUpdate, total_Critical, text5, daily_confirmed_cases, fatality_rate;
    private ImageView countryFlag, backButton;
    RelativeLayout relativeLayout1, relativeLayout2;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_country);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CODE", Context.MODE_PRIVATE);
        countryCode = sharedPreferences.getString("countryCode", "");
        countryName = sharedPreferences.getString("countryName", "");
        countryIso = Locale.getDefault().getISO3Country();

        Initialisation();
        fetchData2();
        fetchData();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        relativeLayout1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        backButton.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        relativeLayout2.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        lastUpdate.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        text5.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void Initialisation() {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        countryNam = findViewById(R.id.country_name);
        countryFlag = findViewById(R.id.country_flag);
        total_cases = findViewById(R.id.total_cases);
        total_deaths = findViewById(R.id.total_deaths);
        total_recovered = findViewById(R.id.total_recovered);
        today_cases = findViewById(R.id.today_cases);
        today_recovered = findViewById(R.id.today_recovered);
        today_deaths = findViewById(R.id.today_deaths);
        total_active = findViewById(R.id.total_active);
        lastUpdate = findViewById(R.id.text2);
        total_Critical = findViewById(R.id.total_Critical);
        backButton = findViewById(R.id.backButton);
        relativeLayout1 = findViewById(R.id.layout);
        relativeLayout2 = findViewById(R.id.layout1);
        backButton = findViewById(R.id.backButton);
        text5 = findViewById(R.id.text5);
        daily_confirmed_cases = findViewById(R.id.daily_confirmed_cases);
        pieChart = findViewById(R.id.piechart);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        fatality_rate = findViewById(R.id.fatality_rate);
    }

    private void fetchData() {
        String api_url = "https://disease.sh/v3/covid-19/countries/" + countryIso;

        StringRequest request = new StringRequest(Request.Method.GET, api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    String country = jsonObject.getString("country");
                    String cases = jsonObject.getString("cases");
                    String deaths = jsonObject.getString("deaths");
                    String recovered = jsonObject.getString("recovered");
                    String active = jsonObject.getString("active");
                    String todayCase = jsonObject.getString("todayCases");
                    String todayDeath = jsonObject.getString("todayDeaths");
                    String todayRecovered = jsonObject.getString("todayRecovered");
                    String critical = jsonObject.getString("critical");
                    String daily_cases_confirmed = jsonObject.getString("casesPerOneMillion");
                    long updated = jsonObject.getLong("updated");

                    JSONObject object = jsonObject.getJSONObject("countryInfo");
                    String country_flag = object.getString("flag");

                    DecimalFormat formatter = new DecimalFormat("###,###,##0");
                    countryNam.setText(country);
                    Picasso.get().load(country_flag).into(countryFlag);
                    total_cases.setText(formatter.format(Double.parseDouble(cases)));
                    total_deaths.setText(formatter.format(Double.parseDouble(deaths)));
                    total_recovered.setText(formatter.format(Double.parseDouble(recovered)));
                    today_cases.setText(formatter.format(Double.parseDouble(todayCase)));
                    today_recovered.setText(formatter.format(Double.parseDouble(todayRecovered)));
                    today_deaths.setText(formatter.format(Double.parseDouble(todayDeath)));
                    total_active.setText(formatter.format(Double.parseDouble(active)));
                    daily_confirmed_cases.setText(formatter.format(Double.parseDouble(daily_cases_confirmed)));
                    lastUpdate.setText(getDate(updated));
                    total_Critical.setText(formatter.format(Double.parseDouble(critical)));

                    pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(cases), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered), Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(deaths), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(active), Color.parseColor("#29B6F6")));
                    pieChart.startAnimation();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(User_country.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchData2() {
        String url = "https://covid-api.com/api/reports?iso=" + countryIso;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (!jsonArray.isNull(0)) {

                        for (int i=0; i<jsonArray.length(); i++){

                            if (jsonArray.length() > 0) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                double fatalityRate = object.getDouble("fatality_rate");
                                int hundred = 100;

                                double fatality = fatalityRate * hundred;

                                fatality_rate.setText(Double.toString(fatality));
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(User_country.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        RequestQueue requestQueue = Volley.newRequestQueue(User_country.this);
        requestQueue.add(stringRequest);
    }


    private String getDate(long milliSecond){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onRefresh() {
        onLoadingSwipeRefresh();
    }

    private void onLoadingSwipeRefresh(){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        String api_url = "https://disease.sh/v3/covid-19/countries/" + countryIso;
                        swipeRefreshLayout.setRefreshing(true);

                        StringRequest request = new StringRequest(Request.Method.GET, api_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    String country = jsonObject.getString("country");
                                    String cases = jsonObject.getString("cases");
                                    String deaths = jsonObject.getString("deaths");
                                    String recovered = jsonObject.getString("recovered");
                                    String active = jsonObject.getString("active");
                                    String todayCase = jsonObject.getString("todayCases");
                                    String todayDeath = jsonObject.getString("todayDeaths");
                                    String todayRecovered = jsonObject.getString("todayRecovered");
                                    String critical = jsonObject.getString("critical");
                                    String daily_cases_confirmed = jsonObject.getString("casesPerOneMillion");
                                    long updated = jsonObject.getLong("updated");

                                    JSONObject object = jsonObject.getJSONObject("countryInfo");
                                    String country_flag = object.getString("flag");

                                    DecimalFormat formatter = new DecimalFormat("###,###,##0");
                                    countryNam.setText(country);
                                    Picasso.get().load(country_flag).into(countryFlag);
                                    total_cases.setText(formatter.format(Double.parseDouble(cases)));
                                    total_deaths.setText(formatter.format(Double.parseDouble(deaths)));
                                    total_recovered.setText(formatter.format(Double.parseDouble(recovered)));
                                    today_cases.setText(formatter.format(Double.parseDouble(todayCase)));
                                    today_recovered.setText(formatter.format(Double.parseDouble(todayRecovered)));
                                    today_deaths.setText(formatter.format(Double.parseDouble(todayDeath)));
                                    total_active.setText(formatter.format(Double.parseDouble(active)));
                                    daily_confirmed_cases.setText(formatter.format(Double.parseDouble(daily_cases_confirmed)));
                                    lastUpdate.setText(getDate(updated));
                                    total_Critical.setText(formatter.format(Double.parseDouble(critical)));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                swipeRefreshLayout.setRefreshing(false);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(User_country.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(request);
                    }
                }
        );
    }
}
