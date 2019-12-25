package com.ali.ultimateweather.data.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.ali.ultimateweather.data.db.entity.WeatherLocation
import com.ali.ultimateweather.internal.GpsNotEnabledException
import com.ali.ultimateweather.internal.LocationPermissionNotGrantedException
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {

    val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {

        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)

    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val customLocationName = getCustomLocationName()
        return customLocationName != lastWeatherLocation.name
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await() ?: return false

        // can't compare double with ==
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat.toDouble()) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon.toDouble()) > comparisonThreshold
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException) {

                return "${getCustomLocationName()}"

            } catch (e: GpsNotEnabledException) {

                return "${getCustomLocationName()}"

            }
        } else
            return "${getCustomLocationName()}"
    }

    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission()) {
            if (gpsLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.asDeferred()
            } else {
                throw GpsNotEnabledException()
            }
        } else {
            throw LocationPermissionNotGrantedException()
        }
    }

    private fun gpsLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}