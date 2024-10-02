package com.imbitbox.recolectora.repository

import com.imbitbox.recolectora.Interfaces.iItinerarioDetalleDB
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import com.imbitbox.recolectora.dataBase.clRegistroRecoleccionDetalleDB
import com.imbitbox.recolectora.dataBase.clServiciosRecoleccionDB
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class clItinerarioDetalleRepositoryDB @Inject constructor(private val iItinerarioDetalleDB: iItinerarioDetalleDB)
{
    fun getItinerarioDetalle(): Flow<List<clItinerarioDetalleDB>> {
        return iItinerarioDetalleDB.getItinerarioDetalle().flowOn(Dispatchers.IO).conflate()
    }

    fun getItinerarioDetalleById(nIdItinerarioDetalle : Int): Flow<clItinerarioDetalleDB>
    {
        return iItinerarioDetalleDB.getItinerarioDetalleById(nIdItinerarioDetalle).flowOn(
            Dispatchers.IO).conflate()
    }

    fun setItinerarioDetalle(objItinerarioDetalle: clItinerarioDetalleDB){
        iItinerarioDetalleDB.setItinerarioDetalle(objItinerarioDetalle)
    }
    fun delItinerarioDetalle(){
        iItinerarioDetalleDB.delItinerarioDetallePending()
    }

    fun setItinerarioDetalleEnCamino(nIdItinerarioDetalle: Int, sHoraAsignacion: String){
        iItinerarioDetalleDB.setItinerarioDetalleEnCamino(nIdItinerarioDetalle, sHoraAsignacion)
    }

    fun setItinerarioDetalleEnProceso(nIdItinerarioDetalle: Int, sHoraAsignacion: String){
        iItinerarioDetalleDB.setItinerarioDetalleEnProceso(nIdItinerarioDetalle, sHoraAsignacion)
    }
    fun setItinerarioDetalleTermino(nIdItinerarioDetalle: Int, sHoraTermino: String, nNumEmpleado: Int, sNombreEmpleado: String){
        iItinerarioDetalleDB.setItinerarioDetalleTermino(nIdItinerarioDetalle, sHoraTermino, nNumEmpleado, sNombreEmpleado )
    }
    fun setItinerarioDetalleQR(nIdItinerarioDetalle: Int){
        iItinerarioDetalleDB.setItinerarioDetalleQR(nIdItinerarioDetalle)
    }
    fun setItinerarioDetalleEliminado(nIdItinerarioDetalle: Int){
        iItinerarioDetalleDB.setItinerarioDetalleEliminado(nIdItinerarioDetalle)
    }
    fun setItinerarioDetalleFirma(nIdItinerarioDetalle: Int, sFirma: String){
        iItinerarioDetalleDB.setItinerarioDetalleFirma(nIdItinerarioDetalle, sFirma)
    }
    fun setItinerarioDetalleRutaFirma(nIdItinerarioDetalle: Int, sRutaFirma: String){
        iItinerarioDetalleDB.setItinerarioDetalleRutaFirma(nIdItinerarioDetalle, sRutaFirma)
    }
    fun getRegistroRecoleccionDetalle(nIdItinerarioDetalle: Int): Flow<List<clRegistroRecoleccionDetalleDB>> {
        return iItinerarioDetalleDB.getRegistroRecoleccionDetalle(nIdItinerarioDetalle).flowOn(Dispatchers.IO).conflate()
    }
    fun delRegistroRecoleccionDetalleID(nIdItinerarioDetalle: Int){
        iItinerarioDetalleDB.delRegistroRecoleccionDetalleID(nIdItinerarioDetalle)
    }
    fun setRegistroRecoleccionDetalle(objRecoleccionDetalleDB: clRegistroRecoleccionDetalleDB){
        iItinerarioDetalleDB.setRegistroRecoleccionDetalle(objRecoleccionDetalleDB)
    }
    fun setRegistroRecoleccionDetalleEliminado(nIdItinerarioDetalle: Int){
        iItinerarioDetalleDB.setRegistroRecoleccionDetalleEliminado(nIdItinerarioDetalle)
    }
    fun getServiciosRecoleccionByIdSucursal(nIdSucursal: Int): Flow<List<clServiciosRecoleccionDB>>{
        return iItinerarioDetalleDB.getServiciosRecoleccionByIdSucursal(nIdSucursal).flowOn(Dispatchers.IO).conflate()
    }
    fun setServiciosRecoleccion(clServiciosRecoleccionDB: clServiciosRecoleccionDB){
        iItinerarioDetalleDB.setServiciosRecoleccion(clServiciosRecoleccionDB)
    }
}