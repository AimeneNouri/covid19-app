package me.app.covid19.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import me.app.covid19.R;
import me.app.covid19.models.Country;

public class AdapterStatistics extends RecyclerView.Adapter<AdapterStatistics.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Country> countryList;
    Context context;

    public AdapterStatistics(Context context, List<Country> countryList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.countryList = countryList;
        this.context = context;
    }


    @NonNull
    @Override
    public AdapterStatistics.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.statistic_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0");

        String countryName = countryList.get(position).getCountry();

        if (countryName.length() > 26) {
            countryName = countryName.substring(0, 26) + "...";
        }

        holder.country_name.setText(countryName);
        holder.country_total_cases.setText(formatter.format(Double.parseDouble(countryList.get(position).getCases())));
        holder.country_today_cases.setText("+" + formatter.format(Double.parseDouble(countryList.get(position).getTodayCases())));

        Picasso.get().load(countryList.get(position).getFlag()).into(holder.country_flag);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView country_flag;
        TextView country_name, country_total_cases, country_today_cases;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            country_name = itemView.findViewById(R.id.country_name);
            country_flag = itemView.findViewById(R.id.country_flag);
            country_total_cases = itemView.findViewById(R.id.total_cases);
            country_today_cases = itemView.findViewById(R.id.TodayCases);
        }
    }
}
