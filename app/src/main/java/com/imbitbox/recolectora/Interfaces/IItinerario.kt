package com.imbitbox.recolectora.Interfaces

import com.imbitbox.recolectora.helpers.clConstant
import com.imbitbox.recolectora.models.clItinerario.clItinerarioApi
import com.imbitbox.recolectora.models.tools.clQuery
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IItinerario {
    @Headers(
        "Server: ${clConstant.sServer}",
        "DataBase: ${clConstant.sDataBase}",
        "AuthToken: ${clConstant.sToken}",
        "Content-Type: application/json"
    )
    @POST("RequestDatabase")
    suspend fun getItinerarioEmpleado(@Body Query: clQuery): Response<clItinerarioApi>

}