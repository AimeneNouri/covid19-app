package me.app.covid19.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import me.app.covid19.R;
import me.app.covid19.activities.News_Details_activity;
import me.app.covid19.models.News;
import me.app.covid19.models.Utils;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.NewsViewHolder>{

    LayoutInflater layoutInflater;
    List<News> newsList;
    Context context;

    public AdapterNews(List<News> newsList, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {

        final News newsItem = newsList.get(position);

        holder.time.setText(Utils.DateToTimeFormat(newsItem.getPublishDate()));
        holder.news_title.setText(newsItem.getTitle());
        holder.news_source.setText(newsItem.getSourceName());
        //holder.news_description.setText(newsList.get(position).getDescription());
        holder.publishedAt.setText(Utils.DateFormat(newsItem.getPublishDate()));
        Glide.with(context)
                .load(newsItem.getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.news_picture);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), News_Details_activity.class);

                News newsItem = newsList.get(position);
                intent.putExtra("url", newsItem.getUrl());
                intent.putExtra("title", newsItem.getTitle());
                intent.putExtra("img", newsItem.getImage());
                intent.putExtra("date", newsItem.getPublishDate());
                intent.putExtra("source", newsItem.getSourceName());
                intent.putExtra("author", newsItem.getAuthor());

                Pair<View, String> pair = Pair.create((View) holder.news_picture, ViewCompat.getTransitionName(holder.news_picture));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context,
                        pair
                );

                holder.itemView.getContext().startActivity(intent, optionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView news_picture;
        TextView news_title, news_description, publishedAt, time, news_source;
        ProgressBar progressBar;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            news_picture = itemView.findViewById(R.id.news_picture);
            news_title = itemView.findViewById(R.id.news_title);
            news_description = itemView.findViewById(R.id.news_description);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            progressBar = itemView.findViewById(R.id.progressBar);
            time = itemView.findViewById(R.id.time);
            news_source = itemView.findViewById(R.id.news_source);
        }
    }
}
