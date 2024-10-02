package com.imbitbox.recolectora.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItinerarioDetalle")
data class clItinerarioDetalleDB(
    @PrimaryKey(false)
    @ColumnInfo(name = "IdItinerarioDetalle")
    var IdItinerarioDetalle : Int = 0,
    @ColumnInfo(name = "IdItinerario")
    var IdItinerario : Int = 0,
    @ColumnInfo(name = "IdSucursal")
    var IdSucursal : Int = 0,
    @ColumnInfo(name = "Sucursal")
    var Sucursal : String = "",
    @ColumnInfo(name = "Latitud")
    var Latitud : String = "",
    @ColumnInfo(name = "Longitud")
    var Longitud : String = "",
    @ColumnInfo(name = "IdVehiculo")
    var IdVehiculo : Int = 0,
    @ColumnInfo(name = "Vehiculo")
    var Vehiculo : String = "",
    @ColumnInfo(name = "IdCliente")
    var IdCliente : Int = 0,
    @ColumnInfo(name = "Cliente")
    var Cliente : String = "",
    @ColumnInfo(name = "CatEstatus")
    var CatEstatus : Int = 0,
    @ColumnInfo(name = "Estatus")
    var Estatus : String = "",
    @ColumnInfo(name = "Orden")
    var Orden : Int = 0,
    @ColumnInfo(name = "FotosAntes")
    var FotosAntes : Int = 0,
    @ColumnInfo(name = "FotosDespues")
    var FotosDespues : Int = 0,
    @ColumnInfo(name = "FirmaDigital")
    var FirmaDigital : String = "",
    @ColumnInfo(name = "RutaFirma")
    var RutaFirma : String = "",
    @ColumnInfo(name = "QR")
    var QR : Boolean = false,
    @ColumnInfo(name = "Fecha")
    var Fecha : String = "",
    @ColumnInfo(name = "HoraAsignacion")
    var HoraAsignacion : String = "",
    @ColumnInfo(name = "HoraLlegada")
    var HoraLlegada : String = "",
    @ColumnInfo(name = "HoraTermino")
    var HoraTermino : String = "",
    @ColumnInfo(name = "Horario")
    var Horario : String = "",
    @ColumnInfo(name = "NumeroEmpleado")
    var NumeroEmpleado : String = "",
    @ColumnInfo(name = "NombreEmpleado")
    var NombreEmpleado : String = "",
    @ColumnInfo(name = "TipoAsignacion")
    var TipoAsignacion : String = "",
    @ColumnInfo(name = "Umbral")
    var Umbral : Int = 0,
    @ColumnInfo(name = "EsNumeroEmpleadoRecoleccion")
    var EsNumeroEmpleadoRecoleccion : Boolean = false,
    @ColumnInfo(name = "Eliminado")
    var Eliminado : Boolean = false
)


