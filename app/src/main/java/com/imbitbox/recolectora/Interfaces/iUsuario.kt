package com.imbitbox.recolectora.Interfaces

import com.imbitbox.recolectora.helpers.clConstant.Companion.sDataBase
import com.imbitbox.recolectora.helpers.clConstant.Companion.sServer
import com.imbitbox.recolectora.helpers.clConstant.Companion.sToken
import com.imbitbox.recolectora.models.clUsuario.clUsuarioApi
import com.imbitbox.recolectora.models.tools.clQuery
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IUsuario {
    @Headers(
        "Server: $sServer",
        "DataBase: $sDataBase",
        "AuthToken: $sToken",
        "Content-Type: application/json"
    )
    @POST("RequestDatabase")
    suspend fun getUsuario(@Body Query: clQuery): Response<clUsuarioApi>
}