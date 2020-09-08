package me.app.covid19.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.app.covid19.R;
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
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {

        holder.time.setText(Utils.DateToTimeFormat(newsList.get(position).getPublishDate()));
        holder.news_title.setText(newsList.get(position).getTitle());
        //holder.news_description.setText(newsList.get(position).getDescription());
        holder.publishedAt.setText(Utils.DateFormat(newsList.get(position).getPublishDate()));
        Glide.with(context)
                .load(newsList.get(position).getImage())
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

        holder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView news_picture;
        TextView news_title, news_description, publishedAt, time;
        ProgressBar progressBar;
        Button read_more;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            news_picture = itemView.findViewById(R.id.news_picture);
            news_title = itemView.findViewById(R.id.news_title);
            news_description = itemView.findViewById(R.id.news_description);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            read_more = itemView.findViewById(R.id.read_more);
            progressBar = itemView.findViewById(R.id.progressBar);
            time = itemView.findViewById(R.id.time);
        }
    }
}
