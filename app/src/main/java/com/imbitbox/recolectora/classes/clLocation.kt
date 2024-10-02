package com.imbitbox.recolectora.classes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class clLocation {
    private var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    //val context = LocalContext.current
    var currentLocation by mutableStateOf(datosLocation())

    //@Composable
    fun pLocation(context: Context){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (lo in p0.locations) {
                    // Update UI with location data
                    currentLocation = datosLocation(lo.latitude, lo.longitude)
                }
            }
        }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                //Actualizar localizacion cada X tiempo en la ventana
//                interval = 10000
//                fastestInterval = 5000
//                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }


}

data class datosLocation(
    var Latitude : Double = 0.00,
    var Longitude : Double = 0.00
)

