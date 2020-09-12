package me.app.covid19.fragments.newsTablayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.app.covid19.R;
import me.app.covid19.activities.UserInfo;
import me.app.covid19.adapters.AdapterNews;
import me.app.covid19.models.Constants;
import me.app.covid19.models.News;
import me.app.covid19.models.Utils;

public class Country extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private static String countryCode = "";

    private AdapterNews adapterNews;
    public static List<News> newsList = new ArrayList<>();

    public Country (){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View countryView = inflater.inflate(R.layout.country, container, false);

        recyclerView = countryView.findViewById(R.id.recycler_news_country);
        progressBar = countryView.findViewById(R.id.progressBar);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CODE", Context.MODE_PRIVATE);
        countryCode = sharedPreferences.getString("countryCode", "");

        fetchData();

        return countryView;
    }

    private void fetchData() {
        String url = "http://newsapi.org/v2/top-headlines?country="+ countryCode +"&apiKey=" + Constants.API_KEY;

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);

                        News news = new News();
                        news.setAuthor(object.getString("author"));
                        news.setTitle(object.getString("title"));
                        news.setDescription(object.getString("description"));
                        news.setUrl(object.getString("url"));
                        news.setImage(object.getString("urlToImage"));
                        news.setPublishDate(object.getString("publishedAt"));
                        news.setContent(object.getString("content"));

                        JSONObject object1 = object.getJSONObject("source");
                        news.setSourceName(object1.getString("name"));

                        newsList.add(news);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.GONE);

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapterNews = new AdapterNews(newsList, getContext());
                recyclerView.setAdapter(adapterNews);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
