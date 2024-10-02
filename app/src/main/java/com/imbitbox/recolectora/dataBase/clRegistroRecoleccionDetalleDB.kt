package com.imbitbox.recolectora.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RegistroRecoleccionDetalle")
data class clRegistroRecoleccionDetalleDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var Id: Int = 0,
    @ColumnInfo(name = "IdSucursalTipoServicio")
    var IdSucursalTipoServicio: Int = 0,
    @ColumnInfo(name = "TipoServicio")
    var TipoServicio: String = "",
    @ColumnInfo(name = "Cantidad")
    var Cantidad: Int = 0,
    @ColumnInfo(name = "IdItinerarioDetalle")
    var IdItinerarioDetalle: Int = 0,
    @ColumnInfo(name = "Eliminado")
    var Eliminado : Boolean = false
)
