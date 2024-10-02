package com.imbitbox.recolectora.repository

import com.google.gson.Gson
import com.imbitbox.recolectora.Interfaces.IItinerario
import com.imbitbox.recolectora.models.clItinerario.clItinerario
import com.imbitbox.recolectora.models.clItinerario.clItinerarioApi
import com.imbitbox.recolectora.models.tools.clQuery
import javax.inject.Inject

class clItinerarioRepository @Inject constructor(private val iitinerarioApi: IItinerario) {
    suspend fun getItinerarioEmpleado(objItinerario: clItinerario): clItinerarioApi? {
        try {
        val jItinerario = Gson().toJson(objItinerario)
        val response = iitinerarioApi.getItinerarioEmpleado(clQuery("sp_Itinerario_Consultar   '$jItinerario'"))

        if(response.isSuccessful){
            return response.body()
        }
        return null
        }
        catch (error: Exception){
            return null
        }
    }
}