package com.imbitbox.recolectora.viewModel

import android.location.Location
import android.os.Build
import android.view.View
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imbitbox.recolectora.BuildConfig
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalle
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleApi
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleInsert
import com.imbitbox.recolectora.models.clItinerario.clItinerarioEstatusInsert
import com.imbitbox.recolectora.repository.clItinerarioDetalleRepository
import com.imbitbox.recolectora.repository.clItinerarioDetalleRepositoryDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class clItinerarioDetalleViewModel @Inject constructor(
    private val repoApi: clItinerarioDetalleRepository,
    private val repoDB: clItinerarioDetalleRepositoryDB,
) : ViewModel() {

    private val _itinerarioDetalle = MutableStateFlow<clItinerarioDetalleApi>(
        clItinerarioDetalleApi(
            false, 0, "", "", "", "",
            emptyList()
        )
    )

    val itinerarioDetalle = _itinerarioDetalle.asStateFlow()

    private  var _objItinerarioDetalleDB = MutableStateFlow<clItinerarioDetalleDB>(
        clItinerarioDetalleDB()
    )

    val objItinerarioDetalleDB = _objItinerarioDetalleDB.asStateFlow()

    private var _itinerarioDetalleDB = MutableStateFlow<List<clItinerarioDetalleDB>>(emptyList())



    fun getItinerarioDetalleDB() {
        viewModelScope.launch(Dispatchers.IO) {

            repoDB.getItinerarioDetalle().collect { item ->
                if (item.isEmpty()) {
                    _itinerarioDetalleDB.value = emptyList()
                } else {
                    _itinerarioDetalleDB.value = item
                }
            }
        }
    }
    fun getItinerarioDetalleByID(nIdItinerarioDetalle: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            repoDB.getItinerarioDetalleById(nIdItinerarioDetalle).collect { item ->
                _objItinerarioDetalleDB.value = item
            }
        }
    }

    fun getItinerarioDetalle(
        nIdItinerario: Int,
        nIdVehiculo: Int,
        sUsuario: String,
        xLatitud: Double,
        xLongitud: Double
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            if (_itinerarioDetalleDB.value.isNotEmpty()) {
                //Retornamos el itinerario que esta guardado
                var objItinerarioDetalle: ArrayList<clItinerarioDetalle> = arrayListOf()
                val nTotal:Int = _itinerarioDetalleDB.value.count()-1
                for(i in 0..nTotal){
                    objItinerarioDetalle.add(
                        clItinerarioDetalle(
                            _itinerarioDetalleDB.value[i].IdItinerarioDetalle,
                            _itinerarioDetalleDB.value[i].IdItinerario,
                            _itinerarioDetalleDB.value[i].IdSucursal,
                            _itinerarioDetalleDB.value[i].Sucursal,
                            _itinerarioDetalleDB.value[i].Latitud,
                            _itinerarioDetalleDB.value[i].Longitud,
                            _itinerarioDetalleDB.value[i].IdVehiculo,
                            _itinerarioDetalleDB.value[i].Vehiculo,
                            _itinerarioDetalleDB.value[i].IdCliente,
                            _itinerarioDetalleDB.value[i].Cliente,
                            _itinerarioDetalleDB.value[i].CatEstatus,
                            _itinerarioDetalleDB.value[i].Estatus,
                            _itinerarioDetalleDB.value[i].Orden,
                            _itinerarioDetalleDB.value[i].FotosAntes,
                            _itinerarioDetalleDB.value[i].FotosDespues,
                            _itinerarioDetalleDB.value[i].FirmaDigital,
                            _itinerarioDetalleDB.value[i].RutaFirma,
                            _itinerarioDetalleDB.value[i].QR,
                            _itinerarioDetalleDB.value[i].Fecha,
                            _itinerarioDetalleDB.value[i].HoraAsignacion,
                            _itinerarioDetalleDB.value[i].HoraLlegada,
                            _itinerarioDetalleDB.value[i].HoraTermino,
                            _itinerarioDetalleDB.value[i].Horario,
                            _itinerarioDetalleDB.value[i].NumeroEmpleado,
                            _itinerarioDetalleDB.value[i].NombreEmpleado,
                            _itinerarioDetalleDB.value[i].TipoAsignacion,
                            _itinerarioDetalleDB.value[i].Umbral,
                            _itinerarioDetalleDB.value[i].EsNumeroEmpleadoRecoleccion,
                            _itinerarioDetalleDB.value[i].Eliminado
                        )
                    )
                }

                var objItinerarioDetalleApi = clItinerarioDetalleApi(
                    true,
                    _itinerarioDetalleDB.value.size,
                    LocalDateTime.now().toString(),
                    "",
                    "",
                    "10ms",
                    objItinerarioDetalle
                )

                _itinerarioDetalle.value = objItinerarioDetalleApi

            } else {
                //Generamos itinerario nuevo
                val result = withContext(Dispatchers.IO) {
                    repoApi.getItinerarioDetalle(
                        objItinerarioDetalle = clItinerarioDetalleInsert(
                            IdItinerario = nIdItinerario,
                            IdVehiculo = nIdVehiculo,
                            Fecha = LocalDateTime.now().toString().substring(0, 10),
                            IpOS = "Android " + Build.VERSION.RELEASE,
                            MqnVersion = "Version " + BuildConfig.VERSION_NAME,
                            Usuario = sUsuario,
                            Latitud = xLatitud,
                            Longitud = xLongitud
                        )
                    )
                }
                _itinerarioDetalle.value = ((result ?: (clItinerarioDetalleApi(
                    false, 0, LocalDate.now().toString().substring(0,10), "", "", "",
                    emptyList()
                ))))


                if (_itinerarioDetalle.value.success) {

                    val nTotal: Int = _itinerarioDetalle.value.data.count() - 1

                    val UserLocation: Location = Location("locationA")
                    val StoreLocation: Location = Location("locationB")
                    var distance: Float = 0.0f

                    for (i in 0..nTotal) {
                        UserLocation.setLatitude(xLatitud)
                        UserLocation.setLongitude(xLongitud)

                        StoreLocation.setLatitude(_itinerarioDetalle.value.data[i].Latitud.toDouble())
                        StoreLocation.setLongitude(_itinerarioDetalle.value.data[i].Longitud.toDouble())
                        distance = UserLocation.distanceTo(StoreLocation)
                        _itinerarioDetalle.value.data[i].Orden = distance.toInt()
                    }

                    //Guardamos en ROOM
                    for (i in 0..nTotal) {
                        repoDB.setItinerarioDetalle(
                            clItinerarioDetalleDB(
                                _itinerarioDetalle.value.data[i].IdItinerarioDetalle,
                                _itinerarioDetalle.value.data[i].IdItinerario,
                                _itinerarioDetalle.value.data[i].IdSucursal,
                                _itinerarioDetalle.value.data[i].Sucursal,
                                _itinerarioDetalle.value.data[i].Latitud,
                                _itinerarioDetalle.value.data[i].Longitud,
                                _itinerarioDetalle.value.data[i].IdVehiculo,
                                _itinerarioDetalle.value.data[i].Vehiculo,
                                _itinerarioDetalle.value.data[i].IdCliente,
                                _itinerarioDetalle.value.data[i].Cliente,
                                _itinerarioDetalle.value.data[i].CatEstatus,
                                _itinerarioDetalle.value.data[i].Estatus,
                                _itinerarioDetalle.value.data[i].Orden,
                                _itinerarioDetalle.value.data[i].FotosAntes,
                                _itinerarioDetalle.value.data[i].FotosDespues,
                                _itinerarioDetalle.value.data[i].FirmaDigital,
                                _itinerarioDetalle.value.data[i].RutaFirma,
                                _itinerarioDetalle.value.data[i].QR,
                                _itinerarioDetalle.value.data[i].Fecha,
                                _itinerarioDetalle.value.data[i].HoraAsignacion,
                                _itinerarioDetalle.value.data[i].HoraLlegada,
                                _itinerarioDetalle.value.data[i].HoraTermino,
                                _itinerarioDetalle.value.data[i].Horario,
                                _itinerarioDetalle.value.data[i].NumeroEmpleado,
                                _itinerarioDetalle.value.data[i].NombreEmpleado,
                                _itinerarioDetalle.value.data[i].TipoAsignacion,
                                _itinerarioDetalle.value.data[i].Umbral,
                                _itinerarioDetalle.value.data[i].EsNumeroEmpleadoRecoleccion,
                                _itinerarioDetalle.value.data[i].Eliminado
                            )
                        )
                    }
                }
            }
        }
    }
    fun setItinerarioEstatus(objItinerario: clItinerarioEstatusInsert){
        viewModelScope.launch(Dispatchers.IO){
            repoApi.setItinerarioEstatus(
                clItinerarioEstatusInsert(
                    IdItinerarioDetalle = objItinerario.IdItinerarioDetalle,
                    CatEstatus = objItinerario.CatEstatus,
                    QR = objItinerario.QR,
                    HoraAsignacion = objItinerario.HoraAsignacion,
                    HoraLlegada = objItinerario.HoraLlegada
                    )
            )
        }
    }
    fun deleteItinerarioBD(){
        repoDB.delItinerarioDetalle()
        _itinerarioDetalleDB.value = emptyList()
        _itinerarioDetalle.value = (clItinerarioDetalleApi(
            false, 0, "", "", "", "",
            emptyList()
        ))
    }
    fun setItinerarioDetalleEnCamino(nIdItinerarioDetalle: Int, sHoraAsignacion: String){
        repoDB.setItinerarioDetalleEnCamino(nIdItinerarioDetalle, sHoraAsignacion)
    }
    fun setItinerarioDetalleEnProceso(nIdItinerarioDetalle: Int, sHoraLlegada: String){
        repoDB.setItinerarioDetalleEnProceso(nIdItinerarioDetalle, sHoraLlegada)
    }

    fun setItinerarioDetalleQR(nIdItinerarioDetalle: Int){
        repoDB.setItinerarioDetalleQR(nIdItinerarioDetalle)
    }
}