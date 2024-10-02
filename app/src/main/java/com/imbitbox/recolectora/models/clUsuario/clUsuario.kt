package com.imbitbox.recolectora.models.clUsuario

data class clUsuarioApi(
    val success: Boolean,
    val records: Int,
    val date : String,
    val message: String,
    val messageDev: String,
    val executionTime : String,
    val data: List<clUsuario>
)
data class clUsuario (
    var IdUsuario : Int = 0,
    var IdEmpleado : Int = 0,
    var Nombre : String = "",
    var Paterno : String = "",
    var Materno : String = "",
    var Usuario : String = "",
    var Password : String = "",
    var Grupo : String = "",
    var IdGrupo : Int = 0,
    var Email : String = "",
    var Eliminado : Boolean = false
)
