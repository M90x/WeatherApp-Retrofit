package com.example.weatherapp

import retrofit2.http.GET

interface APIInterface {

    @GET("weather?zip=94040,us&appid=a58df9fbb276fef7ea66f6ea2d700783&units=metric")

    fun getWeatherData():retrofit2.Call<WeatherData>
}