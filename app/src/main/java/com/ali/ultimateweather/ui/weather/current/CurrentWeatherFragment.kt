package com.ali.ultimateweather.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ali.ultimateweather.R
import com.ali.ultimateweather.internal.UnitSystem
import com.ali.ultimateweather.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    /*Why we created viewModelFactory
    * viewModel are only to preserve state (screen rotation, configuration changes
    * and because it is not destroyed, Also views pass through a life cycle and we
    * cannot create a new instance of viewModel from inside any view instead,we need
    * to initiate viewModel when the view is first launched only, then on subsequent
    * launching after destroy then re-create we need to get the already initiated
    * viewModel, so that would happen through a viewModelFactory that save the instance of viewModel for us
    * */
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()

    }

    /*we could directly call launch (without GlobalScope.launch) because our fragment is an instance
    of ScopedFragment which is instance of CoroutineScope
    */

    private fun bindUI() = launch {
            val currentWeather = viewModel.weather.await()
            currentWeather.observe(this@CurrentWeatherFragment, Observer {
                if (it == null) return@Observer // first launch "it" comes from db and we don't have it so we add the check and the return
                group_loading.visibility = View.GONE
                updateLocation("Beirut")
                updateDateToToday()
                updateTemperatures(it.temperature, it.feelslike)
                updateCondition(it.weatherDescriptions.get(0))
                updatePrecipitation(it.precip)
                updateWind(it.windDir, it.windSpeed)
                updateVisibility(it.visibility)
            })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.unit.equals(UnitSystem.METRIC.name)) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        tv_temperature.text = "$temperature$unitAbbreviation"
        tv_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        tv_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        tv_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        //TODO to be changed based on selected unit
        val unitAbbreviation = "kph"
        tv_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        //TODO to be changed based on selected unit
        val unitAbbreviation = "km"
        tv_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }
 }
