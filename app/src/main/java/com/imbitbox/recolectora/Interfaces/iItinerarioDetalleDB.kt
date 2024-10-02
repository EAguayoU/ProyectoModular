package com.imbitbox.recolectora.Interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.imbitbox.recolectora.dataBase.clImgItinerarioDB
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import com.imbitbox.recolectora.dataBase.clRegistroRecoleccionDetalleDB
import com.imbitbox.recolectora.dataBase.clServiciosRecoleccionDB
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroServiciosTabla
import kotlinx.coroutines.flow.Flow

@Dao
interface iItinerarioDetalleDB {

    @Query("SELECT * FROM itinerariodetalle WHERE Eliminado = 0")
    fun getItinerarioDetalle(): Flow<List<clItinerarioDetalleDB>>

    @Query("SELECT * FROM itinerariodetalle WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun getItinerarioDetalleById(nIdItinerarioDetalle : Int):Flow<clItinerarioDetalleDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setItinerarioDetalle(objItinerarioDetalleDB: clItinerarioDetalleDB)

    @Query("DELETE FROM itinerariodetalle WHERE CatEstatus NOT IN (36)")
    fun delItinerarioDetallePending()
    @Query("DELETE FROM itinerariodetalle WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun delItinerarioDetalleByID(nIdItinerarioDetalle: Int)

    @Query("UPDATE itinerariodetalle SET CatEstatus = 34, Estatus = 'EN CAMINO', HoraAsignacion = :sHoraAsignacion " +
            "WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setItinerarioDetalleEnCamino(nIdItinerarioDetalle: Int, sHoraAsignacion : String)

    @Query("UPDATE itinerariodetalle SET CatEstatus = 35, Estatus = 'EN PROCESO', HoraLlegada = :sHoraLlegada " +
            "WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setItinerarioDetalleEnProceso(nIdItinerarioDetalle: Int, sHoraLlegada : String)

    @Query("UPDATE itinerariodetalle SET CatEstatus = 36, Estatus = 'TERMINO',  HoraTermino = :sHoraTermino, NumeroEmpleado = :nNumEmpleado, NombreEmpleado = :sNombreEmpleado " +
            "WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setItinerarioDetalleTermino(nIdItinerarioDetalle: Int, sHoraTermino : String, nNumEmpleado : Int, sNombreEmpleado : String)

    @Query("UPDATE itinerariodetalle SET QR = true WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setItinerarioDetalleQR(nIdItinerarioDetalle: Int)

    @Query("UPDATE itinerariodetalle SET Eliminado = 1 WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setItinerarioDetalleEliminado(nIdItinerarioDetalle: Int)

    @Query("UPDATE itinerariodetalle SET FirmaDigital = :sFirma  WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setItinerarioDetalleFirma(nIdItinerarioDetalle: Int, sFirma: String)
    @Query("UPDATE itinerariodetalle SET RutaFirma = :sRutaFirma  WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setItinerarioDetalleRutaFirma(nIdItinerarioDetalle: Int, sRutaFirma: String)

    //Imagenes
    @Query("SELECT COUNT(*) FROM imgitinerario WHERE IdItinerarioDetalle = :nIdItinerarioDetalle AND Tipo = :sTipo ")
    fun getImgItinerarioByTipo(nIdItinerarioDetalle:Int, sTipo: String): Int

    @Query("SELECT * FROM imgItinerario WHERE IdItinerarioDetalle= :nIdItinerarioDetalle")
    fun getImgItinerarioByID(nIdItinerarioDetalle:Int): Flow<List<clImgItinerarioDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setImgItinerario(objImgItinerarioDB: clImgItinerarioDB)
    @Query("DELETE  FROM imgitinerario WHERE IdImgItinerario IN (SELECT IdImgItinerario FROM ImgItinerario WHERE IdItinerarioDetalle = :IdItinerarioDetalle ORDER BY IdImgItinerario DESC Limit 1)" )
    fun deleteLastImgItinerario(IdItinerarioDetalle:Int)
    @Query("UPDATE imgitinerario SET Eliminado = 1 WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setImgItinerarioEliminado(nIdItinerarioDetalle: Int)
    @Query("SELECT * FROM RegistroRecoleccionDetalle WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun getRegistroRecoleccionDetalle(nIdItinerarioDetalle:Int): Flow<List<clRegistroRecoleccionDetalleDB>>
    @Query("DELETE FROM RegistroRecoleccionDetalle WHERE id = :nId")
    fun delRegistroRecoleccionDetalleID(nId:Int)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setRegistroRecoleccionDetalle(objRecoleccionDetalleDB: clRegistroRecoleccionDetalleDB)
    @Query("UPDATE RegistroRecoleccionDetalle SET Eliminado = 1 WHERE IdItinerarioDetalle = :nIdItinerarioDetalle")
    fun setRegistroRecoleccionDetalleEliminado(nIdItinerarioDetalle: Int)
    @Query("SELECT * FROM ServiciosRecoleccion WHERE IdSucursal = :nIdSucursal")
    fun getServiciosRecoleccionByIdSucursal(nIdSucursal : Int): Flow<List<clServiciosRecoleccionDB>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setServiciosRecoleccion(clServiciosRecoleccionDB: clServiciosRecoleccionDB)
}