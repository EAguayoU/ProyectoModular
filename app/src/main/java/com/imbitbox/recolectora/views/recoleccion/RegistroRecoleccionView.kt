package com.imbitbox.recolectora.views.recoleccion

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.imbitbox.recolectora.BuildConfig
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.classes.clLocation
import com.imbitbox.recolectora.components.AlertDialogOnly
import com.imbitbox.recolectora.components.BoxLoading
import com.imbitbox.recolectora.components.BtnStandar
import com.imbitbox.recolectora.components.EdtField
import com.imbitbox.recolectora.components.HSpace
import com.imbitbox.recolectora.components.VSpace
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import com.imbitbox.recolectora.models.clItinerario.clItinerarioEstatusInsert
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clItinerarioTerminado
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionSaveAll
import com.imbitbox.recolectora.models.clUsuario.clUsuario
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.ui.theme.clrAzul
import com.imbitbox.recolectora.ui.theme.clrVerde
import com.imbitbox.recolectora.viewModel.clItinerarioDetalleViewModel
import com.imbitbox.recolectora.viewModel.clRegistroRecoleccionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun RegistroRecoleccionView(
    navController: NavController,
    nIdItinerarioDetalle: Int,
    vmItinerarioDetalle: clItinerarioDetalleViewModel,
    vmRegistroRecoleccion: clRegistroRecoleccionViewModel
) {
    //Alerta
    var bAlerta by remember { mutableStateOf(value = false) }
    var bAlertaGuardado by remember { mutableStateOf(value = false) }
    var sTitleAlerta by remember { mutableStateOf(value = "") }
    var sTextAlerta by remember { mutableStateOf(value = "") }
    //Variables Botones
    var bLoading by rememberSaveable { mutableStateOf(value = false) }
    var bLlegada by remember { mutableStateOf(value = false) }
    var bEscaneaQr by rememberSaveable { mutableStateOf(value = false) }
    var bFotos by rememberSaveable { mutableStateOf(value = true) }
    var bNumEmpleado by rememberSaveable { mutableStateOf(value = true) }
    var bFirma by rememberSaveable { mutableStateOf(value = true) }
    var bCantidad by rememberSaveable { mutableStateOf(value = true) }
    //DataSet
    val context = LocalContext.current
    val dataStore = clDataStore(context)
    val objDataSucursal = dataStore.getStoreSucursal.collectAsState(initial = "")
    val sDataUser = dataStore.getStoreUser.collectAsState(initial = "")
    var objUser by remember { mutableStateOf(clUsuario()) }

    var sTipoImagen: String = ""
    var nImgAntesDB by remember { mutableStateOf(0) }
    var nImgDespuesDB by remember { mutableStateOf(0) }

    val objItinerarioDetalleDB by vmItinerarioDetalle.objItinerarioDetalleDB.collectAsState(
        clItinerarioDetalleDB(
            0,
            0,
            0,
            "",
            "",
            "",
            0,
            "",
            0,
            "",
            0,
            "",
            0,
            0,
            0,
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            false
        )
    )
    val objItinerarioGuardado by vmRegistroRecoleccion.itinerarioTerminado.collectAsState(
        clItinerarioTerminado(
            false,
            0,
            "",
            ",",
            "",
            "",
        )
    )
    val objImagenesItinerario by vmRegistroRecoleccion.imgItinerario.collectAsState()
    val objRegistroRecoleccionDetalle by vmRegistroRecoleccion.registroDetalleSave.collectAsState()

    val focusManager = LocalFocusManager.current
    var sNumEmpleado by rememberSaveable { mutableStateOf("") }
    var sNombreEmpleado by rememberSaveable { mutableStateOf("") }
    var nDistancia by rememberSaveable {
        mutableStateOf(-1)
    }
    val clLocation by remember {
        mutableStateOf(clLocation())
    }
    //Permisos
    val permisoCamara = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    Scaffold(
        //Barra
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    "Datos Recolección",
                    color = Color.White,
                    fontSize = 25.sp
                )
            },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = clrVerde
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("DatosSucursal/${nIdItinerarioDetalle}", false) }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                })
        }) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(top = 70.dp)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                HSpace(20)
                Text(text = "Umbral.", modifier = Modifier.weight(.8f))
                Text(
                    text = String.format(Locale.US, "%,d", objItinerarioDetalleDB.Umbral ?: "0"),
                    modifier = Modifier.weight(.9f)
                )
                Text(text = "Distancia.", modifier = Modifier.weight(1f))
                Text(text = nDistancia.toString(), modifier = Modifier.weight(.6f))
            }
            VSpace(15)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Marcar Llegada",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(3f)
                )
                HSpace(50)
                BtnStandar(
                    nRoundedCorner = 20,
                    modifier = Modifier
                        .size(width = 120.dp, height = 60.dp),
                    modifierIcon = Modifier
                        .size(50.dp)
                        .weight(2f),
                    painterIcon = R.drawable.localizacion,
                    containerColor = clrAzul,
                    bIsEnable = bLlegada
                ) {

                    CoroutineScope(Dispatchers.Main).launch {
                        bLoading = true
                        dataStore.saveStoreSucursal("")
                        nDistancia = -1
                        clLocation.pLocation(context)
                        delay(2000)
                        nDistancia = vmRegistroRecoleccion.getDistance(
                            clLocation.currentLocation.Latitude,
                            clLocation.currentLocation.Longitude,
                            objItinerarioDetalleDB.Latitud.toDouble(),
                            objItinerarioDetalleDB.Longitud.toDouble(),
                        )

                        if (nDistancia != -1) {
                            bLoading = false
                        }
                    }

                }
            }
            VSpace(20)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Escanea QR",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(3f)
                )
                HSpace(50)
                BtnStandar(
                    nRoundedCorner = 20,
                    modifier = Modifier
                        .size(width = 120.dp, height = 60.dp),
                    modifierIcon = Modifier
                        .size(50.dp)
                        .weight(2f),
                    painterIcon = R.drawable.codigo_qr,
                    containerColor = clrAzul,
                    bIsEnable = bEscaneaQr
                ) {
                    if (permisoCamara.status.isGranted) {
                        println("Tienes Permiso")
                        navController.navigate("BarCodeScreen/${false}/${nIdItinerarioDetalle}")
                        dataStore.saveStoreSucursal("")
                    } else {
                        println("No tienes Permiso")
                        permisoCamara.run { launchPermissionRequest() }
                    }
                }
            }
            VSpace(20)
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center, modifier = Modifier.weight(3f)
                ) {
                    Text(
                        text = "Fotos Antes",
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                    Text(text = "Tomadas = ${nImgAntesDB}", fontSize = 15.sp, color = clrAzul)
                }
                HSpace(50)
                BtnStandar(
                    nRoundedCorner = 20,
                    modifier = Modifier
                        .size(width = 120.dp, height = 60.dp),
                    modifierIcon = Modifier
                        .size(50.dp)
                        .weight(2f),
                    painterIcon = R.drawable.foto,
                    containerColor = clrAzul,
                    bIsEnable = bFotos
                ) {
                    sTipoImagen = "ANTES"
                    if (permisoCamara.status.isGranted) {
                        println("Tienes Permiso")
                        navController.navigate("CameraScreen/${objItinerarioDetalleDB.IdItinerarioDetalle}/${sTipoImagen}")
                    } else {
                        println("No tienes Permiso")
                        permisoCamara.run { launchPermissionRequest() }
                    }
                }
            }
            VSpace(20)
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center, modifier = Modifier.weight(3f)
                ) {
                    Text(
                        text = "Fotos Después",
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                    Text(text = "Tomadas = ${nImgDespuesDB}", fontSize = 15.sp, color = clrAzul)
                }
                HSpace(50)
                BtnStandar(
                    nRoundedCorner = 20,
                    modifier = Modifier
                        .size(width = 120.dp, height = 60.dp),
                    modifierIcon = Modifier
                        .size(50.dp)
                        .weight(2f),
                    painterIcon = R.drawable.foto,
                    containerColor = clrAzul,
                    bIsEnable = bFotos
                ) {
                    sTipoImagen = "DESPUES"
                    if (permisoCamara.status.isGranted) {
                        println("Tienes Permiso")
                        navController.navigate("CameraScreen/${objItinerarioDetalleDB.IdItinerarioDetalle}/${sTipoImagen}")
                    } else {
                        println("No tienes Permiso")
                        permisoCamara.run { launchPermissionRequest() }
                    }
                }
            }
            VSpace(20)
            EdtField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
                    {
                        focusManager.clearFocus(true)
                    }
                    false
                },
                label = "Número Empleado",
                color = Color.Black,
                keyboardType = KeyboardType.Number,
                placeholder = "0",
                value = sNumEmpleado,
                bReadOnly = if (objItinerarioDetalleDB.EsNumeroEmpleadoRecoleccion) {
                    bNumEmpleado
                } else {
                    !bNumEmpleado
                },

                onValueChange = {
                    sNumEmpleado = if (it == "")
                        ""
                    else if(it.contains(".")){
                        sNumEmpleado
                    }
                    else if(it.contains(",")){
                        sNumEmpleado
                    }
                    else if(it.contains("-")){
                        sNumEmpleado
                    }
                    else if(it.contains(" ")){
                        sNumEmpleado
                    }
                    else if(it.contains("\n")){
                        sNumEmpleado
                    }
                    else{
                        it.trim()
                    }
                })
            VSpace(20)
            EdtField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
                label = "Nombre Empleado",
                color = Color.Black,
                keyboardType = KeyboardType.Text,
                value = sNombreEmpleado,
                onValueChange = { sNombreEmpleado = it })
            VSpace(20)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Firma Digital",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(3f)
                )
                HSpace(50)
                BtnStandar(
                    nRoundedCorner = 20,
                    modifier = Modifier
                        .size(width = 120.dp, height = 60.dp),
                    modifierIcon = Modifier
                        .size(50.dp)
                        .weight(2f),
                    painterIcon = R.drawable.firma,
                    containerColor = clrAzul,
                    bIsEnable = bFirma
                ) {
                    navController.navigate("SignaturePad/${objItinerarioDetalleDB.IdItinerarioDetalle}")
                }
            }
            VSpace(20)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center, modifier = Modifier.weight(3f)
                ) {
                    Text(
                        text = "Cantidad Recolectada",
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                    Text(text = "Completado", fontSize = 15.sp, color = clrAzul)
                }
                HSpace(50)
                BtnStandar(
                    nRoundedCorner = 20,
                    modifier = Modifier
                        .size(width = 120.dp, height = 60.dp),
                    modifierIcon = Modifier
                        .size(50.dp)
                        .weight(2f),
                    painterIcon = R.drawable.recolecta,
                    containerColor = clrAzul,
                    bIsEnable = bCantidad
                ) {
                    navController.navigate("RegistroRecoleccionDetalle/${objItinerarioDetalleDB.IdSucursal}/${objItinerarioDetalleDB.IdItinerarioDetalle}")
                }
            }

            VSpace(30)
            BtnStandar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp),
                sText = "Finalizar",
                nFontSize = 25,
                nRoundedCorner = 20,
                containerColor = clrVerde
            ) {
                bLoading = true
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        vmRegistroRecoleccion.getRegistroRecoleccionDetalle(nIdItinerarioDetalle)
                        delay(100)
                        vmRegistroRecoleccion.setDetalleListToSave()
                        vmRegistroRecoleccion.getImgItinerarioByID(nIdItinerarioDetalle)
                        delay(100)
                        vmRegistroRecoleccion.setRoomtoObjImagenes()
                        delay(100)

                        if (objItinerarioDetalleDB.FirmaDigital == "SIN FIRMA") {
                            bAlerta = true
                            sTitleAlerta = "Firma"
                            sTextAlerta = "Se necesita una firma para continuar"
                        } else if (objImagenesItinerario.Imagenes.isEmpty()) {
                            bAlerta = true
                            sTitleAlerta = "Imagen"
                            sTextAlerta = "Se requiere imagenes para continuar"
                        } else if (objRegistroRecoleccionDetalle.Detalle.isEmpty()) {
                            bAlerta = true
                            sTitleAlerta = "Recolección"
                            sTextAlerta = "Agrega una recolección para continuar"
                        } else {

                            vmRegistroRecoleccion.saveItinerarioDetalle(
                                objItinerario = clRegistroRecoleccionSaveAll(
                                    IdItinerarioDetalle = nIdItinerarioDetalle,
                                    HoraTermino = LocalTime.now().toString().substring(0, 8),
                                    NumeroEmpleado = if (sNumEmpleado != "") sNumEmpleado.toInt() else 0,
                                    NombreEmpleado = sNombreEmpleado,
                                    FirmaDigital = objItinerarioDetalleDB.FirmaDigital,
                                    Usuario = objUser.IdUsuario,
                                    IpOS = "Android " + Build.VERSION.RELEASE,
                                    MqnVersion = "Version " + BuildConfig.VERSION_NAME,
                                    Imagenes = objImagenesItinerario.Imagenes,
                                    Detalle = objRegistroRecoleccionDetalle.Detalle
                                ),
                                objItinerarioDetalleDB
                            )
                        }
                    }
                } catch (error: Exception) {
                    println("Ocurrio un error al guardar")
                    bLoading = false
                }
            }
        }
    }
    if (bAlerta) {
        AlertDialogOnly(
            onConfirmation = {
                bAlerta = false
                bLoading = false
            },
            dialogTitle = sTitleAlerta,
            dialogText = sTextAlerta,
            icon = Icons.Default.Warning
        )
    }
    if (bAlertaGuardado) {
        AlertDialogOnly(
            onConfirmation = {
                bAlertaGuardado = false
                bLoading = false
//                navController.navigate("Itinerario/${objItinerarioDetalleDB.IdItinerario}/${objItinerarioDetalleDB.IdVehiculo}/${objUser.Usuario}/${objUser.IdUsuario}")
                navController.popBackStack("Itinerario",false)
            },
            dialogTitle = sTitleAlerta,
            dialogText = sTextAlerta,
            icon = Icons.Default.Warning
        )
    }

    if (bLoading) {
        BoxLoading(true, sText = "Cargando...")
    }
    LaunchedEffect(Unit) {
        vmItinerarioDetalle.getItinerarioDetalleByID(nIdItinerarioDetalle)
        CoroutineScope(Dispatchers.IO).launch {
            nImgAntesDB = vmRegistroRecoleccion.getImgItinerarioByTipoDB(
                objItinerarioDetalleDB.IdItinerarioDetalle,
                "ANTES"
            )
            nImgDespuesDB = vmRegistroRecoleccion.getImgItinerarioByTipoDB(
                objItinerarioDetalleDB.IdItinerarioDetalle,
                "DESPUES"
            )
        }
    }

    LaunchedEffect(key1 = objItinerarioDetalleDB) {
        if (objItinerarioDetalleDB.CatEstatus < 35) {
            bLlegada = true
        } else {
            bLlegada = false
            if (objItinerarioDetalleDB.QR == true) {
                bEscaneaQr = false
                bFotos = true
                bFirma = true
                bCantidad = true
                bNumEmpleado = true
            } else {
                bEscaneaQr = true
            }
        }
    }
    LaunchedEffect(key1 = nDistancia) {
        if (nDistancia >= 0) {
            if (nDistancia <= objItinerarioDetalleDB.Umbral) {
                bLlegada = false
                bEscaneaQr = true
                CoroutineScope(Dispatchers.IO).launch {
                    vmItinerarioDetalle.setItinerarioDetalleEnProceso(
                        objItinerarioDetalleDB.IdItinerarioDetalle,
                        LocalTime.now().toString().substring(0, 8)
                    )
                }
                vmItinerarioDetalle.setItinerarioEstatus(
                    objItinerario = clItinerarioEstatusInsert(
                        nIdItinerarioDetalle,
                        35,
                        HoraLlegada = LocalTime.now().toString().substring(0, 8)
                    )
                )
            } else {
                bAlerta = true
                sTitleAlerta = "Ubicación Incorrecta"
                sTextAlerta = "La distancia con la sucursal es mayor a la permitida"
            }
            bLoading = false
        }
    }
    LaunchedEffect(key1 = objDataSucursal.value) {
        if (objDataSucursal.value != "") {
            if (vmRegistroRecoleccion.getSucursal(
                    objDataSucursal.value,
                    objItinerarioDetalleDB.IdSucursal
                )
            ) {
                bEscaneaQr = false
                bFotos = true
                bFirma = true
                bCantidad = true
                bNumEmpleado = true
                CoroutineScope(Dispatchers.IO).launch {
                    vmItinerarioDetalle.setItinerarioDetalleQR(nIdItinerarioDetalle)
                }
                vmItinerarioDetalle.setItinerarioEstatus(
                    objItinerario = clItinerarioEstatusInsert(
                        IdItinerarioDetalle = nIdItinerarioDetalle,
                        CatEstatus = 35,
                        QR = true
                    )
                )

            } else {
                bAlerta = true
                sTitleAlerta = "Sucursal Incorrecta"
                sTextAlerta =
                    "El código escaneado no pertenece a la sucursal ${objItinerarioDetalleDB.Sucursal}"
            }
        }
    }
    LaunchedEffect(key1 = sDataUser.value) {
        if (sDataUser.value != "") {
            objUser = Gson().fromJson(sDataUser.value, clUsuario::class.java)
        }
    }
    LaunchedEffect(key1 = objItinerarioGuardado) {
        if (objItinerarioGuardado.success) {

            CoroutineScope(Dispatchers.IO).launch {
                //Guardamos registros de termino en ROOM
                vmRegistroRecoleccion.setItinerarioDetalleTermino(
                    nIdItinerarioDetalle,
                    LocalTime.now().toString().substring(0, 8),
                    if (sNumEmpleado != "") sNumEmpleado.toInt() else 0,
                    sNombreEmpleado
                )
                dataStore.saveStoreSucursal("")
                //Agregamos bit de Elimiado en ROOM
                vmRegistroRecoleccion.setItinerarioDetalleEliminado(nIdItinerarioDetalle)
                vmRegistroRecoleccion.setImgItinerarioEliminado(nIdItinerarioDetalle)
                vmRegistroRecoleccion.setRegistroRecoleccionDetalleEliminado(
                    nIdItinerarioDetalle
                )
                vmRegistroRecoleccion.delObjItinerarioTerminado()
                bLoading = false
            }
            navController.navigate("Itinerario")


        }
        else if(!objItinerarioGuardado.success && objItinerarioGuardado.date != "") {
            bLoading = false
            bAlertaGuardado = true
            sTitleAlerta = "Error"
            sTextAlerta = "Ocurrio un error al momento de guardar. Intente terminar la recolección más tarde."
            CoroutineScope(Dispatchers.IO).launch {
                vmRegistroRecoleccion.setItinerarioDetalleTermino(
                    nIdItinerarioDetalle,
                    LocalTime.now().toString().substring(0, 8),
                    if (sNumEmpleado != "") sNumEmpleado.toInt() else 0,
                    sNombreEmpleado
                )
            }
            vmRegistroRecoleccion.delObjItinerarioTerminado()
        }
        else if(objItinerarioGuardado.message != "") {
            bLoading = false
            bAlertaGuardado = true
            sTitleAlerta = "Error"
            sTextAlerta =
                "No fue posible subir las evidencias(ftp). Intente terminar la recolección más tarde."
            CoroutineScope(Dispatchers.IO).launch {
                vmRegistroRecoleccion.setItinerarioDetalleTermino(
                    nIdItinerarioDetalle,
                    LocalTime.now().toString().substring(0, 8),
                    sNumEmpleado.toInt(),
                    sNombreEmpleado
                )
            }
            vmRegistroRecoleccion.delObjItinerarioTerminado()
        }
    }
}
