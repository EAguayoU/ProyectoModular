package com.imbitbox.recolectora.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity("ImgItinerario")
data class clImgItinerarioDB(
    @PrimaryKey(true)
    @ColumnInfo("IdImgItinerario")
    var IdImgItinerario : Int = 0,
    @ColumnInfo("IdItinerarioDetalle")
    var IdItinerarioDetalle : Int = 0,
    @ColumnInfo("Tipo")
    var Tipo : String = "",
    @ColumnInfo("Imagen")
    var Imagen : String = "",
    @ColumnInfo(name = "Ruta")
    var Ruta: String = "",
    @ColumnInfo(name = "Eliminado")
    var Eliminado : Boolean = false
)
