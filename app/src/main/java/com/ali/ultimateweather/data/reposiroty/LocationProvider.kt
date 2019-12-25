package com.ali.ultimateweather.data.reposiroty

import com.ali.ultimateweather.data.db.entity.WeatherLocation

/**
 * Created by Ali Noureddine on 2019-12-15.
 */
interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean

    suspend fun getPreferredLocationString(): String

}