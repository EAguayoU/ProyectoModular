package com.imbitbox.recolectora.Interfaces

import com.imbitbox.recolectora.helpers.clConstant
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleApi
import com.imbitbox.recolectora.models.tools.clQuery
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface iItinerarioDetalle {
    @Headers(
        "Server: ${clConstant.sServer}",
        "DataBase: ${clConstant.sDataBase}",
        "AuthToken: ${clConstant.sToken}",
        "Content-Type: application/json"
    )
    @POST("RequestDatabase")
    suspend fun getItinerarioDetalle(@Body Query: clQuery): Response<clItinerarioDetalleApi>

}