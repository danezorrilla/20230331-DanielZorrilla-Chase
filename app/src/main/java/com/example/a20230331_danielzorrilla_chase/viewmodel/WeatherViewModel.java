package com.example.a20230331_danielzorrilla_chase.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.a20230331_danielzorrilla_chase.model.WeatherResponse;
import com.example.a20230331_danielzorrilla_chase.network.WeatherRetrofit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherViewModel extends ViewModel {

    private final WeatherRetrofit weatherRetrofit = new WeatherRetrofit();

    public Observable<WeatherResponse> getLocationWeatherRx(String name){
        return weatherRetrofit
                .getLocationWeather(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
