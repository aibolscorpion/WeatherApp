package kz.shymkent.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<City> cities;

    public MyAdapter(ArrayList<City> cities){
        this.cities = cities;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_view, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.city.setText(cities.get(i).getCity());
        myViewHolder.temperature.setText(cities.get(i).getTemperature());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView city;
        public TextView temperature;
        public MyViewHolder(View view) {
            super(view);
            city = view.findViewById(R.id.city);
            temperature = view.findViewById(R.id.temperature);
        }
    }

}
