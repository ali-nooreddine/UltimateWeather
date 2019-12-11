package com.ali.ultimateweather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ali.ultimateweather.data.network.response.CurrentWeatherResponse
import com.ali.ultimateweather.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherStackApiService: WeatherStackApiService
) : WeatherNetworkDataSource {


    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    //live data cannot be changed so we have to create a mutable live data
    // client code can only access downloadedCurrentWeather and cannot change it
    // this class is the only place where we can mess with downloadedCurrentWeather (basic principal of encapsulation)
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, units:String) {
        try{
            val fetchedCurrentWeather = weatherStackApiService
                .getCurrentWeather(location,units)
                .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }catch (e:NoConnectivityException){
            Log.e("Connectivity","No Internet Connection.", e)
        }
    }
}