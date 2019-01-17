package kz.shymkent.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import kz.shymkent.weatherapp.model.Weather;
import kz.shymkent.weatherapp.model.Weathers;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Weathers> weathers;

    public MyAdapter(List<Weathers> weathers){
        this.weathers = weathers;
    }
    public MyAdapter(){
        weathers = new ArrayList<Weathers>();
    }
    public void insertData(List<Weathers> insertList){
        final DiffUtilCallBack diffUtilCallBack = new DiffUtilCallBack(weathers,insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallBack);

        weathers.addAll(insertList);
        List<Weathers> newLists = removeDuplicates(weathers);
        weathers.clear();
        weathers =  newLists;
         diffResult.dispatchUpdatesTo(this);

    }
    List<Weathers> removeDuplicates(List<Weathers> list) {

        List<Weathers> newList = new ArrayList<Weathers>();
        Set<String> name_of_cities = new HashSet<String>();


        for( Weathers item : list ) {
            if( name_of_cities.add(
                item.getName())) {
                newList.add( item );
            }
        }
        return newList;
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

        myViewHolder.city.setText(weathers.get(i).getName());
        myViewHolder.temperature.setText(weathers.get(i).getMain().getTemp() + "°");
    }

    @Override
    public int getItemCount() {
        return weathers.size();
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
