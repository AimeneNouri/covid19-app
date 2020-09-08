package me.app.covid19.adapters;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.app.covid19.R;
import me.app.covid19.activities.Countries;
import me.app.covid19.activities.DetailActivity;
import me.app.covid19.models.Country;

public class AdapterCountries extends RecyclerView.Adapter<AdapterCountries.ViewHolder> implements Filterable {

    LayoutInflater layoutInflater;
    List<Country> countryList;
    List<Country> countryListFiltered;
    Context context;

    public AdapterCountries(Context context, List<Country> countryList){
        this.layoutInflater = LayoutInflater.from(context);
        this.countryList = countryList;
        this.countryListFiltered = countryList;
        this.context = context;
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
        holder.country_name.setText(countryListFiltered.get(position).getCountry());
        holder.country_total_cases.setText(formatter.format(Double.parseDouble(countryListFiltered.get(position).getCases())));
        holder.country_today_cases.setText("+" +formatter.format(Double.parseDouble(countryListFiltered.get(position).getTodayCases())));
        Picasso.get().load(countryListFiltered.get(position).getFlag()).into(holder.country_flag);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("position", position);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryListFiltered.size();
    }



    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0){
                    filterResults.count = countryList.size();
                    filterResults.values = countryList;
                }else {
                    List<Country> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (Country itemsModel:countryList){
                        if (itemsModel.getCountry().toLowerCase().contains(searchStr)) {
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryListFiltered = (List<Country>) results.values;
                Countries.countryList = (List<Country>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
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
}
