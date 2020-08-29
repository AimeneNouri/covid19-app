package me.app.covid19.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;
import me.app.covid19.acitivities.Countries;
import me.app.covid19.acitivities.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    private ImageView searchButton;
    private CircleImageView settingsButton;
    private View HomeView;

    private TextView lastUpdate;

    private TextView AllCases, TotalDeaths, TotalRecovered, ActiveCases, TodayDeaths, TodayCases, TodayRecovered;

    public home() {/* Required empty public constructor*/}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeView = inflater.inflate(R.layout.homefragment, container, false);

        AllCases = HomeView.findViewById(R.id.allcases);
        TotalDeaths = HomeView.findViewById(R.id.DeathCases);
        TotalRecovered = HomeView.findViewById(R.id.CaseClosed);
        ActiveCases = HomeView.findViewById(R.id.ActiveCase);
        TodayDeaths = HomeView.findViewById(R.id.TodayDeaths);
        TodayRecovered = HomeView.findViewById(R.id.TodayRecovered);
        TodayCases = HomeView.findViewById(R.id.TodayCases);
        settingsButton = HomeView.findViewById(R.id.settingsButton);
        searchButton = HomeView.findViewById(R.id.searchButton);
        lastUpdate = HomeView.findViewById(R.id.lastUpdateCases);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getContext(), UserInfo.class);
                startActivity(infoIntent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CountriesIntent = new Intent(getContext(), Countries.class);
                startActivity(CountriesIntent);
            }
        });

        //fetchData
        fetchData();

        fetchDataLastUpdate();

        return HomeView;
    }

    private void fetchDataLastUpdate() {
        String url = "https://api.covid19api.com/summary";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    lastUpdate.setText(jsonObject.getString("Date"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error1", error.getMessage());
                }
            });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void fetchData() {
        String url = "https://disease.sh/v3/covid-19/all/";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    AllCases.setText(jsonObject.getString("cases"));
                    TotalDeaths.setText(jsonObject.getString("deaths"));
                    TotalRecovered.setText(jsonObject.getString("recovered"));
                    ActiveCases.setText(jsonObject.getString("active"));
                    TodayCases.setText(jsonObject.getString("todayCases"));
                    TodayDeaths.setText(jsonObject.getString("todayDeaths"));
                    TodayRecovered.setText(jsonObject.getString("todayRecovered"));

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}
