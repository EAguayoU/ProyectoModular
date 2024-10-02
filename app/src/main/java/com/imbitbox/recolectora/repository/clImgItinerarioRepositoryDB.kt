package com.imbitbox.recolectora.repository

import com.imbitbox.recolectora.Interfaces.iItinerarioDetalleDB
import com.imbitbox.recolectora.dataBase.clImgItinerarioDB
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class clImgItinerarioRepositoryDB @Inject constructor(private val iImgItinerarioDB:iItinerarioDetalleDB){

    fun getImgItinerarioByTipo(nIdItinerarioDetalle: Int, sTipo: String): Int {
        return iImgItinerarioDB.getImgItinerarioByTipo(nIdItinerarioDetalle, sTipo)
    }
    fun getImgItinerarioByID(nIdItinerarioDetalle: Int): Flow<List<clImgItinerarioDB>> {
        return iImgItinerarioDB.getImgItinerarioByID(nIdItinerarioDetalle).flowOn(Dispatchers.IO).conflate()
    }

    fun setImgItinerario(objImgItinerarioDB:clImgItinerarioDB){
        iImgItinerarioDB.setImgItinerario(objImgItinerarioDB)
    }
    fun deleteLastImgItinerario(nIdItinerarioDetalle: Int) {
         iImgItinerarioDB.deleteLastImgItinerario(nIdItinerarioDetalle)
    }

    fun setImgItinerarioEliminado(nIdItinerarioDetalle: Int){
        iImgItinerarioDB.setImgItinerarioEliminado(nIdItinerarioDetalle)
    }

}