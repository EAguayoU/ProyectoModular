package com.imbitbox.recolectora.Interfaces

import com.imbitbox.recolectora.helpers.clConstant
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoApi
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKmApi
import com.imbitbox.recolectora.models.tools.clQuery
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface iVehiculo {
    @Headers(
        "Server: ${clConstant.sServer}",
        "DataBase: ${clConstant.sDataBase}",
        "AuthToken: ${clConstant.sToken}",
        "Content-Type: application/json"
    )
    @POST("RequestDatabase")
    suspend fun getVehiculo(@Body Query: clQuery): Response<clVehiculoApi>

    @Headers(
        "Server: ${clConstant.sServer}",
        "DataBase: ${clConstant.sDataBase}",
        "AuthToken: ${clConstant.sToken}",
        "Content-Type: application/json"
    )
    @POST("RequestDatabase")
    suspend fun setVehiculoKm(@Body Query: clQuery): Response<clVehiculoKmApi>

    @Headers(
        "Server: ${clConstant.sServer}",
        "DataBase: ${clConstant.sDataBase}",
        "AuthToken: ${clConstant.sToken}",
        "Content-Type: application/json"
    )
    @GET("RequestDatabase")
    suspend fun getVehiculoKm(@Body Query: clQuery): Response<clVehiculoKmApi>
}