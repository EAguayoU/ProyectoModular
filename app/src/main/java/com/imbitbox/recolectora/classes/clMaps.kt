package com.imbitbox.recolectora.classes
import android.content.Context
import android.content.Intent
import android.net.Uri
class clMaps
{
    companion object
    {
        fun openWazeNavigationToB(context: Context,
                                  latitude : Double,
                                  longitude : Double)
        {
            val wazeUrl = "https://waze.com/ul?ll=$latitude%2C$longitude&amp;navigate=yes"
            val uri = Uri.parse(wazeUrl)

            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }

        fun openGoogleMapsNavigationToB(context: Context,
                                        latitude : Double,
                                        longitude : Double)
        {
            val googleMapsUrl = "google.navigation:q=$latitude,$longitude"
            val uri = Uri.parse(googleMapsUrl)

            val googleMapsPackage = "com.google.android.apps.maps"
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage(googleMapsPackage)
            }

            context.startActivity(intent)
        }

    }
}