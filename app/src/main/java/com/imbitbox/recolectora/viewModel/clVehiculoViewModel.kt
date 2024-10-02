package com.imbitbox.recolectora.viewModel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.imbitbox.recolectora.BuildConfig
import com.imbitbox.recolectora.models.clVehiculo.clVehiculo
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoApi
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKm
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKmApi
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKmInsert
import com.imbitbox.recolectora.repository.clVehiculoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class clVehiculoViewModel @Inject constructor(private val repo: clVehiculoRepository) :
    ViewModel() {
    private val _vehiculoApi = MutableStateFlow<clVehiculoApi>(
        clVehiculoApi(
            false,
            0,
            "",
            "",
            "",
            "",
            emptyList()
        )
    )
    private val _vehiculoKmApi = MutableStateFlow<clVehiculoKmApi>(
        clVehiculoKmApi(
            false,
            0,
            "",
            "",
            "",
            "",
            emptyList()
        )
    )
    private val _getVehiculoKmApi = MutableStateFlow<clVehiculoKmApi>(
        clVehiculoKmApi(
            false,
            0,
            "",
            "",
            "",
            "",
            emptyList()
        )
    )

    val vehiculoApi = _vehiculoApi.asStateFlow()
    val vehiculoKmApi = _vehiculoKmApi.asStateFlow()
    val getvehiculoKmApi = _getVehiculoKmApi.asStateFlow()
    fun getVehiculo(nIdVehiculo: Int) {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repo.getVehiculo(objVehiculo = clVehiculo(IdVehiculo = nIdVehiculo))
            }
            if(result != null){
                if(result.success){
                    _vehiculoApi.value = result
                    val resultKm = withContext(Dispatchers.IO) {
                        repo.getVehiculoKm(
                            objVehiculo = clVehiculoKm(
                                IdVehiculo = nIdVehiculo,
                                Fecha = LocalDateTime.now().toString().substring(0, 10)
                            )
                        )
                    }
                    if(resultKm != null){
                        if(resultKm.success){
                            _getVehiculoKmApi.value = resultKm
                        }
                    }
                    else {
                        _getVehiculoKmApi.value = clVehiculoKmApi(
                            false, 0, LocalDate.now().toString().substring(0,10), "", "", "",
                            emptyList()
                        )
                    }
                }
                else{
                    _vehiculoApi.value = clVehiculoApi(
                        false, 0, LocalDate.now().toString().substring(0,10), "", "", "",
                        emptyList()
                    )
                }

            }
            else{
                _vehiculoApi.value = clVehiculoApi(
                    false, 0, LocalDate.now().toString().substring(0,10), "", "", "",
                    emptyList()
                )
            }


        }
    }

    fun setVehiculo(sVehiculo: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repo.setVehiculo(sVehiculo)
            }
            _vehiculoApi.value = (result ?: (clVehiculoApi(
                false, 0, LocalDateTime.now().toString().substring(0, 10), "", "", "",
                emptyList()
            )))
            val resultKm = withContext(Dispatchers.IO) {
            repo.getVehiculoKm(
                objVehiculo = clVehiculoKm(
                    IdVehiculo = _vehiculoApi.value.data[0].IdVehiculo,
                    Fecha = LocalDateTime.now().toString().substring(0, 10)
                )
            )
        }
            _getVehiculoKmApi.value = (resultKm ?: (clVehiculoKmApi(
                false, 0, "", "", "", "",
                emptyList()
            )))
        }
    }

    fun validateJson(sJson: String): Boolean {
        try {
            Gson().fromJson(sJson, clVehiculo::class.java)
            return true
        } catch (error: Throwable) {
            return false
        }
    }

    fun setVehiculoKm(nIdVehiculo: Int, nKmVehiculo: Int, sUsuario: String) {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repo.setVehiculoKm(
                    objVehiculo = clVehiculoKmInsert(
                        IdVehiculo = nIdVehiculo,
                        KmInicial = nKmVehiculo,
                        KmFinal = 0,
                        Fecha = LocalDateTime.now().toString().substring(0, 10),
                        IpOS = "Android " + Build.VERSION.RELEASE,
                        MqnVersion = "Version " + BuildConfig.VERSION_NAME,
                        Usuario = sUsuario
                    )
                )
            }
            if(result != null){
                if(result.success){
                    _vehiculoKmApi.value = result
                }
                else{
                    _vehiculoKmApi.value = clVehiculoKmApi(
                        false, 0, LocalDate.now().toString().substring(0,10), "", "", "",
                        emptyList()
                    )
                }
            }
            else{
                _vehiculoKmApi.value = clVehiculoKmApi(
                    false, 0, LocalDate.now().toString().substring(0,10), "", "", "",
                    emptyList()
                )
            }
            //delay(3000)

        }
    }

    fun delVehiculoKm() {
        _vehiculoKmApi.value = (clVehiculoKmApi(
            false, 0, "", "", "", "",
            emptyList()
        ))
    }
    fun delVehiculoApi() {
        _vehiculoApi.value = clVehiculoApi(
            false, 0, "", "", "", "",
            emptyList()
        )
    }


    fun getVehiculofromDataset(sDataSet: String) {
        var bUpdate: Boolean = false

        if (vehiculoApi.value.success) {
            if (sDataSet != Gson().toJson(vehiculoApi.value.data[0])) {
                bUpdate = true
            }
        } else {
            bUpdate = true
        }

        if (bUpdate) {
            val objQrCode = sDataSet.split("%")
            if (objQrCode.count() == 2) {
                if (objQrCode[1].substring(0, 1) == "V") {
                    println("Es un vehiculo")
                    getVehiculo(objQrCode[0].toInt())
                } else {
                    println("No tiene el formato de un Qr Vehiculo")
                }
            } else if (validateJson(sDataSet)) {
                setVehiculo(sDataSet)
            } else {
                println("No tiene el formato de un Qr Vehiculo")
            }
        }
    }
}