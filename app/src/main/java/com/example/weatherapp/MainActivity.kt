package com.example.weatherapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.util.*

class MainActivity : AppCompatActivity() {

    public var zipCode = "0"
    lateinit var apiCall: String


    // variables to be used for changing location
    lateinit var location: TextView
    lateinit var updateTimeStamp: TextView
    lateinit var weatherDes: TextView
    lateinit var temp: TextView
    lateinit var lowTemp: TextView
    lateinit var maxTemp: TextView
    lateinit var sunrise: TextView
    lateinit var sunset: TextView
    lateinit var windVel: TextView
    lateinit var pressureTV: TextView
    lateinit var humidityTV: TextView
    lateinit var refreshView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        location = findViewById(R.id.locationTV)
        updateTimeStamp = findViewById(R.id.updateTimeStamp)
        weatherDes = findViewById(R.id.weatherCondition)
        temp = findViewById(R.id.temp)
        lowTemp = findViewById(R.id.lowTemp)
        maxTemp = findViewById(R.id.highTemp)
        sunrise = findViewById(R.id.sunRiseTimeTV)
        sunset = findViewById(R.id.sunsetTimeTV)
        windVel = findViewById(R.id.windSpeed)
        pressureTV = findViewById(R.id.pressureValueTV)
        humidityTV = findViewById(R.id.humidityValueTV)
        refreshView = findViewById(R.id.refreshImageView)


        //OnClick listener to change the location
        location.setOnClickListener { getZipCode() }
        zipCode = intent.getStringExtra("zipCode").toString()


        //Response API data
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        apiInterface?.getWeatherData()?.enqueue(object : Callback<WeatherData> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {

                val city = response.body()!!.name
                Log.d("weatherInfo", city)

                val country = response.body()!!.sys.country
                Log.d("weatherInfo", country)

                val date = response.body()!!.dt
                Log.d("weatherInfo", date.toString())

                var weatherDescription = response.body()!!.weather[0].description
                Log.d("weatherInfo", weatherDescription)

                val currentTemp = response.body()!!.main.temp
                Log.d("weatherInfo", currentTemp.toString())

                val lowestTemp = response.body()!!.main.temp_min
                Log.d("weatherInfo", lowestTemp.toString())

                val highestTemp = response.body()!!.main.temp_max
                Log.d("weatherInfo", highestTemp.toString())

                val pressure = response.body()!!.main.pressure
                Log.d("weatherInfo", pressure.toString())

                val humidity = response.body()!!.main.humidity
                Log.d("weatherInfo", humidity.toString())

                val windSpeed = response.body()!!.wind.speed
                Log.d("weatherInfo", windSpeed.toString())

                val sunriseTime = response.body()!!.sys.sunrise
                Log.d("weatherInfo", sunriseTime.toString())

                val sunsetTime = response.body()!!.sys.sunset
                Log.d("weatherInfo", sunsetTime.toString())



                //print to UI here
                location.text = "$city, $country"
                updateTimeStamp.text =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
                        .format(Date(date.toLong() * 1000))
                weatherDes.text = weatherDescription

                temp.text = "${String.format("%.0f", currentTemp.toFloat())}" + "°C"
                lowTemp.text = "Low: " + "${String.format("%.1f", lowestTemp.toFloat())}" + "°C"
                maxTemp.text = "High: " + "${String.format("%.1f", highestTemp.toFloat())}" + "°C"
                sunrise.text = SimpleDateFormat(
                    "HH:mm a",
                    Locale.ENGLISH
                ).format(Date(sunriseTime.toLong() * 1000))
                sunset.text =
                    SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(Date(sunsetTime.toLong() * 1000))
                windVel.text = windSpeed.toString()
                pressureTV.text = pressure.toString()
                humidityTV.text = humidity.toString()

                temp.setOnClickListener {
                    if(temp.text.contains("℃")){
                        val fahrenheit = currentTemp.toFloat() * 1.800 + 32
                        val tempMaxF = highestTemp.toFloat() * 1.800 + 32
                        val tempMinF = lowestTemp.toFloat() * 1.800 + 32

                        val solution = String.format("%.0f", fahrenheit)
                        val solutiontempMax = String.format("%.1f", tempMaxF)
                        val solutiontempMin = String.format("%.1f", tempMinF)

                        temp.text= solution+" F"
                        maxTemp.text="High: "+solutiontempMax+" F"
                        lowTemp.text="Low: "+solutiontempMin+" F"
                    }else{
                        val celsius = currentTemp.toFloat()
                        val tempMax = highestTemp.toFloat()
                        val tempMin = lowestTemp.toFloat()


                        val solution = String.format("%.0f", celsius)
                        val solutiontempMax = String.format("%.1f", tempMax)
                        val solutiontempMin = String.format("%.1f", tempMin)
                        temp.text= solution+"℃"
                        maxTemp.text="High: "+solutiontempMax+"℃"
                        lowTemp.text="Low: "+solutiontempMin+"℃"
                    }

                }




            } //onResponse function

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("retrofit", "onFailure: ${t.message.toString()}")
            }

        })



    }

    // Get zipCode from another page
    private fun getZipCode() {
        val intent = Intent(this, GetZipCode::class.java)
        startActivity(intent)
    }
}