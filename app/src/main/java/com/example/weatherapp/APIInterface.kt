package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET("weather")

    fun getWeatherData(@Query("zip") zipCode:String,
                       @Query("appid") apiKey:String,
                       @Query("units") units:String ):retrofit2.Call<WeatherData>

}