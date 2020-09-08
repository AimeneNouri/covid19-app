package me.app.covid19.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterCountries;
import me.app.covid19.models.Country;
import retrofit2.http.Url;

public class User_country extends AppCompatActivity {

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private String retrieveCountry;
    private TextView UserCountry, Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_country);

        Initialisation();
        RetrieveUserCountry();
        fetchData();
    }

    private void RetrieveUserCountry() {
        RootRef.child("Users")
                .child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    retrieveCountry = snapshot.child("User_Country").getValue().toString().toLowerCase();
                    Url.setText(("https://disease.sh/v3/covid-19/countries/").concat(retrieveCountry));
                }
                else {
                    Toast.makeText(User_country.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Initialisation() {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        UserCountry = findViewById(R.id.country_name);
        Url = findViewById(R.id.url);
    }

    private void fetchData() {
        StringRequest request = new StringRequest(Request.Method.GET, Url.getText().toString(), new Response.Listener<String>() {
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

                    JSONObject object = jsonObject.getJSONObject("countryInfo");
                    String country_flag = object.getString("flag");

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
}
