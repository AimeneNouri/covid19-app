package me.app.covid19.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterStatistics;
import me.app.covid19.models.Country;
import me.app.covid19.models.Utils;

public class statisticFragment extends Fragment {

    private View StatisticView;
    Country country;

    private ImageView country_flag1, country_flag2, country_flag3;

    private TextView total_casesUsa, total_casesINDIA, total_casesBRAZIL;
    private TextView countryName1, countryName2, countryName3, worldCases, TodayWorldCases, lastUpdateCases;

    private RecyclerView recyclerView;
    private AdapterStatistics adapterStatistics;
    public static List<Country> countryList = new ArrayList<>();

    public statisticFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StatisticView = inflater.inflate(R.layout.fragment_statistic, container, false);

        country_flag1 = StatisticView.findViewById(R.id.country_flag1);
        country_flag2 = StatisticView.findViewById(R.id.country_flag2);
        country_flag3 = StatisticView.findViewById(R.id.country_flag3);

        total_casesUsa = StatisticView.findViewById(R.id.total_casesUsa);
        total_casesINDIA = StatisticView.findViewById(R.id.total_casesINDIA);
        total_casesBRAZIL = StatisticView.findViewById(R.id.total_casesBRAZIL);


        countryName1 = StatisticView.findViewById(R.id.country_name1);
        countryName2 = StatisticView.findViewById(R.id.country_name2);
        countryName3 = StatisticView.findViewById(R.id.country_name3);

        worldCases = StatisticView.findViewById(R.id.worldCases);
        TodayWorldCases = StatisticView.findViewById(R.id.TodayWorldCases);
        recyclerView = StatisticView.findViewById(R.id.recycler_view);
        lastUpdateCases = StatisticView.findViewById(R.id.lastUpdateCases);

        //fetching data for all Countries
        fetchDataAllCountry();

        //fetching data for USA
        fetchDataUSA();

        //fetching data for USA
        fetchDataINDIA();

        //fetching data for USA
        fetchDataBRAZIL();

        //fetching data for world
        fetchCasesWorld();

        return StatisticView;
    }

    private void fetchDataAllCountry() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

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
                                country.setCases(CountryObject.getString("cases").toString());
                                country.setTodayCases(CountryObject.getString("todayCases").toString());
                                country.setTodayRecovered(CountryObject.getString("todayRecovered").toString());
                                country.setTodayDeaths(CountryObject.getString("todayDeaths").toString());
                                country.setDeaths(CountryObject.getString("deaths").toString());
                                country.setRecovered(CountryObject.getString("recovered").toString());
                                country.setActive(CountryObject.getString("active").toString());
                                country.setCritical(CountryObject.getString("critical").toString());
                                country.setLastUpdate(CountryObject.getLong("updated"));

                                JSONObject object = CountryObject.getJSONObject("countryInfo");
                                country.setFlag(object.getString("flag"));

                                countryList.add(country);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapterStatistics = new AdapterStatistics(getContext(), countryList);
                        recyclerView.setAdapter(adapterStatistics);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void fetchCasesWorld() {
        String url = "https://disease.sh/v3/covid-19/all/";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    DecimalFormat formatter = new DecimalFormat("###,###,##0");
                    String cases = jsonObject.getString("cases");
                    String TodayCases = jsonObject.getString("todayCases");
                    long lastUpdate = jsonObject.getLong("updated");

                    worldCases.setText(formatter.format(Double.parseDouble(cases)));
                    TodayWorldCases.setText("+"+formatter.format(Double.parseDouble(TodayCases)));
                    lastUpdateCases.setText("Last Update " + Utils.getDate(lastUpdate));

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

    private void fetchDataUSA() {
        String url = "https://disease.sh/v3/covid-19/countries/us";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    DecimalFormat formatter = new DecimalFormat("###,###,##0");
                    String cases = jsonObject.getString("cases");
                    String recovered = jsonObject.getString("recovered");
                    String countryName = jsonObject.getString("country");

                    JSONObject object = jsonObject.getJSONObject("countryInfo");
                    String country_flag = object.getString("flag");

                    countryName1.setText(countryName);
                    total_casesUsa.setText(formatter.format(Double.parseDouble(cases)));
                    Picasso.get().load(country_flag).into(country_flag1);

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

    private void fetchDataINDIA() {
        String url = "https://disease.sh/v3/covid-19/countries/in";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    DecimalFormat formatter = new DecimalFormat("###,###,##0");
                    String cases = jsonObject.getString("cases");
                    String recovered = jsonObject.getString("recovered");
                    String countryName = jsonObject.getString("country");

                    JSONObject object = jsonObject.getJSONObject("countryInfo");
                    String country_flag = object.getString("flag");

                    countryName2.setText(countryName);
                    total_casesINDIA.setText(formatter.format(Double.parseDouble(cases)));
                    Picasso.get().load(country_flag).into(country_flag2);


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

    private void fetchDataBRAZIL() {
        String url = "https://disease.sh/v3/covid-19/countries/br";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    DecimalFormat formatter = new DecimalFormat("###,###,##0");
                    String cases = jsonObject.getString("cases");
                    String recovered = jsonObject.getString("recovered");
                    String countryName = jsonObject.getString("country");

                    JSONObject object = jsonObject.getJSONObject("countryInfo");
                    String country_flag = object.getString("flag");

                    countryName3.setText(countryName);
                    total_casesBRAZIL.setText(formatter.format(Double.parseDouble(cases)));
                    Picasso.get().load(country_flag).into(country_flag3);


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
