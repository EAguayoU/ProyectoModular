package com.imbitbox.recolectora.repository

import com.google.gson.Gson
import com.imbitbox.recolectora.Interfaces.IUsuario
import com.imbitbox.recolectora.models.clUsuario.clUsuario
import com.imbitbox.recolectora.models.clUsuario.clUsuarioApi
import com.imbitbox.recolectora.models.tools.clQuery
import javax.inject.Inject

class clUsuarioRepository @Inject constructor(private val usuarioApi: IUsuario) {
    suspend fun getUsuario(objUsuario : clUsuario): clUsuarioApi? {
        val jUsuario = Gson().toJson(objUsuario)
        val response = usuarioApi.getUsuario(clQuery("sp_Usuario_Login  '$jUsuario'"))

        if(response.isSuccessful){
            return response.body()
        }
        return null
    }
}