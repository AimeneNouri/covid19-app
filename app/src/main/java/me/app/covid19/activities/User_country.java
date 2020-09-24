package me.app.covid19.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterCountries;
import me.app.covid19.models.Country;
import retrofit2.http.Url;

public class User_country extends AppCompatActivity {

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    PieChart pieChart;

    private static String countryCode = "";
    private TextView countryNam, total_cases, total_recovered, total_deaths, today_cases, today_recovered, today_deaths, total_active, lastUpdate, total_Critical, text5;
    private ImageView countryFlag, backButton;
    RelativeLayout relativeLayout1, relativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_country);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CODE", Context.MODE_PRIVATE);
        countryCode = sharedPreferences.getString("countryCode", "");

        Initialisation();
        fetchData();

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
        pieChart = findViewById(R.id.piechart);
    }

    private void fetchData() {
        String api_url = "https://disease.sh/v3/covid-19/countries/" + countryCode;

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

    private String getDate(long milliSecond){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }
}
