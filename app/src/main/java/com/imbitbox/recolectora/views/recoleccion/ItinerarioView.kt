package com.imbitbox.recolectora.views.recoleccion

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.imbitbox.recolectora.BuildConfig
import com.imbitbox.recolectora.classes.clLocation
import com.imbitbox.recolectora.components.AlertDialogOnly
import com.imbitbox.recolectora.components.AlertDialogYesNo
import com.imbitbox.recolectora.components.BoxLoading
import com.imbitbox.recolectora.components.BtnStandar
import com.imbitbox.recolectora.components.NoDataView
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import com.imbitbox.recolectora.models.clItinerario.clItinerario
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleApi
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
import java.time.LocalDate
import java.time.LocalTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItinerarioView(
    navController: NavController,
    viewModel: clItinerarioDetalleViewModel,
    vmRegistroRecoleccion: clRegistroRecoleccionViewModel,
//    nIdRuta: Int,
//    nIdItinerario: Int,
//    nIdVehiculo: Int,
//    sUsuario: String,
//    nIdUsuario: Int
) {
    val clLocation by remember {
        mutableStateOf(clLocation())
    }
    var nIdItinerarioDetalle by remember { mutableStateOf(0) }
    val objItinerario by viewModel.itinerarioDetalle.collectAsState(
        clItinerarioDetalleApi(
            false,
            0,
            "",
            ",",
            "",
            "",
            emptyList()
        )
    )
    val objItinerarioGuardado by vmRegistroRecoleccion.itinerarioTerminado.collectAsState(
        clItinerarioTerminado(
            false,
            0,
            "",
            "",
            "",
            "",
        )
    )
    val objItinerarioDetalleDB by viewModel.objItinerarioDetalleDB.collectAsState(clItinerarioDetalleDB())
    val objImagenesItinerario by vmRegistroRecoleccion.imgItinerario.collectAsState()
    val objRegistroRecoleccionDetalle by vmRegistroRecoleccion.registroDetalleSave.collectAsState()


    var sTitleAlerta by remember { mutableStateOf(value = "") }
    var sTextAlerta by remember { mutableStateOf(value = "") }
    var bAlertaGuardado by remember { mutableStateOf(value = false) }
    var bLoading by remember { mutableStateOf(value = false) }
    var bAlerta by remember { mutableStateOf(value = false) }
    var bAlertaSucursal by remember { mutableStateOf(value = false) }
    var bAlertaConsulta by remember { mutableStateOf(value = false) }
    var bAlertaTermino by remember { mutableStateOf(value = false) }
    var bAlertaCapturarTodas by remember { mutableStateOf(value = false) }
    var bGuardadoCompleto by remember { mutableStateOf(value = false) }
    val Context = LocalContext.current
    val dataStore = clDataStore(Context)
    var objUser by remember { mutableStateOf(clUsuario()) }
    var objRuta by remember { mutableStateOf(clItinerario()) }
    val sDataUser = dataStore.getStoreUser.collectAsState(initial = "")
    val sDataRuta = dataStore.getStoreRuta.collectAsState(initial = "")

    Scaffold(
        //Barra
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Itinerario del día", color = Color.White, fontSize = 25.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = clrVerde
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("Menu")
                        {
                            popUpTo(0)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        for (i in 0..objItinerario.data.count()-1){
                            if(objItinerario.data[i].CatEstatus == 36){
                                bAlertaGuardado = true
                                sTitleAlerta = "Generar Itinerario"
                                sTextAlerta = "Para generar un nuevo itinerario se necesita subir las evidencias pendientes"
                            }
                        }
                        if(!bAlertaGuardado) {
                            bAlerta = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            )
        }
    )
    {
        if (objItinerario.success) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 80.dp, bottom = 90.dp)
                .background(color = Color(0xffeeeefb))
        ) {

                items(objItinerario.data.sortedBy { it.Orden }) { item ->
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .shadow(5.dp, shape = RoundedCornerShape(10.dp))

                        .border(
                            BorderStroke(
                                2.dp, if (item.Estatus == "PENDIENTE") {
                                    Color.Green
                                } else if (item.Estatus == "TERMINO") {
                                    Color.Red
                                } else {
                                    Color.Yellow
                                }
                            ), shape = RoundedCornerShape(10.dp)
                        )
                        .background(Color.White, RoundedCornerShape(10.dp))

                        .selectable(
                            selected = item.IdItinerarioDetalle != 0,
                            onClick = {
                                if (item.CatEstatus == 33) {
                                    bAlertaSucursal = true
                                    nIdItinerarioDetalle = item.IdItinerarioDetalle
                                } else if (item.CatEstatus == 34 || item.CatEstatus == 35) {
                                    nIdItinerarioDetalle = item.IdItinerarioDetalle
                                    navController.navigate("DatosSucursal/${item.IdItinerarioDetalle}")
                                } else if (item.CatEstatus == 36) {
                                    nIdItinerarioDetalle = item.IdItinerarioDetalle
                                    bAlertaTermino = true
                                }
                            }

                        )
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .weight(4f)
                            .padding(10.dp)
                    )
                    {
                        Text(
                            text = item.Sucursal,
                            fontStyle = FontStyle.Italic,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                        Text(
                            text = item.Cliente,
                            color = Color.Black
                        )
                        Row(Modifier.fillMaxWidth()) {

                            Text(
                                text = item.Estatus,
                                color = if (item.Estatus == "PENDIENTE") {
                                    clrVerde
                                }
                                else if(item.Estatus == "TERMINO"){
                                    Color.Red
                                }
                                else {
                                    Color(0xFFBBBB62)

                                }
                            )
                            Text(
                                text = item.Horario,
                                color = clrAzul,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .weight(4f)

                            )
                        }

                    }
                }
            }
        }
        } else if (!objItinerario.success && objItinerario.date != "") {
            NoDataView()
        }
        else{
            BoxLoading(true, sText = "Cargando...")
        }

    }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(bottom = 10.dp, top = 10.dp)
    ) {

        Row (modifier = Modifier.padding(horizontal = 10.dp)) {
            Text(
                text = "Latitud: ${clLocation.currentLocation.Latitude}",
                color = Color.Black,
                fontSize = 11.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            Text(
                text = "Longitud: ${clLocation.currentLocation.Longitude}",
                color = Color.Black,
                fontSize = 11.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
            Text(
                text = "Cantidad: " + objItinerario.data.count().toString(),
                color = Color.Black,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
        BtnStandar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            sText = "Capturar",
            nFontSize = 25,
            containerColor = clrVerde,
            nRoundedCorner = 30,
        ) {
            bAlertaCapturarTodas = true
        }
    }

    if(bAlerta){
        AlertDialogYesNo(
            onDismissRequest = { bAlerta = false },
            onConfirmation = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteItinerarioBD()
                    if(clLocation.currentLocation.Latitude != 0.0) {
                        viewModel.getItinerarioDetalle(
                            objRuta.IdItinerario,
                            objRuta.IdVehiculo,
                            objUser.Usuario,
                            clLocation.currentLocation.Latitude,
                            clLocation.currentLocation.Longitude
                        )
                    }
                    else{
                        viewModel.getItinerarioDetalle(
                            objRuta.IdItinerario,
                            objRuta.IdVehiculo,
                            objUser.Usuario,
                            20.6691577,
                            -103.2334544
                        )
                    }
                    bAlerta = false
                }
            },
            dialogTitle = "Generar Itinerario",
            dialogText = "Estas seguro de volver a generar el Itinerario",
            icon = Icons.Default.Warning
        )
    }
    if(bAlertaSucursal){
        AlertDialogYesNo(
            onDismissRequest = { bAlertaSucursal = false },
            onConfirmation = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.setItinerarioDetalleEnCamino(
                        nIdItinerarioDetalle,
                        LocalTime
                            .now()
                            .toString()
                            .substring(0, 8)
                    )
                }
                viewModel.setItinerarioEstatus(objItinerario = clItinerarioEstatusInsert(nIdItinerarioDetalle, 34, HoraAsignacion = LocalTime.now().toString().substring(0,8)))
                navController.navigate("DatosSucursal/${nIdItinerarioDetalle}")
            },
            dialogTitle = "Acudir a Sucursal",
            dialogText = "¿Desea dirigirse hacia esta sucursal ahora?",
            icon =  Icons.Filled.Notifications
        )
    }
    if(bAlertaTermino){
        AlertDialogYesNo(
            onDismissRequest = {
                navController.navigate("DatosSucursal/${nIdItinerarioDetalle}")
                bAlertaTermino = false
                               },
            onConfirmation = {
                CoroutineScope(Dispatchers.Main).launch {
                    bLoading = true
                viewModel.getItinerarioDetalleByID(nIdItinerarioDetalle)
                    delay(100)
                    vmRegistroRecoleccion.getRegistroRecoleccionDetalle(nIdItinerarioDetalle)
                    delay(100)
                    vmRegistroRecoleccion.setDetalleListToSave()
                    vmRegistroRecoleccion.getImgItinerarioByID(nIdItinerarioDetalle)
                    delay(100)
                    vmRegistroRecoleccion.setRoomtoObjImagenes()
                    delay(100)
                    vmRegistroRecoleccion.saveItinerarioDetalle(
                    objItinerario = clRegistroRecoleccionSaveAll(
                        IdItinerarioDetalle = nIdItinerarioDetalle,
                        HoraTermino = LocalTime.now().toString().substring(0, 8),
                        NumeroEmpleado = objItinerarioDetalleDB.NumeroEmpleado.toInt(),
                        NombreEmpleado = objItinerarioDetalleDB.NombreEmpleado,
                        FirmaDigital = objItinerarioDetalleDB.FirmaDigital,
                        Usuario = objUser.IdUsuario,
                        IpOS = "Android " + Build.VERSION.RELEASE,
                        MqnVersion = "Version " + BuildConfig.VERSION_NAME,
                        Imagenes = objImagenesItinerario.Imagenes,
                        Detalle = objRegistroRecoleccionDetalle.Detalle
                    ),
                    objItinerarioDetalleDB
                )

                    bAlertaTermino = false
                }
            },
            dialogTitle = "Termino",
            dialogText = "Deseas finalizar esta sucursal",
            icon = Icons.Default.Warning
        )
    }
    if (bAlertaCapturarTodas){
        AlertDialogYesNo(
            onDismissRequest = {
                bAlertaCapturarTodas = false
            },
            onConfirmation = {
                CoroutineScope(Dispatchers.Main).launch {
                    val nTotal: Int = objItinerario.data.count() - 1
                    bLoading = true
                    for (i in 0..nTotal) {
                        if (objItinerario.data[i].CatEstatus == 36) {
                            nIdItinerarioDetalle = objItinerario.data[i].IdItinerarioDetalle
                            viewModel.getItinerarioDetalleByID(nIdItinerarioDetalle)
                            delay(100)
                            vmRegistroRecoleccion.getRegistroRecoleccionDetalle(nIdItinerarioDetalle)
                            delay(100)
                            vmRegistroRecoleccion.setDetalleListToSave()
                            vmRegistroRecoleccion.getImgItinerarioByID(nIdItinerarioDetalle)
                            delay(100)
                            vmRegistroRecoleccion.setRoomtoObjImagenes()
                            delay(100)
                            vmRegistroRecoleccion.saveItinerarioDetalle(
                                objItinerario = clRegistroRecoleccionSaveAll(
                                    IdItinerarioDetalle = nIdItinerarioDetalle,
                                    HoraTermino = LocalTime.now().toString().substring(0, 8),
                                    NumeroEmpleado = objItinerarioDetalleDB.NumeroEmpleado.toInt(),
                                    NombreEmpleado = objItinerarioDetalleDB.NombreEmpleado,
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
                        if (i == nTotal){
                            delay(5000)
                            bAlertaCapturarTodas = false
                        }
                    }

//                    navController.navigate("Itinerario")
                }
            },
            dialogTitle = "Guardar Datos",
            dialogText = "Deseas capturar las sucursales con estatus \"Termino\"? ",
            icon = Icons.Default.Warning
        )
    }
    if (bAlertaConsulta) {
        AlertDialogOnly(
            onConfirmation = { bAlertaConsulta = false },
            dialogTitle = "Error",
            dialogText = "No se pudo realizar la consulta, favor de verificar conexión",
            icon = Icons.Default.Warning
        )
    }
    if (bAlertaGuardado) {
        AlertDialogOnly(
            onConfirmation = {
                bAlertaGuardado = false
                //navController.navigate("Itinerario/${objItinerarioDetalleDB.IdItinerario}/${objItinerarioDetalleDB.IdVehiculo}/${sUsuario}/${nIdUsuario}")
                navController.navigate("Itinerario")
                             },
            dialogTitle = sTitleAlerta,
            dialogText = sTextAlerta,
            icon = Icons.Default.Warning
        )
    }
    if (bLoading) {
        BoxLoading(true, sText = "Cargando...")
    }
    LaunchedEffect(key1 = objItinerario.date) {
        bLoading = false
        if(!objItinerario.success && objItinerario.date != ""){
            bAlertaConsulta = true
        }
    }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            bLoading = true
            if(sDataUser.value != "") {
                objUser = Gson().fromJson(sDataUser.value, clUsuario::class.java)
            }
            if(sDataRuta.value != "") {
                objRuta = Gson().fromJson(sDataRuta.value, clItinerario::class.java)
            }
            delay(100)
            viewModel.getItinerarioDetalleDB()
            clLocation.pLocation(Context)
            delay(1000)
            if(clLocation.currentLocation.Latitude != 0.0) {
                viewModel.getItinerarioDetalle(
                    objRuta.IdItinerario,
                    objRuta.IdVehiculo,
                    objUser.Usuario,
                    clLocation.currentLocation.Latitude,
                    clLocation.currentLocation.Longitude
                )
            }
            else{
                viewModel.getItinerarioDetalle(
                    objRuta.IdItinerario,
                    objRuta.IdVehiculo,
                    objUser.Usuario,
                    20.6691577,
                    -103.2334544
                )
            }
            vmRegistroRecoleccion.getServiciosRecoleccion(objRuta.IdRuta, LocalDate.now().toString().substring(0,10))

            bLoading = false
        }
    }

    LaunchedEffect(key1 = objItinerarioGuardado, ) {
        if(objItinerarioGuardado.success) {
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.saveStoreSucursal("")
                //Agregamos bit de Elimiado en ROOM
                vmRegistroRecoleccion.setItinerarioDetalleEliminado(nIdItinerarioDetalle)
                vmRegistroRecoleccion.setImgItinerarioEliminado(nIdItinerarioDetalle)
                vmRegistroRecoleccion.setRegistroRecoleccionDetalleEliminado(
                    nIdItinerarioDetalle
                )
                vmRegistroRecoleccion.delObjItinerarioTerminado()

            }
//                if (bAlertaCapturarTodas == false) {
//                    navController.navigate("Itinerario")
//                }
        }
        else if (!objItinerarioGuardado.success && objItinerarioGuardado.date != "") {
            bAlertaGuardado = true
            sTitleAlerta = "Error"
            sTextAlerta =
                "Ocurrio un error al momento de guardar. Intente terminar la recolección más tarde."
            vmRegistroRecoleccion.delObjItinerarioTerminado()
        }
        else if(objItinerarioGuardado.message != ""){
            bAlertaGuardado = true
            sTitleAlerta = "Error"
            sTextAlerta =
                "No fue posible subir las evidencias(ftp). Intente terminar la recolección más tarde."
            vmRegistroRecoleccion.delObjItinerarioTerminado()
        }
        bLoading = false
    }
    LaunchedEffect(key1 = bGuardadoCompleto) {


//
    }
}

