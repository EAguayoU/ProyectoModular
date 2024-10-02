package com.imbitbox.recolectora.models.clItinerario

data class clItinerarioDetalleApi(
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
    val data: List<clItinerarioDetalle>
)
data class clItinerarioDetalle (
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
    var RutaFirma : String = "",
    var QR : Boolean = false,
    var Fecha : String = "",
    var HoraAsignacion : String = "",
    var HoraLlegada : String = "",
    var HoraTermino : String = "",
    var Horario : String = "",
    var NumeroEmpleado : String = "",
    var NombreEmpleado : String = "",
    var TipoAsignacion : String = "",
    var Umbral : Int = 0,
    var EsNumeroEmpleadoRecoleccion : Boolean = false,
    var Eliminado : Boolean = false,
)

data class clItinerarioDetalleInsert(
    var IdItinerario : Int = 0,
    var IdVehiculo : Int = 0,
    var Fecha : String = "",
    var IpOS : String = "",
    var MqnVersion : String = "",
    var Usuario : String = "",
    var Latitud : Double = 0.00,
    var Longitud : Double = 0.00,
)

data class clItinerarioEstatusInsert(
    var IdItinerarioDetalle: Int = 0,
    var CatEstatus: Int = 0,
    var QR: Boolean = false,
    var HoraAsignacion: String = "",
    var HoraLlegada: String = ""
)

