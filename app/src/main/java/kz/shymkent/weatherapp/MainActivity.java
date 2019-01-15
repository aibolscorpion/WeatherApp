package kz.shymkent.weatherapp;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    AutoCompleteTextView autoCompleteTextView;
    RecyclerView.LayoutManager mLayoutManager;
    String OPEN_WEATHER_MAP_API = "19e4ed1e456d488284353c7e019a28a2";
    PlaceAutocompleteAdapter placeAutocompleteAdapter;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<City> cities;
    GoogleApiClient mGoogleApiClient;
    GeoDataClient mGeoDataClient;
    City city;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).setCountry("KZ").build();
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, new LatLngBounds(new LatLng(0, 0), new LatLng(0, 0)), autocompleteFilter);

        cities = new ArrayList<City>();
        autoCompleteTextView = findViewById(R.id.search_cities_text_view);
        autoCompleteTextView.setAdapter(placeAutocompleteAdapter);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {

                    taskLoadUp(autoCompleteTextView.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            Log.i("aibolscorpion", xml);

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject main = json.getJSONObject("main");
                    if (json.getJSONObject("sys").getString("country").equals("KZ")) {

                        city = new City();
                        city.setCity(json.getString("name").toUpperCase(Locale.US));
                        city.setTemperature(String.format("%.2f", main.getDouble("temp")) + "°");
                        cities.add(city);
                        //  currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°");


                        myAdapter = new MyAdapter(removeDuplicates(cities));
                        recyclerView.setAdapter(myAdapter);
                    }
                }
            } catch (JSONException e) {

            }


        }
        ArrayList<City> removeDuplicates(ArrayList<City> list) {

            ArrayList<City> newList = new ArrayList<City>();
            Set<String> name_of_cities = new HashSet<String>();


            for( City item : list ) {
                if( name_of_cities.add( item.getCity())) {
                    newList.add( item );
                }
            }
            return newList;
        }
    }
}
