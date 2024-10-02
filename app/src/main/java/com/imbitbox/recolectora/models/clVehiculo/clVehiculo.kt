package com.imbitbox.recolectora.models.clVehiculo
//Retorna Api
data class clVehiculoApi (
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
    val data: List<clVehiculo>
)
data class clVehiculoKmApi (
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
    val data: List<clVehiculoKm>
)
//Modelos
data class clVehiculo (
    var IdVehiculo : Int = 0,
    var Marca : String = "",
    var Modelo : String = "",
    var Anio : Int = 0,
    var Placas : String = "",
    var NumSerie : String = "",
    var NumEco : String = "",
    var CantUniMed : Int = 0,
    var Campo : String = "",
    var Capacidad : Double = 0.0,
)

data class clVehiculoKm (
    var IdVehiculoKM : Int = 0,
    var IdVehiculo : Int = 0,
    var Marca : String = "",
    var Modelo : String = "",
    var Anio : Int = 0,
    var Placas : String = "",
    var NumSerie : String = "",
    var NumEco : String = "",
    var KmInicial : Int = 0,
    var KmFinal : Int = 0,
    var Recorrido : Int = 0,
    var Fecha : String = "",
    var Eliminado : Boolean = false
)

data class clVehiculoKmInsert (
    var IdVehiculo : Int = 0,
    var KmInicial : Int = 0,
    var KmFinal : Int = 0,
    var Fecha : String = "",
    var Eliminado : Boolean = false,
    var IpOS : String = "",
    var MqnVersion : String = "",
    var Usuario : String = ""
)
