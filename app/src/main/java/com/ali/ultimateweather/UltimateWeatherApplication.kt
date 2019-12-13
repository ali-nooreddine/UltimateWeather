package com.ali.ultimateweather

import android.app.Application
import androidx.preference.PreferenceManager
import com.ali.ultimateweather.data.db.WeatherDatabase
import com.ali.ultimateweather.data.network.*
import com.ali.ultimateweather.data.provider.UnitProvider
import com.ali.ultimateweather.data.provider.UnitProviderImpl
import com.ali.ultimateweather.data.reposiroty.UltimateWeatherRepository
import com.ali.ultimateweather.data.reposiroty.UltimateWeatherRepositoryImpl
import com.ali.ultimateweather.ui.weather.current.CurrentWeatherViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * Created by Ali Noureddine on 12/11/2019.
 */
class UltimateWeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@UltimateWeatherApplication))
        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().currentWeatherDao() }

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherStackApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<UltimateWeatherRepository>() with singleton {
            UltimateWeatherRepositoryImpl(
                instance(),
                instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

}
