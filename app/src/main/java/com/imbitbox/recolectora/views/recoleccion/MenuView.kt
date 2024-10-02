package com.imbitbox.recolectora.views.recoleccion

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.components.AlertDialogOnly
import com.imbitbox.recolectora.components.VSpace
import com.imbitbox.recolectora.models.clItinerario.clItinerario
import com.imbitbox.recolectora.models.clUsuario.clUsuario
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.ui.theme.clrVerde

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MenuView(navController: NavController) {
    val context = LocalContext.current
    val dataStore = clDataStore(context)
    val sDataUser = dataStore.getStoreUser.collectAsState(initial = "")
    val sDataRuta = dataStore.getStoreRuta.collectAsState(initial = "")
    var objUser by remember { mutableStateOf(clUsuario()) }
    var objRuta by remember { mutableStateOf(clItinerario()) }
    var bError by remember { mutableStateOf(false)}
    var sError by remember { mutableStateOf("")}
    val permisoLocation = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {}

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color(0xffeeeefb))
            .verticalScroll(rememberScrollState())
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1.2f)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .background(
                        color = clrVerde,
                        shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
                    )
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(start = 14.dp)
                        .weight(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "BIENVENIDO", color = Color.White, fontSize = 18.sp
                    )
                    Text(
                        text = objUser.Nombre,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                    Text(
                        text = objUser.Grupo,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.recolector_perfil_zoom),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)

                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 170.dp, start = 24.dp, end = 24.dp)
                    .shadow(3.dp, shape = RoundedCornerShape(20.dp))
                    .background(
                        color = Color.White, shape = RoundedCornerShape(20.dp)
                    )

            ) {
//                Column(
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
//                        .height(90.dp)
//                        .width(90.dp)
////
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.lock),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .width(50.dp)
//                            .height(50.dp)
//                    )
//                    Text(
//                        text = "Cambiar",
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black,
//                        modifier = Modifier
//                            .padding(top = 10.dp)
//                    )
//
//                }
//                Column(
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
//                        .height(90.dp)
//                        .width(90.dp)
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.wifi),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .width(50.dp)
//                            .height(50.dp)
//                    )
//                    Text(text = "En Linea",
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black,
//                            modifier = Modifier
//                            .padding(top = 10.dp))
//
//                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
                        .height(90.dp)
                        .width(150.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = null,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable {
                                //CoroutineScope(Dispatchers.Main).launch {
                                dataStore.saveStoreLogin(false)
                                dataStore.saveStoreUser("")
                                navController.navigate("SplashScreen")
                                {
                                    //  popUpTo(0)
                                }
                                //}
                            }
                    )
                    Text(text = "Cerrar Sesión",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(top = 10.dp))
                }

            }
        }
        Text(text = "Menu",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 35.sp,
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 5.dp)
        )
//        VSpace(-70)
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 5.dp)
                .weight(2f)

        ) {

            BtnMenuItems(
                nImagen = R.drawable.ruta2,
                sText = "Ruta",
                sDescription = "Selecciona tu ruta de recolección"
            ) {
                navController.navigate("SeleccionRuta/${objUser.IdEmpleado}")
            }
            VSpace(5)
            BtnMenuItems(
                nImagen = R.drawable.vehiculo,
                sText = "Vehículo",
                sDescription = "Captura el Km de tu vehículo"
            ) {
                navController.navigate("RegistroVehiculo/${objUser.Usuario}"){
                }
            }
            VSpace(5)
            BtnMenuItems(
                nImagen = R.drawable.itinerario,
                sText = "Itinerario",
                sDescription = "Genera el itinerario del día"
            ) {
                if(sDataRuta.value != "") {
                    if (objRuta.IdItinerario == 0) {
                        sError = "Debe seleccionar una ruta para continuar"
                        bError = true
                    } else if (objRuta.IdRuta == 0) {
                        sError = "Debe seleccionar una ruta para continuar"
                        bError = true
                    } else if (objRuta.IdVehiculo == 0) {
                        sError = "Debe seleccionar un vehiculo para continuar"
                        bError = true
                    } else {
                        if (permisoLocation.all {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    it
                                ) == PackageManager.PERMISSION_GRANTED
                            }) {
                            println("Tienes Permiso")
//                            navController.navigate("Itinerario/${objRuta.IdRuta}/${objRuta.IdItinerario}/${objRuta.IdVehiculo}/${objUser.Usuario}/${objUser.IdUsuario}")
                            navController.navigate("Itinerario")
                        } else {
                            println("No tienes Permiso")
                            launcherMultiplePermissions.launch(permisoLocation)
                        }
                    }
                }else{
                    sError = "Debe seleccionar una ruta para continuar"
                    bError = true
                }
            }
            VSpace(15)
//            BtnMenuItems(
//                nImagen = R.drawable.tirar_carga,
//                sText = "Tirar Carga",
//                sDescription = "Registra un páro para tirar la carga recolectada"
//            ) {
////                navController.navigate("Itinerario/${objRuta.IdItinerario}/${objRuta.IdVehiculo}/${objUser.Usuario}")
//            }
//            VSpace(15)
//            BtnMenuItems(
//                nImagen = R.drawable.senal_stop,
//                sText = "Detener",
//                sDescription = "Registre una incidencia por la cual no puedas continuar"
//            ) {
////                navController.navigate("Itinerario/${objRuta.IdItinerario}/${objRuta.IdVehiculo}/${objUser.Usuario}")
//            }

        }

        if(bError){
            AlertDialogOnly(
                onConfirmation = { bError = false },
                dialogTitle = "Revisar Itinerario",
                dialogText = sError,
                icon = Icons.Default.Warning,
            )
        }

        LaunchedEffect(key1 = sDataUser.value) {
            if (sDataUser.value != "") {
                objUser = Gson().fromJson(sDataUser.value, clUsuario::class.java)
            }
        }

        LaunchedEffect(key1 = sDataRuta.value) {
            if (sDataRuta.value != "") {
                objRuta = Gson().fromJson(sDataRuta.value, clItinerario::class.java)
            }
        }
    }
}

@Composable
fun BtnMenuItems(nImagen: Int, sText: String, sDescription: String, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .shadow(10.dp, shape = RoundedCornerShape(50.dp))
                .background(
                    Color.White,
                    shape = RoundedCornerShape(50.dp)
                )
                .size(width = 80.dp, height = 80.dp)

        ) {
            Image(
                painter = painterResource(id = nImagen),
                contentDescription = sText,
                modifier = Modifier
                    .size(width = 50.dp, height = 50.dp)
                    .clickable(onClick = onClick)

            )
        }
        Column {
            Text(
                text = sText,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Text(
                text = sDescription,
                color = Color.Black,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}