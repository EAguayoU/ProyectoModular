package com.imbitbox.recolectora.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imbitbox.recolectora.Interfaces.iItinerarioDetalleDB


@Database(entities = [clItinerarioDetalleDB::class, clImgItinerarioDB::class, clRegistroRecoleccionDetalleDB::class, clServiciosRecoleccionDB::class],
    version = 5,
    exportSchema = false,
)
abstract class clDataBaseRoom : RoomDatabase(){
    abstract fun clDataBaseRoom(): iItinerarioDetalleDB
}
