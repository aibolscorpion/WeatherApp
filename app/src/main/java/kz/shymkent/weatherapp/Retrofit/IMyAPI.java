package kz.shymkent.weatherapp.Retrofit;



import java.util.List;

import io.reactivex.Observable;
import kz.shymkent.weatherapp.model.Weathers;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IMyAPI {
    @GET("weather?")
    Observable<Weathers> getWeather(@Query("q") String city,@Query("units") String metric, @Query("APPID") String yourkey);
}
