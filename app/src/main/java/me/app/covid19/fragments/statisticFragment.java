package me.app.covid19.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import me.app.covid19.R;

public class statisticFragment extends Fragment {

    private View StatisticView;

    private ImageView country_flag1, country_flag2, country_flag3;

    private TextView total_casesUsa, total_casesINDIA, total_casesBRAZIL;
    private TextView countryName1, countryName2, countryName3;

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

        //fetching data for USA
        fetchDataUSA();

        //fetching data for USA
        fetchDataINDIA();

        //fetching data for USA
        fetchDataBRAZIL();


        return StatisticView;
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
