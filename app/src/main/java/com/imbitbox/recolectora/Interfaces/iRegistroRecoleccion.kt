package com.imbitbox.recolectora.Interfaces

import com.imbitbox.recolectora.helpers.clConstant
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleApi
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clItinerarioTerminado
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionDetalleApi
import com.imbitbox.recolectora.models.tools.clQuery
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface iRegistroRecoleccion {
    @Headers(
        "Server: ${clConstant.sServer}",
        "DataBase: ${clConstant.sDataBase}",
        "AuthToken: ${clConstant.sToken}",
        "Content-Type: application/json"
    )
    @POST("RequestDatabase")
    suspend fun getItinerarioServicios(@Body Query: clQuery): Response<clRegistroRecoleccionDetalleApi>
    @Headers(
        "Server: ${clConstant.sServer}",
        "DataBase: ${clConstant.sDataBase}",
        "AuthToken: ${clConstant.sToken}",
        "Content-Type: application/json"
    )
    @POST("RequestDatabase")
    suspend fun setItinerarioTerminado(@Body Query: clQuery): Response<clItinerarioTerminado>
}