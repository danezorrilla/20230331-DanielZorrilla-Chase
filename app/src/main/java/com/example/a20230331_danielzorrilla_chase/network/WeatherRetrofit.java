package com.example.a20230331_danielzorrilla_chase.network;

import com.example.a20230331_danielzorrilla_chase.model.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRetrofit {

    private WeatherAPI weatherAPI;

    public WeatherRetrofit(){
        weatherAPI = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherAPI.class);
    }

    public Observable<WeatherResponse> getLocationWeather(String name){
        return weatherAPI.getLocationWeather(name, "c972c1af262dc359cfe47b5885a07209");
    }

}
