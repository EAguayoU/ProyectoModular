package com.imbitbox.recolectora.views.recoleccion

import android.Manifest
import android.annotation.SuppressLint
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.components.AlertDialogOnly
import com.imbitbox.recolectora.components.BoxLoading
import com.imbitbox.recolectora.components.BtnIcon
import com.imbitbox.recolectora.components.BtnStandar
import com.imbitbox.recolectora.components.EdtField
import com.imbitbox.recolectora.components.VSpace
import com.imbitbox.recolectora.models.clUsuario.clUsuario
import com.imbitbox.recolectora.models.clUsuario.clUsuarioApi
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoApi
import com.imbitbox.recolectora.models.clVehiculo.clVehiculoKmApi
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.ui.theme.clrAzul
import com.imbitbox.recolectora.ui.theme.clrVerde
import com.imbitbox.recolectora.viewModel.clUsuarioViewModel
import com.imbitbox.recolectora.viewModel.clVehiculoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalComposeUiApi::class
)

@Composable
fun RegistroVehiculoView(navController: NavController, viewModel: clVehiculoViewModel, sUsuario: String){
    var bLoading by remember { mutableStateOf(value = false) }
    var bAlertaConsulta by remember { mutableStateOf(value = false) }
    //Verificar permiso de uso camara para QR
    val permisoCamara = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )
    val focusManager = LocalFocusManager.current
    var sKilometros by remember { mutableStateOf("") }
    val context = LocalContext.current
    val dataStore = clDataStore(context)
    val objDataVehiculo = dataStore.getStoreVehiculo.collectAsState(initial = "")
    val sDataUser = dataStore.getStoreUser.collectAsState(initial = "")
    var objUser by remember { mutableStateOf(clUsuario()) }
    val objVehiculo by viewModel.vehiculoApi.collectAsState(clVehiculoApi(false,0,"",",","", "", emptyList()))
    val objVehiculoKm by viewModel.vehiculoKmApi.collectAsState(clVehiculoKmApi(false,0,"",",","", "", emptyList()))
    val objGetVehiculoKm by viewModel.getvehiculoKmApi.collectAsState(clVehiculoKmApi(false,0,"",",","", "", emptyList()))
    Scaffold(
        //Barra
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("Registro de Vehículo",color = Color.White, fontSize = 25.sp)},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = clrVerde
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
            )
        }
    )
    {
        Box()
        {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.recoleccion_degradado),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 64.dp)
                )
            }
            Column(){
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp)
                        .weight(3f)
//                        .background(Color.Yellow)
                )
                {

                    BtnIcon(
                        modifier = Modifier
                            .background(clrAzul, shape = RoundedCornerShape(10.dp))
                            .size(80.dp),
                        painterIcon = R.drawable.codigo_qr
                    ) {
                        if (permisoCamara.status.isGranted) {
                            println("Tienes Permiso")
                            sKilometros = ""
                            navController.navigate("BarCodeScreen/${true}/${objUser.Usuario}")
                            dataStore.saveStoreVehiculo("")
                        } else {
                            println("No tienes Permiso")
                            permisoCamara.run { launchPermissionRequest() }
                        }
                    }
                    VSpace(20)
                    Image(
                        painter = painterResource(id = R.drawable.camion_recolector),
                        contentDescription = "",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    VSpace(20)
                    Text(if(objVehiculo.success) objVehiculo.data[0].Marca else "UNIDAD", fontSize = 20.sp, color = Color.Black)
                    Text(if(objVehiculo.success) objVehiculo.data[0].Modelo else "MODELO", fontSize = 20.sp, color = Color.Black)
                    Text(if(objVehiculo.success) objVehiculo.data[0].Placas else "PLACAS", fontSize = 20.sp, color = Color.Black)
                    Text(if(objVehiculo.success) objVehiculo.data[0].NumEco else "NUM.ECO", fontSize = 20.sp, color = Color.Black)
                    VSpace(20)
                    Image(
                        painter = painterResource(id = R.drawable.speedometer),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    VSpace(20)
                    EdtField(
                        label = "Kilometraje.",
                        color = Color.Black,
                        value = sKilometros,
                        keyboardType = KeyboardType.Number,
                        placeholder = "0",
                        modifier = Modifier.onKeyEvent {
                            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
                            {
                                focusManager.clearFocus(true)
                            }
                            false
                        }
                        ,
                        onValueChange = {
                            sKilometros = if (it == "")
                                ""
                            else if(it.contains(".")){
                                sKilometros
                            }
                            else if(it.contains(",")){
                                sKilometros
                            }
                            else if(it.contains("-")){
                                sKilometros
                            }
                            else if(it.contains(" ")){
                                sKilometros
                            }
                            else if(it.contains("\n")){
                                sKilometros
                            }
                            else{
                                it.trim()
                            }
                        })

                }

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp)
                        .weight(.3f)
//                        .background(Color.Cyan)

                ) {
                    BtnStandar(
                        sText = "Siguiente",
                        nFontSize = 30,
                        nRoundedCorner = 20,
                        containerColor = clrVerde,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        if(sKilometros != ""){
                            //CoroutineScope(Dispatchers.IO).launch {
                            if(objVehiculo.success) {
                                bLoading = true
                                viewModel.setVehiculoKm(
                                    objVehiculo.data[0].IdVehiculo,
                                    sKilometros.toInt(),
                                    objUser.Usuario
                                )
                            }
                            //}
                        }
                    }
                }
            }
        }

        if (bLoading) {
            BoxLoading(true, sText = "Cargando...")
        }

        if (bAlertaConsulta) {
            AlertDialogOnly(
                onConfirmation = { bAlertaConsulta = false },
                dialogTitle = "Error",
                dialogText = "No se pudo realizar la consulta, favor de verificar conexión",
                icon = Icons.Default.Warning
            )
        }
        /*LAUNCHED EFFECTS*/

        LaunchedEffect(key1 = objDataVehiculo.value) {
            if(objDataVehiculo.value != "") {
                bLoading = true
                viewModel.getVehiculofromDataset(objDataVehiculo.value)
            }

        }

        LaunchedEffect(key1 = objVehiculo) {
            if (objVehiculo.success  && objVehiculo.date != "") {
                bLoading = true
                dataStore.saveStoreVehiculo(Gson().toJson(objVehiculo.data[0]))
            }
            else if (!objVehiculo.success && objVehiculo.date != ""){
                bAlertaConsulta = true
                sKilometros = ""
                viewModel.delVehiculoApi()
            }
            bLoading = false
        }

        LaunchedEffect(key1 = objGetVehiculoKm) {
            if(objGetVehiculoKm.success && objGetVehiculoKm.date != "") {
                if (objGetVehiculoKm.data[0].KmInicial != 0) {
                    sKilometros = objGetVehiculoKm.data[0].KmInicial.toString()
                }
                else{
                    sKilometros = ""
                }
            }
            else{
                sKilometros = ""
            }
            bLoading = false
        }

        LaunchedEffect(key1 = objVehiculoKm ) {
            if(objVehiculoKm.success && objVehiculoKm.date != ""){
                navController.popBackStack("Menu", false)
                viewModel.delVehiculoKm()
            }
            else if(!objVehiculoKm.success && objVehiculoKm.date != ""){
                bAlertaConsulta = true
                sKilometros = ""
                viewModel.delVehiculoKm()
            }
            bLoading = false
        }
        LaunchedEffect(key1 = sDataUser.value) {
            if (sDataUser.value != "") {
                objUser = Gson().fromJson(sDataUser.value, clUsuario::class.java)
            }
        }
    }
}