package me.app.covid19.fragments.newsTablayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.app.covid19.R;
import me.app.covid19.adapters.AdapterNews;
import me.app.covid19.models.Constants;
import me.app.covid19.models.News;

public class World extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private AdapterNews adapterNews;
    public static List<News> newsList = new ArrayList<>();
    News news;

    private SwipeRefreshLayout swipeRefreshLayout;

    public World (){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View worldView = inflater.inflate(R.layout.world, container, false);

        recyclerView = worldView.findViewById(R.id.recycler_news);
        progressBar = worldView.findViewById(R.id.progressBar);
        swipeRefreshLayout = worldView.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        fetchData();

        return worldView;
    }

    private void fetchData() {
        String url = "http://newsapi.org/v2/everything?domains=who.int,news-medical.net,medicalnewstoday.com,medscape.com,health.com,medicalxpress.com,medscape.com,medicalnewstoday.com&language=en&apiKey=" + Constants.API_KEY;
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
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onRefresh() {
        onLoadingSwipeRefresh();
    }

    private void onLoadingSwipeRefresh(){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://newsapi.org/v2/everything?domains=who.int,news-medical.net,medicalnewstoday.com,medscape.com,health.com,medicalxpress.com,medscape.com,medicalnewstoday.com&language=en&apiKey=" + Constants.API_KEY;
                        swipeRefreshLayout.setRefreshing(true);

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

                                swipeRefreshLayout.setRefreshing(false);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adapterNews = new AdapterNews(newsList, getContext());
                                recyclerView.setAdapter(adapterNews);
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        swipeRefreshLayout.setRefreshing(false);
                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);
                    }
                }
        );
    }
}
