package com.imbitbox.recolectora.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imbitbox.recolectora.models.clItinerario.clItinerario
import com.imbitbox.recolectora.models.clItinerario.clItinerarioApi
import com.imbitbox.recolectora.repository.clItinerarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class clItinerarioViewModel @Inject constructor(private val repo: clItinerarioRepository): ViewModel() {
    private val _itinerario = MutableStateFlow<clItinerarioApi>(
        clItinerarioApi(false,0,"","","","",
        emptyList())
    )
    val itinerario = _itinerario.asStateFlow()
    fun getItinerarioEmpleado(nIdEmpleado : Int){


        viewModelScope.launch {
            val result =   withContext(Dispatchers.IO){
                repo.getItinerarioEmpleado(objItinerario = clItinerario(IdEmpleado = nIdEmpleado, Eliminado = false))

            }
            _itinerario.value = ((result ?: (clItinerarioApi(false,0,LocalDate.now().toString().substring(0,10),"","","",
                emptyList()))))
        }
    }

//    fun delItinerarioEmpleado(){
//        _itinerario.value =  (clItinerarioApi(false,0,"","","","",
//            emptyList()))
//    }
}