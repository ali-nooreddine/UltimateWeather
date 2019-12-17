package com.ali.ultimateweather.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback

/**
 * Created by Ali Noureddine on 12/17/2019.
 */
class LifeCycleBoundLocationManager(
    lifecycleOwner: LifecycleOwner,
    fusedLocationProviderClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
):LifecycleObserver {
    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback)
    }

}