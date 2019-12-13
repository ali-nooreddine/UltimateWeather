package com.ali.ultimateweather.data.network

import com.ali.ultimateweather.data.network.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Ali Noureddine on 12/10/2019.
 */

const val API_KEY = "086b2f46cc554acbdcd35d2a65f26bcc"
//http://api.weatherstack.com/current?access_key=086b2f46cc554acbdcd35d2a65f26bcc&query=zawtar
interface WeatherStackApiService {

    @GET("current")
    fun getCurrentWeather(
        @Query("query") location:String,
        @Query("units") units:String="m"
    ): Deferred<CurrentWeatherResponse>

    companion object{
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): WeatherStackApiService {

            val requestInterceptor = Interceptor{chain->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key",
                        API_KEY
                    )
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherStackApiService::class.java)

        }
    }

}