package com.example.a20230331_danielzorrilla_chase.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a20230331_danielzorrilla_chase.R;
import com.example.a20230331_danielzorrilla_chase.model.WeatherResponse;
import com.example.a20230331_danielzorrilla_chase.viewmodel.WeatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView cityName;
    EditText searchCityName;
    Button searchBtn;

    TextView cityTemp;
    TextView cityForecast;
    TextView cityMinMax;

    private WeatherViewModel weatherViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        cityName = findViewById(R.id.city_name);
        searchCityName = findViewById(R.id.search_city_name);
        searchBtn = findViewById(R.id.search_button);
        cityTemp = findViewById(R.id.city_temp);
        cityForecast = findViewById(R.id.city_forecast);
        cityMinMax = findViewById(R.id.city_min_max);

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        getDeviceLocation();

        searchBtn.setOnClickListener(v -> {
            city = searchCityName.getText().toString();
            if(city.isEmpty()){
                Toast.makeText(
                        MainActivity.this,
                        "Please Enter Location",
                        Toast.LENGTH_SHORT).show();
            } else {
                searchCityWeather(city);
            }

        });

    }

    private void getDeviceLocation(){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null){
                            Geocoder geocoder = new Geocoder(
                                    MainActivity.this, Locale.getDefault());
                            List<Address> addressList;
                            try {
                                addressList = geocoder.getFromLocation(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        1
                                );
                                city = addressList.get(0).getLocality();
                                searchCityWeather(city);
                            } catch (IOException e){
                                throw new RuntimeException(e);
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getDeviceLocation();
            }
        }
    }

    private void searchCityWeather(String cityName){
        compositeDisposable.add(weatherViewModel.getLocationWeatherRx(cityName).subscribe(
                this::displayWeatherResponse
        ));
    }

    private void displayWeatherResponse(WeatherResponse weatherResponse){
        cityName.setText(weatherResponse.getName());
        cityTemp.setText(convertKelvinToFahrenheit(weatherResponse.getMain().getTemp()));
        cityForecast.setText(weatherResponse.getWeather().get(0).getDescription());
        cityMinMax.setText(
                String.format("%s / %s Feels like %s",
                        convertKelvinToFahrenheit(weatherResponse.getMain().getTempMax()),
                        convertKelvinToFahrenheit(weatherResponse.getMain().getTempMin()),
                        convertKelvinToFahrenheit(weatherResponse.getMain().getFeelsLike())));

    }

    private String convertKelvinToFahrenheit(double num){
        double d = num - 273.15;
        int value = (int)Math.round(d);
        int Fahrenheit = value * 9/5 + 32;
        return Fahrenheit + " F";
    }

}