package me.app.covid19.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.app.covid19.R;
import me.app.covid19.models.Country;

public class AdapterCountries extends RecyclerView.Adapter<AdapterCountries.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Country> countryList;
    Context context;

    public AdapterCountries(Context context, List<Country> countryList){
        this.layoutInflater = LayoutInflater.from(context);
        this.countryList = countryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.country_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //animation item
        //holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));

        holder.country_name.setText(countryList.get(position).getCountry());
        holder.country_total_cases.setText(countryList.get(position).getCases());
        holder.country_today_cases.setText("+" + countryList.get(position).getTodayCases());
        Picasso.get().load(countryList.get(position).getFlag()).into(holder.country_flag);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
