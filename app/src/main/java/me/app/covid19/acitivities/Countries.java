package me.app.covid19.acitivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.Query;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;
import me.app.covid19.adapters.AdapterCountries;
import me.app.covid19.models.Country;


public class Countries extends AppCompatActivity {

    RecyclerView  CountryView;
    Country country;
    AdapterCountries adapterCountries;
    List<Country> countryList;

    ImageView backButton;
    RelativeLayout relativeLayout, layout;
    EditText search_field;
    CircleImageView search_button;

    private static String Api_url = "https://disease.sh/v3/covid-19/countries/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        CountryView = findViewById(R.id.country_list);
        countryList = new ArrayList<>();

        search_field = findViewById(R.id.search_field);
        backButton = findViewById(R.id.backButton);
        relativeLayout = findViewById(R.id.layout);
        layout = findViewById(R.id.country_totalCases);
        //search_button = findViewById(R.id.search_button);

        //search_button.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        relativeLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        backButton.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim));
        CountryView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_droit));
        layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_droit));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fetchData();

    }


    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Api_url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++){
                        try {
                            JSONObject CountryObject = response.getJSONObject(i);

                            country = new Country();
                            country.setCountry(CountryObject.getString("country").toString());
                            country.setCases(CountryObject.getString("cases").toString());
                            country.setTodayCases(CountryObject.getString("todayCases").toString());
                            country.setTodayRecovered(CountryObject.getString("todayRecovered").toString());
                            country.setTodayDeaths(CountryObject.getString("todayDeaths").toString());
                            country.setDeaths(CountryObject.getString("deaths").toString());
                            country.setRecovered(CountryObject.getString("recovered").toString());
                            country.setActive(CountryObject.getString("active").toString());

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
