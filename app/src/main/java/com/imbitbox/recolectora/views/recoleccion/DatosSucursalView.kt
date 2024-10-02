package com.imbitbox.recolectora.views.recoleccion

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.classes.clMaps
import com.imbitbox.recolectora.components.HSpace
import com.imbitbox.recolectora.components.VSpace
import com.imbitbox.recolectora.dataBase.clItinerarioDetalleDB
import com.imbitbox.recolectora.ui.theme.clrAzul
import com.imbitbox.recolectora.ui.theme.clrVerde
import com.imbitbox.recolectora.viewModel.clItinerarioDetalleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DatosSucursalView(
    navController: NavController,
    nIdItinerarioDetalle: Int,
    viewModel: clItinerarioDetalleViewModel
) {
    val objItinerarioDetalleDB by viewModel.objItinerarioDetalleDB.collectAsState(clItinerarioDetalleDB(
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
        0
    ))
    var bShowAlert by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        //Barra
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Datos Sucursal", color = Color.White, fontSize = 25.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = clrVerde
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack("Itinerario",false)
                    }) {
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
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(top = 70.dp)
                    .padding(horizontal = 10.dp)
                    .weight(2f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.sucursal),
                        contentDescription = "",
                        modifier = Modifier
                            .size(80.dp)
                    )
                    Text(
                        text = objItinerarioDetalleDB.IdSucursal.toString(),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray
                    )
                }
                HSpace(15)
                Column {
                    Text(
                        text = "Id Recolección - ${objItinerarioDetalleDB.IdItinerarioDetalle}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                    VSpace(10)
                    Text(
                        text = "Cliente",
                        color = clrAzul,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = objItinerarioDetalleDB.Cliente, fontSize = 17.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray
                    )
                    VSpace(10)
                    Text(
                        text = "Sucursal",
                        color = clrAzul,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = objItinerarioDetalleDB.Sucursal,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray
                    )
                    VSpace(10)
                    Row {
                        Column(
                            Modifier
                                .size(width = 160.dp, height = 70.dp)
                        ) {
                            Text(
                                text = "Latitud",
                                color = clrAzul,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = objItinerarioDetalleDB.Latitud,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(end = 25.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Longitud",
                                color = clrAzul,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = objItinerarioDetalleDB.Longitud,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                    }
                    VSpace(10)
                    Row {
                        Column(
                            Modifier
                                .size(width = 160.dp, height = 50.dp)
                        ) {
                            Text(
                                text = "Tipo Asignación",
                                color = clrAzul,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = objItinerarioDetalleDB.TipoAsignacion,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                        }
                        VSpace(10)
                        Column {
                            Text(
                                text = "Estatus",
                                color = clrAzul,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = objItinerarioDetalleDB.Estatus,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                    }
                    VSpace(10)
                    Text(
                        text = "Horario",
                        color = clrAzul,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = objItinerarioDetalleDB.Horario,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                }
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Divider(color = Color.LightGray, thickness = 2.dp)
                VSpace(20)
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mapa),
                        contentDescription = "",
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                bShowAlert = true
                            }
                    )
                    HSpace(15)
                    Column {
                        Text(
                            text = "Ver Mapa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Traza la Ruta desde tu punto a la sucursal y sigue el trayecto",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }
                VSpace(20)
                Divider(color = Color.LightGray, thickness = 2.dp)
                VSpace(20)
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.editar),
                        contentDescription = "",
                        modifier = Modifier
                            .size(80.dp)
                            .clickable { navController.navigate("RegistroRecoleccion/${nIdItinerarioDetalle}") }
                    )
                    HSpace(15)
                    Column {
                        Text(
                            text = "Registrar la recolección", fontWeight = FontWeight.Bold,
                            fontSize = 19.sp
                        )
                        Text(
                            text = "Registre la información de la visita para el reporte del manifiesto",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }
                VSpace(60)
            }
        }
        if (bShowAlert) {
            AlertDialog(
                onDismissRequest = {
                    bShowAlert = false
                },
                title = {
                    Text(text = "Abrir App")
                },
                text = {
                    Text("Selecciona la aplicación que deseas te lleve a la sucursal")
                },
                confirmButton = {
                    Image(
                        painter = painterResource(id = R.drawable.google_maps),
                        contentDescription = "",
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(130.dp)
                            .padding(horizontal = 20.dp)
                            .clickable {
                                bShowAlert = false

                                clMaps.openGoogleMapsNavigationToB(
                                    context,
                                    objItinerarioDetalleDB.Latitud.toDouble(),
                                    objItinerarioDetalleDB.Longitud.toDouble()
                                )
                            }
                    )
                },
                dismissButton = {
                    Image(
                        painter = painterResource(id = R.drawable.waze),
                        contentDescription = "",
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(130.dp)
                            .padding(horizontal = 20.dp)
                            .clickable {
                                bShowAlert = false
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.setItinerarioDetalleEnCamino(
                                        objItinerarioDetalleDB.IdItinerarioDetalle,
                                        LocalTime
                                            .now()
                                            .toString()
                                            .substring(0, 8)
                                    )
                                }
                                clMaps.openWazeNavigationToB(
                                    context,
                                    objItinerarioDetalleDB.Latitud.toDouble(),
                                    objItinerarioDetalleDB.Longitud.toDouble()
                                )
                            }
                    )
                }
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getItinerarioDetalleByID(nIdItinerarioDetalle)
    }
}