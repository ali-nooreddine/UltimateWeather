package com.ali.ultimateweather.data.provider

import com.ali.ultimateweather.data.db.entity.WeatherLocation

/**
 * Created by Ali Noureddine on 12/16/2019.
 */
interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}