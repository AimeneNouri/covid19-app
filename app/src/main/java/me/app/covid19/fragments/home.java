package me.app.covid19.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import me.app.covid19.R;
import me.app.covid19.activities.Countries;
import me.app.covid19.activities.Settings;
import me.app.covid19.activities.User_country;

/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    private ImageView searchButton, UserCountryCases;
    private CircleImageView settingsButton;
    private View HomeView;

    private TextView lastUpdate;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private RelativeLayout relativeLayout;

    private TextView AllCases, TotalDeaths, TotalRecovered, ActiveCases, TodayDeaths, TodayCases, TodayRecovered;

    public home() {/* Required empty public constructor*/}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeView = inflater.inflate(R.layout.homefragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        AllCases = HomeView.findViewById(R.id.allcases);
        TotalDeaths = HomeView.findViewById(R.id.DeathCases);
        TotalRecovered = HomeView.findViewById(R.id.CaseClosed);
        ActiveCases = HomeView.findViewById(R.id.ActiveCase);
        TodayDeaths = HomeView.findViewById(R.id.TodayDeaths);
        TodayRecovered = HomeView.findViewById(R.id.TodayRecovered);
        TodayCases = HomeView.findViewById(R.id.TodayCases);
        settingsButton = HomeView.findViewById(R.id.settingsButton);
        searchButton = HomeView.findViewById(R.id.searchButton);
        UserCountryCases = HomeView.findViewById(R.id.UserCountryCases);
        lastUpdate = HomeView.findViewById(R.id.lastUpdateCases);
        relativeLayout = HomeView.findViewById(R.id.relativeLayout1);

        retrieveUserImage();

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getContext(), Settings.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_out_left).toBundle();
                startActivity(infoIntent, bundle);

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getContext(), Settings.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_out_left).toBundle();
                startActivity(infoIntent, bundle);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CountriesIntent = new Intent(getContext(), Countries.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                startActivity(CountriesIntent, bundle);
            }
        });

        UserCountryCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserCountryIntent = new Intent(getContext(), User_country.class);
                startActivity(UserCountryIntent);
            }
        });

        //fetchData
        fetchData();

        //fetchDataLastUpdate();

        return HomeView;
    }




    private void retrieveUserImage() {
        RootRef.child("Users")
                .child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && (snapshot.hasChild("UserPicture"))){
                    String retrieveUserImage = snapshot.child("UserPicture").getValue().toString();

                    Picasso.get().load(retrieveUserImage).placeholder(R.drawable.person).into(settingsButton);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*private void fetchDataLastUpdate() {
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
    }*/

    private String getDate(long milliSecond){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }

    private void fetchData() {
        String url = "https://disease.sh/v3/covid-19/all/";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    DecimalFormat formatter = new DecimalFormat("###,###,##0");
                    String cases = jsonObject.getString("cases");
                    String deaths = jsonObject.getString("deaths");
                    String recovered = jsonObject.getString("recovered");
                    String active = jsonObject.getString("active");
                    String todayCase = jsonObject.getString("todayCases");
                    String todayDeath = jsonObject.getString("todayDeaths");
                    String todayRecovered = jsonObject.getString("todayRecovered");

                    AllCases.setText(formatter.format(Double.parseDouble(cases)));
                    TotalDeaths.setText(formatter.format(Double.parseDouble(deaths)));
                    TotalRecovered.setText(formatter.format(Double.parseDouble(recovered)));
                    ActiveCases.setText(formatter.format(Double.parseDouble(active)));
                    TodayCases.setText(formatter.format(Double.parseDouble(todayCase)));
                    TodayDeaths.setText(formatter.format(Double.parseDouble(todayDeath)));
                    TodayRecovered.setText(formatter.format(Double.parseDouble(todayRecovered)));
                    lastUpdate.setText(getDate(jsonObject.getLong("updated")));


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
