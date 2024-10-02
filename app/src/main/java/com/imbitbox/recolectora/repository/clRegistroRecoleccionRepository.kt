package com.imbitbox.recolectora.repository

import com.google.gson.Gson
import com.imbitbox.recolectora.Interfaces.iRegistroRecoleccion
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clItinerarioTerminado
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionDetalleApi
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionSaveAll
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionServiciosInsert
import com.imbitbox.recolectora.models.tools.clQuery
import com.imbitbox.recolectora.viewModel.clRegistroRecoleccionViewModel
import javax.inject.Inject

class clRegistroRecoleccionRepository @Inject constructor(private val iRegistroRecoleccion: iRegistroRecoleccion) {
    suspend fun getServicios(objServicios: clRegistroRecoleccionServiciosInsert): clRegistroRecoleccionDetalleApi? {
        try {
            val jItinerario = Gson().toJson(objServicios)
            val response = iRegistroRecoleccion.getItinerarioServicios(clQuery("sp_RutaTipoServicio_Consultar'$jItinerario'"))

            if (response.isSuccessful) {
                return response.body()
            }
            return null
        }
        catch (error: Exception){
            return null
        }
    }

    suspend fun saveItinerarioDetalle(objItinerario: clRegistroRecoleccionSaveAll): clItinerarioTerminado? {
        try {
            val jItinerario = Gson().toJson(objItinerario)
            val response =
                iRegistroRecoleccion.setItinerarioTerminado(clQuery("sp_ItinerarioDetalle_Terminar'$jItinerario'"))

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