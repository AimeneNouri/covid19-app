package me.app.covid19.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterNews;
import me.app.covid19.models.Constants;
import me.app.covid19.models.News;
import me.app.covid19.models.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class newsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View newsView;

    private ProgressBar progressBar;

    private AdapterNews adapterNews;
    public static List<News> newsList = new ArrayList<>();

    News news;

    public newsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newsView = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = newsView.findViewById(R.id.recycler_news);
        progressBar = newsView.findViewById(R.id.progressBar);

        fetchData();

        return newsView;
    }

    private void fetchData() {
        String url = "http://newsapi.org/v2/everything?domains=who.int&apiKey=" + Constants.API_KEY;

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
