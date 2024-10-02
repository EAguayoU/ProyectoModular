package com.imbitbox.recolectora.views.recoleccion

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.imbitbox.recolectora.components.BtnIcon
import com.imbitbox.recolectora.components.BtnStandar
import com.imbitbox.recolectora.components.HSpace
import com.imbitbox.recolectora.components.VSpace
import com.imbitbox.recolectora.models.clItinerario.clItinerarioDetalleApi
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionDetalleApi
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroRecoleccionDetalleData
import com.imbitbox.recolectora.models.clRegistroRecoleccion.clRegistroServiciosTabla
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.ui.theme.clrGrisOscuro
import com.imbitbox.recolectora.ui.theme.clrVerde
import com.imbitbox.recolectora.ui.theme.clrVerdeClaro
import com.imbitbox.recolectora.viewModel.clRegistroRecoleccionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroRecoleccionDetalleView(navController: NavController, nIdSucursal: Int, nIdItinerarioDetalle: Int, vmRegistroRecoleccion: clRegistroRecoleccionViewModel) {
    //DataStore
    var context = LocalContext.current
    val dataStore = clDataStore(context)
    //Controles
    var nCantidad by remember { mutableStateOf(0) }
    var objColor: Color = clrGrisOscuro
    var bExpandido by remember { mutableStateOf(false) }
    var sCombo by remember { mutableStateOf("") }
    var nRows = 0
    val objServicios by vmRegistroRecoleccion.serviciosRecoleccionByIdSucursal.collectAsState()
    var nIdSucursalTipoServicio = 0
    val objRegistrosTabla by vmRegistroRecoleccion.RegistroRecoleccionDetalle.collectAsState(
        clRegistroRecoleccionDetalleData(
            false,
            0,
            "",
            ",",
            "",
            "",
            emptyList()
        )
    )
    Scaffold(
        //Barra
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    "Registro de Recolección", color = Color.White, fontSize = 25.sp
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = clrVerde
            ), navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            })
        }) {
        //Contenido
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
                .padding(horizontal = 10.dp)

        ) {
            ExposedDropdownMenuBox(expanded = bExpandido, onExpandedChange = { bExpandido = it }) {
                TextField(
                    value = sCombo, onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Servicios registrados en sucursal")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = bExpandido)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .background(Color.White)
                )

                ExposedDropdownMenu(
                    expanded = bExpandido,
                    onDismissRequest = { bExpandido = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    for(i in 0..objServicios.Servicios.size-1){
                        DropdownMenuItem(
                            text = { Text(text = objServicios.Servicios[i].TipoServicio, color = Color.Black) },
                            onClick = {
                                sCombo = objServicios.Servicios[i].TipoServicio
                                bExpandido = false
                                nIdSucursalTipoServicio = objServicios.Servicios[i].IdSucursalTipoServicio
                            })
                    }
                }
            }
            VSpace(20)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                BtnStandar(
                    modifier = Modifier.size(60.dp),
                    sText = "-",
                    containerColor = clrGrisOscuro,
                    nFontSize = 30,
                    nRoundedCorner = 30
                ) {
                    if (nCantidad > 0) nCantidad--
                }
                HSpace(10)
                TextField(modifier = Modifier
                    .size(width = 155.dp, height = 60.dp)
                    .background(Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = nCantidad.toString(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    readOnly = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center, fontSize = 20.sp
                    ),
                    onValueChange = {
                        if (it == "") nCantidad = 0
                        else nCantidad = it.toInt()
                    })
                HSpace(10)
                BtnStandar(
                    modifier = Modifier.size(60.dp),
                    sText = "+",
                    containerColor = clrGrisOscuro,
                    nFontSize = 30,
                    nRoundedCorner = 30
                ) {
                    nCantidad++
                }
            }
            VSpace(25)
            BtnStandar(
                modifier = Modifier.size(width = 290.dp, height = 60.dp),
                sText = "Agregar",
                containerColor = clrGrisOscuro,
                nRoundedCorner = 30,
                nFontSize = 20
            ) {
                if(sCombo != "" && nCantidad != 0) {

                    val nLastIndex = objRegistrosTabla.data.size
                    val sBuscar = objRegistrosTabla.data.filter {
                        it.TipoServicio.contains(sCombo)
                    }
                    if(sBuscar.isNullOrEmpty()){
                        println("No esta")
                        CoroutineScope(Dispatchers.IO).launch {
                            vmRegistroRecoleccion.setRegistroRecoleccionDetalle(clRegistroServiciosTabla(IdSucursalTipoServicio = nIdSucursalTipoServicio, TipoServicio = sCombo, Cantidad = nCantidad, IdItinerarioDetalle = nIdItinerarioDetalle))
                            delay(100)
                            vmRegistroRecoleccion.getRegistroRecoleccionDetalle(nIdItinerarioDetalle)
                            delay(100)
                            vmRegistroRecoleccion.setRoomtoObjRegistroRecoleccionDetalle()
                        sCombo = ""
                        nCantidad = 0
                        }
                    }
                    else{
                        println("Si esta")
                        val nIndex = objRegistrosTabla.data.indexOfFirst{ it.TipoServicio == sCombo}
                        objRegistrosTabla.data[nIndex].Cantidad += nCantidad
                        CoroutineScope(Dispatchers.IO).launch {
                            vmRegistroRecoleccion.updateRegistroRecoleccionDetalleCantidad(
                                nIndex,
                                objRegistrosTabla.data[nIndex].Cantidad
                            )
                            delay(100)
                            vmRegistroRecoleccion.getRegistroRecoleccionDetalle(nIdItinerarioDetalle)
                            delay(100)
                            vmRegistroRecoleccion.setRoomtoObjRegistroRecoleccionDetalle()

                            sCombo = ""
                            nCantidad = 0
                        }
                    }
                }
            }

            VSpace(25)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "Tipo Servicio",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(clrVerde)
                        .height(40.dp)
                        .weight(2.5f)
                )
                Text(
                    text = "Cantidad",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(clrVerde)
                        .height(40.dp)
                        .weight(2f)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f)
            )
            {
                items(objRegistrosTabla.data) { servicio ->
                    nRows ++
                    if (nRows % 2 == 0)
                        objColor = clrVerdeClaro
                    else
                        objColor = Color.White

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    )
                    {
                        BtnIcon(
                            imageVector = Icons.Filled.Delete,
                            tint = clrGrisOscuro,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .background(objColor)
                                .height(40.dp)
                                .weight(.5f)
                        )
                        {
                            CoroutineScope(Dispatchers.IO).launch {
                                vmRegistroRecoleccion.delRegistroRecoleccionDetalleID(servicio.Id)
                                delay(100)
                                vmRegistroRecoleccion.getRegistroRecoleccionDetalle(nIdItinerarioDetalle)
                                delay(100)
                                vmRegistroRecoleccion.setRoomtoObjRegistroRecoleccionDetalle()

                            }
                        }
//                        VSpace(5)
                        Text(
                            text = servicio.TipoServicio,
                            color = clrGrisOscuro,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .background(objColor)
                                .height(40.dp)
                                .weight(2f)
                                .padding(top = 7.dp)

                        )

                        Text(
                            text = servicio.Cantidad.toString(),
                            color = clrGrisOscuro,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .background(objColor)
                                .height(40.dp)
                                .weight(2f)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 10.dp)
                    .weight(.3f)
            ) {
                BtnStandar(
                    modifier = Modifier
                        .size(width = 350.dp, height = 80.dp)
                        .padding(top = 25.dp),
                    sText = "Guardar",
                    containerColor = clrVerde,
                    nRoundedCorner = 30,
                    nFontSize = 20
                ) {
                        navController.popBackStack()
                }
            }
        }
        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                vmRegistroRecoleccion.getServiciosRecoleccionSucursal(nIdSucursal)
                delay(100)
                vmRegistroRecoleccion.setRoomtoObjServiciosRecoleccionSucursal()

                vmRegistroRecoleccion.getRegistroRecoleccionDetalle(nIdItinerarioDetalle)
                delay(100)
                vmRegistroRecoleccion.setRoomtoObjRegistroRecoleccionDetalle()
            }
        }
    }
}

