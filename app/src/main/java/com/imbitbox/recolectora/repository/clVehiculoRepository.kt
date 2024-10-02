package com.imbitbox.recolectora.repository

import com.google.gson.Gson
import com.imbitbox.recolectora.Interfaces.iVehiculo
import com.imbitbox.recolectora.models.clVehiculo.clVehiculo
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoApi
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKm
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKmApi
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKmInsert
import com.imbitbox.recolectora.models.tools.clQuery
import java.time.LocalDate
import javax.inject.Inject

class clVehiculoRepository @Inject constructor(private val vehiculoApi: iVehiculo) {
    suspend fun getVehiculo(objVehiculo: clVehiculo): clVehiculoApi? {
        try {
            val jVehiculo = Gson().toJson(objVehiculo)
            val response = vehiculoApi.getVehiculo(clQuery("sp_Vehiculo_Consultar  '$jVehiculo'"))

            if (response.isSuccessful) {
                return response.body()
            }
            return null
        }
        catch (error: Exception){
            return null
        }
    }

     fun setVehiculo(sVehiculo: String): clVehiculoApi? {
        if (sVehiculo != "") {
            val objVehiculo = Gson().fromJson(sVehiculo, clVehiculo::class.java)
            val response = clVehiculoApi(true, 1, LocalDate.now().toString().substring(0,10), "", "", "0", listOf(objVehiculo))


            return response

        }
        return null
    }

    suspend fun setVehiculoKm(objVehiculo: clVehiculoKmInsert): clVehiculoKmApi? {
        try{
            val jVehiculo = Gson().toJson(objVehiculo)
            val response = vehiculoApi.setVehiculoKm(clQuery("sp_VehiculoKm_Insertar  '$jVehiculo'"))

            if (response.isSuccessful) {
                return response.body()
            }
            return null
        }
        catch (error: Exception){
            return null
        }
    }

    suspend fun getVehiculoKm(objVehiculo: clVehiculoKm): clVehiculoKmApi? {
        try {
            val jVehiculo = Gson().toJson(objVehiculo)
            val response =
                vehiculoApi.setVehiculoKm(clQuery("sp_VehiculoKm_Consultar  '$jVehiculo'"))

            if (response.isSuccessful) {
                return response.body()
            }
            return null
        }
        catch (error: Exception){
            return null
        }
    }
}