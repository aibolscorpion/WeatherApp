package kz.shymkent.weatherapp;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.List;

import kz.shymkent.weatherapp.model.Weathers;

public class DiffUtilCallBack extends DiffUtil.Callback {
    private final List<Weathers> oldList;
    private final List<Weathers> newList;
    public DiffUtilCallBack(List<Weathers> oldList, List<Weathers> newList){
        this.oldList = oldList;
        this.newList = newList;
    }
    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        Weathers oldCity = oldList.get(i);
        Weathers newCity = newList.get(i1);
        if(oldCity.getName().equals(newCity.getName())) {
            {
            } ;
        }else{

        }
        return oldCity.getName() == newCity.getName();

    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        Weathers oldCity = oldList.get(i);
        Weathers newCity = newList.get(i1);
        if(oldCity.getMain().getTemp().equals(newCity.getMain().getTemp())) {
            {
            } ;
        }else{

        }
        return oldCity.getMain().getTemp().equals(newCity.getMain().getTemp());
    }
}
