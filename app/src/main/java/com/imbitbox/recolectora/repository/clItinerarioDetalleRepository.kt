package com.imbitbox.recolectora.repository

import com.google.gson.Gson
import com.imbitbox.recolectora.Interfaces.iItinerarioDetalle
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleApi
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleInsert
import com.imbitbox.recolectora.models.clItinerario.clItinerarioEstatusInsert
import com.imbitbox.recolectora.models.tools.clQuery
import javax.inject.Inject

class clItinerarioDetalleRepository @Inject constructor(private val iitinerarioDetalleApi: iItinerarioDetalle) {
    suspend fun getItinerarioDetalle(objItinerarioDetalle: clItinerarioDetalleInsert): clItinerarioDetalleApi? {
        try {
        val jItinerarioDetalle = Gson().toJson(objItinerarioDetalle)
        val response = iitinerarioDetalleApi.getItinerarioDetalle(clQuery("sp_ItinerarioDetalle_Generar '$jItinerarioDetalle'"))
        if(response.isSuccessful){
            return response.body()
        }
        return null
        }
        catch (error: Exception){
            return null
        }
    }

    suspend fun setItinerarioEstatus(objItinerario: clItinerarioEstatusInsert) {
        try {
            val jItinerario = Gson().toJson(objItinerario)
            val response =
                iitinerarioDetalleApi.getItinerarioDetalle(clQuery("sp_ItinerarioDetalle_Modificar '$jItinerario'"))
        }
        catch (error: Exception){
            println("Ocurrio un error en la consulta")
        }

    }
}