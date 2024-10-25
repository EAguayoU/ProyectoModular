package com.imbitbox.recolectora.views.recoleccion

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.components.AlertDialogOnly
import com.imbitbox.recolectora.components.BoxLoading
import com.imbitbox.recolectora.components.HSpace
import com.imbitbox.recolectora.components.NoDataView
import com.imbitbox.recolectora.components.VSpace
import com.imbitbox.recolectora.models.clItinerario.clItinerarioApi
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.ui.theme.clrAzul
import com.imbitbox.recolectora.ui.theme.clrVerde
import com.imbitbox.recolectora.viewModel.clItinerarioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SeleccionRutaView(navController: NavController, viewModel : clItinerarioViewModel, nIdEmpleado : Int) {
    val context = LocalContext.current
    val dataStore = clDataStore(context)
    var bLoading by remember { mutableStateOf(value = false) }
    val objRuta by viewModel.itinerario.collectAsState(clItinerarioApi(false,0,"",",","", "", emptyList()))
    var bAlerta by remember { mutableStateOf(value = false) }
    var bAlertaAsignado by remember { mutableStateOf(value = false) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Seleccione ruta", fontSize = 25.sp, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = clrVerde,
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
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        },
    ) {
        if (objRuta.success){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 70.dp)
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start

            ) {
                items(objRuta.data) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize(),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ruta),
                            contentDescription = "Rutas",
                            modifier = Modifier
                                .size(90.dp)
                                .padding(end = 20.dp)
                                .clip(CircleShape)
                                .weight(1.5f),

                            )
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .weight(3.5f),

                        ) {
                            Text(
                                text = item.Ruta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Black

                            )
                            Text(
                                text = "Recolectora y reciclados de guadalajara",
                                fontSize = 13.sp,
                                color = Color.Black
                            )


                        }
                        HSpace(30)
                        IconButton(
                            onClick = {
                                dataStore.saveStoreRuta(Gson().toJson(item))
                                bAlertaAsignado = true

                                      },
                            modifier = Modifier
                                .size(60.dp)

                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(color = clrAzul)
                                    .weight(1f)
                            )
                        }

                    }
                    VSpace(30)
                }
            }
        }
        else if (!objRuta.success && objRuta.date != "") {
            NoDataView()
            bLoading = false
        }
        else{
            BoxLoading(true, sText = "Cargando...")
        }

    }
    if (bLoading) {
        BoxLoading(true, sText = "Cargando...")
    }
    if (bAlerta) {
        AlertDialogOnly(
            onConfirmation = { bAlerta = false },
            dialogTitle = "Error",
            dialogText = "No se pudo realizar la consulta, favor de verificar conexión",
            icon = Icons.Default.Warning
        )
    }
    if (bAlertaAsignado) {
        AlertDialogOnly(
            onConfirmation = {
                bAlertaAsignado = false
                navController.popBackStack("Menu", false)
                             },
            dialogTitle = "Asignado",
            dialogText = "Se asigno la ruta correctamente",
            icon = Icons.Default.CheckCircle
        )
    }

    LaunchedEffect(key1 = objRuta.date) {
        bLoading = false
    }
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            bLoading = true
            viewModel.getItinerarioEmpleado(nIdEmpleado)
        }
    }
    LaunchedEffect(key1 = objRuta) {
        if(!objRuta.success && objRuta.date != ""){
            CoroutineScope(Dispatchers.IO).launch {
                bAlerta = true
                bLoading = false
            }
        }

    }
}

