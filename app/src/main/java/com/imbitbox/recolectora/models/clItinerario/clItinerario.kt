package com.imbitbox.recolectora.models.clItinerario

data class clItinerarioApi(
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
    val data: List<clItinerario>
)
data class clItinerario (
    var IdItinerario : Int = 0,
    var IdEmpleado : Int = 0,
    var Empleado : String = "",
    var IdVehiculo : Int = 0,
    var Vehiculo : String = "",
    var IdRuta : Int = 0,
    var Ruta : String = "",
    var CatStatus : Int = 0,
    var Estatus : String = "",
    var Eliminado : Boolean = false
)