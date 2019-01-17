package kz.shymkent.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kz.shymkent.weatherapp.Retrofit.IMyAPI;
import kz.shymkent.weatherapp.Retrofit.RetrofitClient;
import kz.shymkent.weatherapp.model.Weathers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AutoCompleteTextView autoCompleteTextView;
    RecyclerView.LayoutManager mLayoutManager;
    String OPEN_WEATHER_MAP_API = "19e4ed1e456d488284353c7e019a28a2";
    PlaceAutocompleteAdapter placeAutocompleteAdapter;
    RecyclerView recyclerView;
    MyAdapter adapter;
    GoogleApiClient mGoogleApiClient;
    GeoDataClient mGeoDataClient;
    Context context;
    ArrayList<Weathers> citiesORM;
    List<Weathers> weathersList;
    CityORM cityORM ;
    ArrayList<String> requestsORM;
    LastRequestORM lastRequestORM;
    IMyAPI myAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(IMyAPI.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        cityORM = new CityORM();
        lastRequestORM = new LastRequestORM();
        context = getApplicationContext();
        mGeoDataClient = Places.getGeoDataClient(this, null);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).setCountry("KZ").build();
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, new LatLngBounds(new LatLng(0, 0), new LatLng(0, 0)), autocompleteFilter);

        autoCompleteTextView = findViewById(R.id.search_cities_text_view);
        weathersList = new ArrayList<Weathers>();
        citiesORM = cityORM.getCities(context);
        requestsORM = lastRequestORM.getRequests(context);

        if (weathersList.size() == 0) {
            if(citiesORM.size() >0) {
                weathersList.add(citiesORM.get(citiesORM.size() - 1));
            }
            if(requestsORM.size() >0){
                autoCompleteTextView.setText(requestsORM.get(requestsORM.size() - 1));
            }
        }

            autoCompleteTextView.setAdapter(placeAutocompleteAdapter);
            adapter = new MyAdapter(weathersList);
            recyclerView.setAdapter(adapter);


            autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 2) {

                        String typedText = autoCompleteTextView.getText().toString();
                        fetchData(typedText);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    protected void onStop(){
        super.onStop();
        compositeDisposable.clear();
    }

    private void fetchData(String typedText){
        compositeDisposable.add(myAPI.getWeather(typedText,"metric",OPEN_WEATHER_MAP_API).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Weathers>() {
                    @Override
                    public void accept(Weathers weathers) {
                        if(weathers.getSys().getCountry().equals("KZ")) {
                            List<Weathers> newWeathersList = new ArrayList<Weathers>();
                            newWeathersList.add(weathers);
                            displayData(newWeathersList);

                            CityORM.insertCity(context,weathers);
                        }
                    }}, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                        }
                }));
    }

    private void displayData(List<Weathers> weathers) {
        adapter.insertData(weathers);
        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
        }
