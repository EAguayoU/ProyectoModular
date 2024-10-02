package com.imbitbox.recolectora.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "ServiciosRecoleccion")
data class clServiciosRecoleccionDB(
@PrimaryKey(autoGenerate = false)
@ColumnInfo(name = "IdSucursalTipoServicio")
var IdSucursalTipoServicio: Int = 0,
@ColumnInfo(name = "IdSucursal")
var IdSucursal: Int = 0,
@ColumnInfo(name = "TipoServicio")
var TipoServicio: String = "",
)
