package com.ali.ultimateweather.data.network

import androidx.lifecycle.LiveData
import com.ali.ultimateweather.data.network.response.CurrentWeatherResponse

/**
 * Created by Ali Noureddine on 12/11/2019.
 */
interface WeatherNetworkDataSource {
    val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        units:String
    )

}