package com.example.a20230331_danielzorrilla_chase.network;

import com.example.a20230331_danielzorrilla_chase.model.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("data/2.5/weather")
    Observable<WeatherResponse> getLocationWeather(
            @Query("q") String query, @Query("appid") String apiKey);
}
