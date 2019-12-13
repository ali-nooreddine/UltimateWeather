package com.ali.ultimateweather.data.network.response

import com.ali.ultimateweather.data.db.entity.CurrentWeatherEntry
import com.ali.ultimateweather.data.db.entity.WeatherLocation
import com.ali.ultimateweather.data.db.entity.Request
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    val request: Request,
    val location: WeatherLocation,
    @SerializedName(value = "current")
    val currentWeatherEntry: CurrentWeatherEntry
)