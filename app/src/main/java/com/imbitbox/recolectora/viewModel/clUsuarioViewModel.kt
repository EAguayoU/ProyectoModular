package com.imbitbox.recolectora.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imbitbox.recolectora.models.clUsuario.clUsuario
import com.imbitbox.recolectora.models.clUsuario.clUsuarioApi
import com.imbitbox.recolectora.repository.clUsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class clUsuarioViewModel @Inject constructor(private val repo: clUsuarioRepository): ViewModel() {
    private val _usuario = MutableStateFlow<clUsuarioApi>(clUsuarioApi(false,0,"","","","",
        emptyList()))
    val usuario = _usuario.asStateFlow()
    fun getUsuarios(sUsuario: String, sPassword: String){


        viewModelScope.launch {
            val result =   withContext(Dispatchers.IO){
                 repo.getUsuario(objUsuario = clUsuario(Usuario = sUsuario, Password = sPassword))
                //delay(3000)

            }
            delay(3000)
            _usuario.value = (result ?: (clUsuarioApi(false,0,"","","","",
                emptyList())))
        }
    }
    fun delUsuario(){
        _usuario.value = clUsuarioApi(false,0,"","","","",
            emptyList())
    }
}