package com.imbitbox.recolectora.models.clRegistroRecoleccion

import androidx.camera.core.processing.SurfaceProcessorNode.In

data class clRegistroRecoleccionDetalleApi(
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
    val data: List<clRegistroRecoleccionServicios>
)
data class clItinerarioTerminado(
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
)
data class clRegistroRecoleccionServicios(
    var IdSucursal: Int = 0,
    var TipoServicio: String = "",
    var IdSucursalTipoServicio: Int = 0
)
data class clRegistroRecoleccionServiciosData(
    var Servicios: List<clRegistroRecoleccionServicios>
)
data class clRegistroRecoleccionServiciosInsert (
    var IdRuta: Int = 0,
    var Fecha: String = ""
)

data class clRegistroRecoleccionDetalleData(
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
    val data: List<clRegistroServiciosTabla>
)
data class clRegistroServiciosTabla(
    var Id: Int = 0,
    var IdSucursalTipoServicio: Int = 0,
    var TipoServicio: String = "",
    var Cantidad: Int = 0,
    var IdItinerarioDetalle: Int = 0,
)

data class clRegistroRecoleccionSaveAll(
    var IdItinerarioDetalle: Int = 0,
    var HoraTermino: String = "",
    var NumeroEmpleado: Int = 0,
    var NombreEmpleado: String = "",
    var FirmaDigital: String = "",
    var Usuario: Int = 0,
    var IpOS: String = "",
    var MqnVersion: String = "",
    var Imagenes: List<clImagenes>,
    var Detalle : List<clDetalle>
)

data class clImagenes(
    var Tipo: String = "",
    var Imagen: String = "",
    var Ruta: String = "",
)
data class clDetalle(
    var IdSucursalTipoServicio: Int = 0,
    var CantidadRecolectada: Int = 0,
)
data class clImgList(
    var Imagenes: List<clImagenes>
)
data class clDetalleList(
    var Detalle : List<clDetalle>
)