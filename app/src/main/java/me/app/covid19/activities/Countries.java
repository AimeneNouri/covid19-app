package me.app.covid19.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterCountries;
import me.app.covid19.models.Country;


public class Countries extends AppCompatActivity{

    RecyclerView  CountryView;
    Country country;
    private AdapterCountries adapterCountries;
    public static List<Country> countryList = new ArrayList<>();
    private TextView today_date;

    ImageView backButton;
    RelativeLayout relativeLayout, layout, layout1;
    EditText search_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        CountryView = findViewById(R.id.country_list);
        search_field = findViewById(R.id.search_field);
        backButton = findViewById(R.id.backButton);
        relativeLayout = findViewById(R.id.layout);
        layout = findViewById(R.id.country_totalCases);
        layout1 = findViewById(R.id.layout1);
        today_date = findViewById(R.id.today_date);

        relativeLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        layout1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        CountryView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_droit));
        layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_droit));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fetchData();
        fetchLastUpdate();

        search_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_field.getText().toString().toLowerCase(Locale.getDefault());
                adapterCountries.filter(text);
            }
        });

    }

    private void fetchLastUpdate() {
        String url = "https://disease.sh/v3/covid-19/all/";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    DecimalFormat formatter = new DecimalFormat("###,###,##0");

                    today_date.setText(getDate(jsonObject.getLong("updated")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error2", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private String getDate(long milliSecond){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }

    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String api_url = "https://disease.sh/v3/covid-19/countries/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, api_url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++){
                        try {
                            JSONObject CountryObject = response.getJSONObject(i);

                            country = new Country();
                            country.setCountry(CountryObject.getString("country").toString());
                            country.setCases(CountryObject.getInt("cases"));
                            country.setTodayCases(CountryObject.getString("todayCases").toString());
                            country.setTodayRecovered(CountryObject.getString("todayRecovered").toString());
                            country.setTodayDeaths(CountryObject.getString("todayDeaths").toString());
                            country.setDeaths(CountryObject.getString("deaths").toString());
                            country.setRecovered(CountryObject.getString("recovered").toString());
                            country.setActive(CountryObject.getString("active").toString());
                            country.setCritical(CountryObject.getString("critical").toString());
                            country.setLastUpdate(CountryObject.getLong("updated"));
                            country.setCasesPerOneMillion(CountryObject.getString("casesPerOneMillion").toString());

                            JSONObject object = CountryObject.getJSONObject("countryInfo");
                            country.setFlag(object.getString("flag"));

                            countryList.add(country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    CountryView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterCountries = new AdapterCountries(getApplicationContext(), countryList);
                    CountryView.setAdapter(adapterCountries);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Countries.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        queue.add(jsonArrayRequest);
    }

}
