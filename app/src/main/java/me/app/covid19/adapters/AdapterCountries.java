package me.app.covid19.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.app.covid19.R;
import me.app.covid19.activities.Countries;
import me.app.covid19.activities.DetailActivity;
import me.app.covid19.models.Country;

public class AdapterCountries extends RecyclerView.Adapter<AdapterCountries.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Country> countryList;
    Context context;
    ArrayList<Country> countryArrayList;

    public AdapterCountries(Context context, List<Country> countryList){
        this.layoutInflater = LayoutInflater.from(context);
        this.countryList = countryList;
        this.context = context;
        this.countryArrayList = new ArrayList<Country>();
        this.countryArrayList.addAll(countryList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.country_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //animation item
        //holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));

        DecimalFormat formatter = new DecimalFormat("###,###,##0");
        holder.country_name.setText(countryList.get(position).getCountry());
        //holder.country_total_cases.setText(formatter.format(Double.parseDouble(countryListFiltered.get(position).getCases())));
        //holder.country_today_cases.setText("+" +formatter.format(Double.parseDouble(countryListFiltered.get(position).getTodayCases())));
        Picasso.get().load(countryList.get(position).getFlag()).into(holder.country_flag);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("position", position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView country_flag;
        TextView country_name, country_total_cases, country_today_cases;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            country_name = itemView.findViewById(R.id.country_name);
            country_flag = itemView.findViewById(R.id.country_flag);
            relativeLayout = itemView.findViewById(R.id.item_country_layout);
            country_total_cases = itemView.findViewById(R.id.country_total_cases);
            country_today_cases = itemView.findViewById(R.id.country_today_cases);
        }
    }

    public void Filter(String countryName){
        countryName = countryName.toLowerCase(Locale.getDefault());
        countryList.clear();
        if (countryName.length() == 0) {
            countryList.addAll(countryArrayList);
        }else {
            for (Country cl : countryArrayList){
                if (cl.getCountry().toLowerCase(Locale.getDefault()).contains(countryName)){
                    countryList.add(cl);
                }
            }
        }
        notifyDataSetChanged();
    }
}
