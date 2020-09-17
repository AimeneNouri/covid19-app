package me.app.covid19.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterStatistics;
import me.app.covid19.models.Country;
import me.app.covid19.models.Utils;

public class statisticFragment extends Fragment {

    private View StatisticView;
    Country country;

    private RelativeLayout form1, form2, form3, sub_form1_case, sub_form2_case, sub_form3_case, sub_form1_recovered, sub_form2_recovered, sub_form3_recovered;

    private ImageView country_flag1, country_flag2, country_flag3, sort_by_Cases, sort_by_Alphabet;

    private TextView total_casesUsa, total_casesINDIA, total_casesBRAZIL;
    private TextView countryName1, countryName2, countryName3, worldCases, TodayWorldCases, lastUpdateCases;

    private TextView total_recoveredUsa, total_recoveredIndia, total_recoveredBrazil;

    private RecyclerView recyclerView;
    private AdapterStatistics adapterStatistics;
    public static List<Country> countryList = new ArrayList<>();

    public boolean isClickedFirstTime = true;

    public statisticFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StatisticView = inflater.inflate(R.layout.fragment_statistic, container, false);

        form1 = StatisticView.findViewById(R.id.form1);
        form2 = StatisticView.findViewById(R.id.form2);
        form3 = StatisticView.findViewById(R.id.form3);

        sub_form1_case = StatisticView.findViewById(R.id.sub_layout1);
        sub_form1_recovered = StatisticView.findViewById(R.id.sub_layout_1);

        sub_form2_case = StatisticView.findViewById(R.id.sub_layout2);
        sub_form2_recovered = StatisticView.findViewById(R.id.sub_layout_2);

        sub_form3_case = StatisticView.findViewById(R.id.sub_layout3);
        sub_form3_recovered = StatisticView.findViewById(R.id.sub_layout_3);

        country_flag1 = StatisticView.findViewById(R.id.country_flag1);
        country_flag2 = StatisticView.findViewById(R.id.country_flag2);
        country_flag3 = StatisticView.findViewById(R.id.country_flag3);

        total_casesUsa = StatisticView.findViewById(R.id.total_casesUsa);
        total_casesINDIA = StatisticView.findViewById(R.id.total_casesINDIA);
        total_casesBRAZIL = StatisticView.findViewById(R.id.total_casesBRAZIL);

        total_recoveredUsa = StatisticView.findViewById(R.id.total_RecoveredUsa);
        total_recoveredIndia = StatisticView.findViewById(R.id.total_RecoveredIndia);
        total_recoveredBrazil = StatisticView.findViewById(R.id.total_RecoveredBrazil);

        countryName1 = StatisticView.findViewById(R.id.country_name1);
        countryName2 = StatisticView.findViewById(R.id.country_name2);
        countryName3 = StatisticView.findViewById(R.id.country_name3);

        worldCases = StatisticView.findViewById(R.id.worldCases);
        TodayWorldCases = StatisticView.findViewById(R.id.TodayWorldCases);
        recyclerView = StatisticView.findViewById(R.id.recycler_view);
        lastUpdateCases = StatisticView.findViewById(R.id.lastUpdateCases);

        sort_by_Cases = StatisticView.findViewById(R.id.sort_by_cases);
        sort_by_Alphabet = StatisticView.findViewById(R.id.sort_by_alphabet);

        //fetching data for all Countries
        fetchDataAllCountry();

        //fetching data for USA
        fetchDataUSA();

        //fetching data for INDIA
        fetchDataINDIA();

        //fetching data for BRAZIL
        fetchDataBRAZIL();

        //fetching data for world
        fetchCasesWorld();

        sort_by_Cases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sorting countryList by largest cases
                countryList.clear();
                fetchDataAllCountry_Sort_Cases();
            }
        });

        sort_by_Alphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sorting countryList by alphabet
                countryList.clear();
                fetchDataAllCountry_Sort_Alphabet();
            }
        });

        form1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickedFirstTime) {
                    sub_form1_case.setVisibility(View.GONE);
                    total_recoveredUsa.setVisibility(View.VISIBLE);
                    sub_form1_recovered.setVisibility(View.VISIBLE);
                    form1.setBackgroundResource(R.drawable.form_recovered);
                    isClickedFirstTime = false;
                }else {
                    sub_form1_case.setVisibility(View.VISIBLE);
                    total_recoveredUsa.setVisibility(View.GONE);
                    sub_form1_recovered.setVisibility(View.GONE);
                    form1.setBackgroundResource(R.drawable.form_usa);
                    isClickedFirstTime = true;
                }
            }
        });

        form2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickedFirstTime) {
                    sub_form2_case.setVisibility(View.GONE);
                    sub_form2_recovered.setVisibility(View.VISIBLE);
                    form2.setBackgroundResource(R.drawable.form_recovered);
                    total_recoveredIndia.setVisibility(View.VISIBLE);
                    isClickedFirstTime = false;
                }else {
                    sub_form2_case.setVisibility(View.VISIBLE);
                    sub_form2_recovered.setVisibility(View.GONE);
                    form2.setBackgroundResource(R.drawable.form_india);
                    total_recoveredIndia.setVisibility(View.GONE);
                    isClickedFirstTime = true;
                }
            }
        });

        form3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickedFirstTime) {
                    sub_form3_case.setVisibility(View.GONE);
                    sub_form3_recovered.setVisibility(View.VISIBLE);
                    form3.setBackgroundResource(R.drawable.form_recovered);
                    total_recoveredBrazil.setVisibility(View.VISIBLE);
                    isClickedFirstTime = false;
                }else {
                    sub_form3_case.setVisibility(View.VISIBLE);
                    sub_form3_recovered.setVisibility(View.GONE);
                    form3.setBackgroundResource(R.drawable.form_brazil);
                    total_recoveredBrazil.setVisibility(View.GONE);
                    isClickedFirstTime = true;
                }
            }
        });

        return StatisticView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.color_fragment));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        countryList.clear();
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
                                country.setCases(CountryObject.getInt("cases"));
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

                        //sorting countryList by largest cases
                        Collections.sort(countryList, new Comparator<Country>() {
                            @Override
                            public int compare(Country o1, Country o2) {
                                if(o1.getCases() > o2.getCases()){
                                    return -1;
                                }else {
                                    return 1;
                                }
                            }
                        });

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

    private void fetchDataAllCountry_Sort_Alphabet() {
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
                                country.setCases(CountryObject.getInt("cases"));
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

    private void fetchDataAllCountry_Sort_Cases() {
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
                                country.setCases(CountryObject.getInt("cases"));
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

                        //sorting countryList by largest cases
                        Collections.sort(countryList, new Comparator<Country>() {
                            @Override
                            public int compare(Country o1, Country o2) {
                                if(o1.getCases() > o2.getCases()){
                                    return -1;
                                }else {
                                    return 1;
                                }
                            }
                        });

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
                    lastUpdateCases.setText(Utils.getDate(lastUpdate));

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
                    total_recoveredUsa.setText(formatter.format(Double.parseDouble(recovered)));
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
                    total_recoveredIndia.setText(formatter.format(Double.parseDouble(recovered)));
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
                    total_recoveredBrazil.setText(formatter.format(Double.parseDouble(recovered)));
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
