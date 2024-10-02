package com.imbitbox.recolectora.viewModel

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imbitbox.recolectora.dataBase.clImgItinerarioDB
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import com.imbitbox.recolectora.dataBase.clRegistroRecoleccionDetalleDB
import com.imbitbox.recolectora.dataBase.clServiciosRecoleccionDB
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clDetalle
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clDetalleList
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clImagenes
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clImgList
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clItinerarioTerminado
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionDetalleApi
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionDetalleData
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionSaveAll
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionServicios
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionServiciosData
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionServiciosInsert
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroServiciosTabla
import com.imbitbox.recolectora.repository.clImgItinerarioRepositoryDB
import com.imbitbox.recolectora.repository.clItinerarioDetalleRepositoryDB
import com.imbitbox.recolectora.repository.clRegistroRecoleccionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import java.time.LocalTime

@HiltViewModel
class clRegistroRecoleccionViewModel @Inject constructor(
    private val repoImgDB: clImgItinerarioRepositoryDB,
    private val repoRegDB: clItinerarioDetalleRepositoryDB,
    private val repoServicios: clRegistroRecoleccionRepository,
) : ViewModel() {
    var nDistancia: Int = 0
    private val _serviciosRecoleccion = MutableStateFlow<clRegistroRecoleccionDetalleApi>(
        clRegistroRecoleccionDetalleApi(
            false, 0, "", "", "", "",
            emptyList()
        )
    )
    val serviciosRecoleccion = _serviciosRecoleccion.asStateFlow()

    private val _serviciosRecoleccionByIdSucursalDB =
        MutableStateFlow<List<clServiciosRecoleccionDB>>(emptyList())
    private val _serviciosRecoleccionByIdSucursal =
        MutableStateFlow<clRegistroRecoleccionServiciosData>(
            clRegistroRecoleccionServiciosData(emptyList())
        )
    val serviciosRecoleccionByIdSucursal = _serviciosRecoleccionByIdSucursal.asStateFlow()

    private val _itinerarioTerminado = MutableStateFlow<clItinerarioTerminado>(
        clItinerarioTerminado(
            false, 0, "", "", "", ""
        )
    )
    val itinerarioTerminado = _itinerarioTerminado.asStateFlow()

    private val _RegistroRecoleccionDetalleDB =
        MutableStateFlow<List<clRegistroRecoleccionDetalleDB>>(emptyList())

    private val _RegistroRecoleccionDetalle = MutableStateFlow<clRegistroRecoleccionDetalleData>(
        clRegistroRecoleccionDetalleData(
            false, 0, "", "", "", "",
            emptyList()
        )
    )

    val RegistroRecoleccionDetalle = _RegistroRecoleccionDetalle.asStateFlow()

    private val _registroDetalleSave = MutableStateFlow<clDetalleList>(
        clDetalleList(emptyList())
    )
    val registroDetalleSave = _registroDetalleSave.asStateFlow()

    private val _imgItinerarioDB =
        MutableStateFlow<List<clImgItinerarioDB>>(emptyList())
    private val _Imgitinerario = MutableStateFlow<clImgList>(
        clImgList(emptyList())
    )
    val imgItinerario = _Imgitinerario.asStateFlow()


    fun getDistance(
        xLatitud: Double,
        xLongitud: Double,
        xStoreLatitud: Double,
        xStoreLongitud: Double
    ): Int {
        viewModelScope.launch {
            val UserLocation: Location = Location("locationA")
            val StoreLocation: Location = Location("locationB")

            UserLocation.setLatitude(xLatitud)
            UserLocation.setLongitude(xLongitud)

            StoreLocation.setLatitude(xStoreLatitud)
            StoreLocation.setLongitude(xStoreLongitud)
            nDistancia = UserLocation.distanceTo(StoreLocation).toInt()

        }
        return nDistancia
    }

    fun getSucursal(sDataSucursal: String, nIdSucursal: Int): Boolean {
        var bResultado: Boolean = false
        val objQrCode = sDataSucursal.split("%")
        if (objQrCode.count() == 2) {
            if (objQrCode[1].substring(0, 1) == "S") {
                println("Es una Sucursal")
                if (nIdSucursal == objQrCode[0].toInt()) {
                    bResultado = true
                }
            }
        }
        return bResultado
    }

    @SuppressLint("SimpleDateFormat")
    fun saveImage(
        sIdItinerarioDetalle: String,
        sTipoImagen: String,
        sNameImage: String,
        sPath: String
    ): Boolean {
        try {

            CoroutineScope(Dispatchers.IO).launch {
                //Guardamos en BD
                repoImgDB.setImgItinerario(
                    clImgItinerarioDB(
                        IdItinerarioDetalle = sIdItinerarioDetalle.toInt(),
                        Tipo = sTipoImagen,
                        Imagen = sNameImage,
                        Ruta = sPath + "/"
                    )
                )
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun saveFirma(sIdItinerarioDetalle: String, sFirma: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //Guardamos en BD
            repoRegDB.setItinerarioDetalleFirma(sIdItinerarioDetalle.toInt(), sFirma)
        }

    }

    fun saveRutaFirma(sIdItinerarioDetalle: String, sRutaFirma: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //Guardamos en BD
            repoRegDB.setItinerarioDetalleRutaFirma(sIdItinerarioDetalle.toInt(), sRutaFirma)
        }

    }

    fun getImgItinerarioByID(nIdItinerarioDetalle: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repoImgDB.getImgItinerarioByID(nIdItinerarioDetalle).collect { item ->
                if (item.isEmpty()) {
                    _imgItinerarioDB.value = emptyList()
                } else {
                    _imgItinerarioDB.value = item
                }
            }
        }
    }

    fun setRoomtoObjImagenes() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_imgItinerarioDB.value.isNotEmpty()) {
                var objImgItinerario: ArrayList<clImagenes> = arrayListOf()
                val nTotal: Int = _imgItinerarioDB.value.count() - 1
                for (i in 0..nTotal) {
                    objImgItinerario.add(
                        clImagenes(
                            Imagen = _imgItinerarioDB.value[i].Imagen,
                            Tipo = _imgItinerarioDB.value[i].Tipo,
                            Ruta = _imgItinerarioDB.value[i].Ruta,
                        )
                    )
                }

                val objImagenes = clImgList(
                    objImgItinerario
                )

                _Imgitinerario.value = objImagenes
            } else {
                _Imgitinerario.value = clImgList(
                    emptyList()
                )
            }
        }
    }

    fun getImgItinerarioByTipoDB(nIdItinerarioDetalle: Int, sTipo: String): Int {
        val nCantidad = repoImgDB.getImgItinerarioByTipo(nIdItinerarioDetalle, sTipo)
        return nCantidad
    }

    fun deleteLastImgItinerario(nIdItinerarioDetalle: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repoImgDB.deleteLastImgItinerario(nIdItinerarioDetalle)
        }
    }

    fun setImgItinerarioEliminado(nIdItinerarioDetalle: Int) {
        repoImgDB.setImgItinerarioEliminado(nIdItinerarioDetalle)
    }

    fun getServiciosRecoleccion(nIdRuta: Int, sFecha: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = withContext(Dispatchers.IO) {
                repoServicios.getServicios(clRegistroRecoleccionServiciosInsert(nIdRuta, sFecha))

            }
            _serviciosRecoleccion.value = ((result ?: (clRegistroRecoleccionDetalleApi(
                false, 0, "", "", "", "",
                emptyList()
            ))))
            if (_serviciosRecoleccion.value.success) {
                val nTotal: Int = _serviciosRecoleccion.value.data.count() - 1
                for (i in 0..nTotal) {
                    repoRegDB.setServiciosRecoleccion(
                        clServiciosRecoleccionDB(
                            IdSucursalTipoServicio = _serviciosRecoleccion.value.data[i].IdSucursalTipoServicio,
                            IdSucursal = _serviciosRecoleccion.value.data[i].IdSucursal,
                            TipoServicio = _serviciosRecoleccion.value.data[i].TipoServicio,
                        )
                    )
                }
            }
        }
    }

    fun getServiciosRecoleccionSucursal(nIdSucursal: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repoRegDB.getServiciosRecoleccionByIdSucursal(nIdSucursal).collect { item ->
                if (item.isEmpty()) {
                    _serviciosRecoleccionByIdSucursalDB.value = emptyList()
                } else {
                    _serviciosRecoleccionByIdSucursalDB.value = item
                }
            }
        }
    }

    fun setRoomtoObjServiciosRecoleccionSucursal() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_serviciosRecoleccionByIdSucursalDB.value.isNotEmpty()) {
                var objServiciosRecoleccion: ArrayList<clRegistroRecoleccionServicios> =
                    arrayListOf()
                val nTotal: Int = _serviciosRecoleccionByIdSucursalDB.value.count() - 1
                for (i in 0..nTotal) {
                    objServiciosRecoleccion.add(
                        clRegistroRecoleccionServicios(
                            IdSucursalTipoServicio = _serviciosRecoleccionByIdSucursalDB.value[i].IdSucursalTipoServicio,
                            IdSucursal = _serviciosRecoleccionByIdSucursalDB.value[i].IdSucursal,
                            TipoServicio = _serviciosRecoleccionByIdSucursalDB.value[i].TipoServicio,
                        )
                    )
                }

                val objServiciosRecoleccionData = clRegistroRecoleccionServiciosData(
                    objServiciosRecoleccion
                )

                _serviciosRecoleccionByIdSucursal.value = objServiciosRecoleccionData
            } else {
                _serviciosRecoleccionByIdSucursal.value = clRegistroRecoleccionServiciosData(
                    emptyList()
                )
            }
        }
    }

    fun getRegistroRecoleccionDetalle(nIdItinerarioDetalle: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repoRegDB.getRegistroRecoleccionDetalle(nIdItinerarioDetalle).collect { item ->
                if (item.isEmpty()) {
                    _RegistroRecoleccionDetalleDB.value = emptyList()
                } else {
                    _RegistroRecoleccionDetalleDB.value = item
                }
            }
        }
    }

    fun setRoomtoObjRegistroRecoleccionDetalle() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_RegistroRecoleccionDetalleDB.value.isNotEmpty()) {
                var objRegistroRecoleccionDetalle: ArrayList<clRegistroServiciosTabla> =
                    arrayListOf()
                val nTotal: Int = _RegistroRecoleccionDetalleDB.value.count() - 1
                for (i in 0..nTotal) {
                    objRegistroRecoleccionDetalle.add(
                        clRegistroServiciosTabla(
                            _RegistroRecoleccionDetalleDB.value[i].Id,
                            _RegistroRecoleccionDetalleDB.value[i].IdSucursalTipoServicio,
                            _RegistroRecoleccionDetalleDB.value[i].TipoServicio,
                            _RegistroRecoleccionDetalleDB.value[i].Cantidad,
                            _RegistroRecoleccionDetalleDB.value[i].IdItinerarioDetalle,

                            )
                    )
                }

                var objRegistroRecoleccionDetalleData = clRegistroRecoleccionDetalleData(
                    true,
                    _RegistroRecoleccionDetalleDB.value.size,
                    LocalDateTime.now().toString(),
                    "",
                    "",
                    "10ms",
                    objRegistroRecoleccionDetalle
                )

                _RegistroRecoleccionDetalle.value = objRegistroRecoleccionDetalleData
            } else {
                _RegistroRecoleccionDetalle.value = clRegistroRecoleccionDetalleData(
                    false, 0, "", "", "", "",
                    emptyList()
                )
            }
        }
    }

    fun setDetalleListToSave() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_RegistroRecoleccionDetalleDB.value.isNotEmpty()) {
                var objDetalleRecoleccion: ArrayList<clDetalle> = arrayListOf()
                val nTotal: Int = _RegistroRecoleccionDetalleDB.value.count() - 1
                for (i in 0..nTotal) {
                    objDetalleRecoleccion.add(
                        clDetalle(
                            _RegistroRecoleccionDetalleDB.value[0].IdSucursalTipoServicio,
                            _RegistroRecoleccionDetalleDB.value[0].Cantidad
                        )
                    )
                }

                var objRegistro = clDetalleList(
                    objDetalleRecoleccion
                )

                _registroDetalleSave.value = objRegistro
            } else {
                _registroDetalleSave.value = clDetalleList(
                    emptyList()
                )
            }
        }
    }

    fun delRegistroRecoleccionDetalleID(nId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repoRegDB.delRegistroRecoleccionDetalleID(nId)
        }
    }

    fun setRegistroRecoleccionDetalle(objRegistroRecoleccion: clRegistroServiciosTabla) {
        viewModelScope.launch(Dispatchers.IO) {
            repoRegDB.setRegistroRecoleccionDetalle(
                clRegistroRecoleccionDetalleDB(
                    IdSucursalTipoServicio = objRegistroRecoleccion.IdSucursalTipoServicio,
                    TipoServicio = objRegistroRecoleccion.TipoServicio,
                    Cantidad = objRegistroRecoleccion.Cantidad,
                    IdItinerarioDetalle = objRegistroRecoleccion.IdItinerarioDetalle
                )
            )
        }
    }

    fun updateRegistroRecoleccionDetalleCantidad(nIndex: Int, nCantidad: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repoRegDB.setRegistroRecoleccionDetalle(
                clRegistroRecoleccionDetalleDB(
                    _RegistroRecoleccionDetalle.value.data[nIndex].Id,
                    _RegistroRecoleccionDetalle.value.data[nIndex].IdSucursalTipoServicio,
                    _RegistroRecoleccionDetalle.value.data[nIndex].TipoServicio,
                    nCantidad,
                    _RegistroRecoleccionDetalle.value.data[nIndex].IdItinerarioDetalle
                )
            )
        }
    }

    fun saveItinerarioDetalle(
        objItinerario: clRegistroRecoleccionSaveAll,
        objItinerarioDetalleDB: clItinerarioDetalleDB
    ) {
        var bSuccess: Boolean = false
        CoroutineScope(Dispatchers.IO).launch {
            val result = withContext(Dispatchers.IO) {
                repoServicios.saveItinerarioDetalle(
                    objItinerario = clRegistroRecoleccionSaveAll(
                        objItinerario.IdItinerarioDetalle,
                        objItinerario.HoraTermino,
                        objItinerario.NumeroEmpleado,
                        objItinerario.NombreEmpleado,
                        objItinerario.FirmaDigital,
                        objItinerario.Usuario,
                        objItinerario.IpOS,
                        objItinerario.MqnVersion,
                        objItinerario.Imagenes,
                        objItinerario.Detalle
                    ),
                )
            }
            try {
                if (result != null) {
                    if (result.success) {
                        uploadFileToFTP(
                            sServer = "imbitbox.com",
                            nPort = 21,
                            sUser = "Versiones",
                            sPass = "sistemas123,",
                            sFilePath = objItinerarioDetalleDB.RutaFirma + "/" + objItinerarioDetalleDB.FirmaDigital,
                            sRemotePath = "/Recolectora/Files/RecoleccionTest/${objItinerarioDetalleDB.FirmaDigital}"
                        )
                        for (i in 0..objItinerario.Imagenes.size - 1) {
                            uploadFileToFTP(
                                sServer = "imbitbox.com",
                                nPort = 21,
                                sUser = "Versiones",
                                sPass = "sistemas123,",
                                sFilePath = objItinerario.Imagenes[i].Ruta + objItinerario.Imagenes[i].Imagen,
                                sRemotePath = "/Recolectora/Files/RecoleccionTest/${objItinerario.Imagenes[i].Imagen}"
                            )
                        }
                       _itinerarioTerminado.value = (clItinerarioTerminado(
                            true, 1, LocalDate.now().toString().substring(0, 10), "", "", ""
                        ))
                    } else {
                        _itinerarioTerminado.value = (clItinerarioTerminado(
                            false, 0, LocalDate.now().toString().substring(0, 10), "", "", ""
                        ))
                    }
                } else {
                    _itinerarioTerminado.value = (clItinerarioTerminado(
                        false, 0, LocalDate.now().toString().substring(0, 10), "", "", ""
                    ))
                }
            } catch (error: Exception) {
                _itinerarioTerminado.value = (clItinerarioTerminado(
                    false, 0, "", "Error FTP", "", ""
                ))
            }
        }
    }

    fun setItinerarioDetalleEliminado(nIdItinerarioDetalle: Int) {
        repoRegDB.setItinerarioDetalleEliminado(nIdItinerarioDetalle)
    }

    fun setRegistroRecoleccionDetalleEliminado(nIdItinerarioDetalle: Int) {
        repoRegDB.setRegistroRecoleccionDetalleEliminado(nIdItinerarioDetalle)
    }

    fun delObjItinerarioTerminado() {
        _itinerarioTerminado.value = (clItinerarioTerminado(
            false, 0, "", "", "", ""
        ))
    }

    fun setItinerarioDetalleTermino(
        nIdItinerarioDetalle: Int,
        sHoraTermino: String,
        nNumEmpleado: Int,
        sNombreEmpleado: String
    ) {
        repoRegDB.setItinerarioDetalleTermino(
            nIdItinerarioDetalle,
            sHoraTermino,
            nNumEmpleado,
            sNombreEmpleado
        )
    }

    fun uploadFileToFTP(
        sServer: String,
        nPort: Int,
        sUser: String,
        sPass: String,
        sFilePath: String,
        sRemotePath: String
    ): Boolean {
        val ftpClient = FTPClient()
        return try {
            ftpClient.connect(sServer, nPort)
            ftpClient.login(sUser, sPass)
            ftpClient.enterLocalPassiveMode()
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

            val localFile = File(sFilePath)
            val inputStream = FileInputStream(localFile)

            val done = ftpClient.storeFile(sRemotePath, inputStream)
            inputStream.close()
            done
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        } finally {
            ftpClient.logout()
            ftpClient.disconnect()
        }
    }
}
