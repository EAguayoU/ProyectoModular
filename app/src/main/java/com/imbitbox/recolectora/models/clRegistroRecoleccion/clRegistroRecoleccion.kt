package com.imbitbox.recolectora.models.clRegistroRecoleccion


data class clRegistroRecoleccion (
    var IdItinerarioDetalle : Int = 0,
    var IdItinerario : Int = 0,
    var IdSucursal : Int = 0,
    var Sucursal : String = "",
    var Latitud : String = "",
    var Longitud : String = "",
    var IdVehiculo : Int = 0,
    var Vehiculo : String = "",
    var IdCliente : Int = 0,
    var Cliente : String = "",
    var CatEstatus : Int = 0,
    var Estatus : String = "",
    var Orden : Int = 0,
    var FotosAntes : Int = 0,
    var FotosDespues : Int = 0,
    var FirmaDigital : String = "",
    var QR : Boolean = false,
    var Fecha : String = "",
    var HoraAsignacion : String = "",
    var HoraLlegada : String = "",
    var HoraTermino : String = "",
    var Horario : String = "",
    var NumeroEmpleado : String = "",
    var NombreEmpleado : String = "",
    var TipoAsignacion : String = "",
    var Umbral : Int = 0
)
