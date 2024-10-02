package com.imbitbox.recolectora.views.security

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.imbitbox.recolectora.BuildConfig
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.components.BoxLoading
import com.imbitbox.recolectora.components.BtnStandar
import com.imbitbox.recolectora.components.BtnSwitch
import com.imbitbox.recolectora.components.EdtHintField
import com.imbitbox.recolectora.components.HSpace
import com.imbitbox.recolectora.components.EdtField
import com.imbitbox.recolectora.components.VSpace
import com.imbitbox.recolectora.models.clUsuario.clUsuario
import com.imbitbox.recolectora.models.clUsuario.clUsuarioApi
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.ui.theme.clrAzul
import com.imbitbox.recolectora.ui.theme.clrGrisOscuro
import com.imbitbox.recolectora.ui.theme.clrVerde
import com.imbitbox.recolectora.viewModel.clUsuarioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun LoginView(navController: NavController, viewModel: clUsuarioViewModel) {
    //Declaración de variables

    var sUser by remember { mutableStateOf("") }
    var sPassword by remember { mutableStateOf("") }
    var bShowPassword by remember { mutableStateOf(value = false) }
    var bChecked by remember { mutableStateOf(value = false) }
    var bLoading by remember { mutableStateOf(value = false) }
    var bShowError by remember { mutableStateOf(value = false) }

    val context = LocalContext.current
    val dataStore = clDataStore(context)
    val objUsuario by viewModel.usuario.collectAsState(clUsuarioApi(false,0,"",",","", "", emptyList()))
    //Interfaz
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()

        ) {
            Text(
                //BuildConfig.VERSION_NAME Version Aplicacion
                //Build.VERSION.RELEASE Version Android
                "Version " + BuildConfig.VERSION_NAME,
                color = Color.Gray,
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(horizontal = 10.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.logo_banner),
                contentDescription = "Logo Banner",
                modifier = Modifier
                    .size(width = 300.dp, height = 150.dp)
            )
            VSpace(20)
            EdtField(
                label = "Usuario",
                color = Color.Black,
                bReadOnly = bLoading,
                value = sUser.uppercase().trim(),
                onValueChange = { sUser = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            VSpace(20)
            EdtHintField(
                label = "Password",
                bReadOnly = bLoading,
                color = Color.Black,
                bShowValue = bShowPassword,
                sText = sPassword.trim(),
                onValueChange = { sPassword = it },
                onShowChange = { bShowPassword = !bShowPassword },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)

            )
            if (bShowError) {
                VSpace(10)
                Text(
                    "Usuario / Contraseña incorrecto",
                    color = Color.Red
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 5.dp)
                    .padding(bottom = 20.dp)
            ) {
                Text("Recordarme", color = Color.Black)
                HSpace(15)
                BtnSwitch(cColor = clrVerde, bEnabled = !bLoading, bChecked = bChecked) {
                    bChecked = it
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BtnStandar(
                    sText = "Cerrar", nRoundedCorner = 30, containerColor = clrGrisOscuro,
                    bIsClickable = !bLoading
                ) {
                    Runtime.getRuntime().exit(-1)

                }
                HSpace(20)
                BtnStandar(
                    sText = "Aceptar", nRoundedCorner = 30, containerColor = clrAzul,
                    bIsClickable = !bLoading
                )
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        bLoading = true
                        viewModel.getUsuarios(sUser, sPassword)
                    }
                }
            }
            VSpace(45)
            Image(
                painter = painterResource(id = R.drawable.trashman),
                contentDescription = "Logo Banner",
                modifier = Modifier
                    .size(400.dp)
            )
        }
    }
    if (bLoading) {
        BoxLoading(true, sText = "Validando...")
    }
    LaunchedEffect(key1 = objUsuario.date) {
        bLoading = false
        if (objUsuario.success == false && objUsuario.date != "") {
            bShowError = true
        }
    }
    LaunchedEffect(key1 = sUser, key2 = sPassword) {
        bShowError = false
    }
    LaunchedEffect(key1 = objUsuario.success) {
        if (objUsuario.success) {
            dataStore.saveStoreLogin(bChecked)
            dataStore.saveStoreUser(Gson().toJson(objUsuario.data[0]))
            sUser = ""
            sPassword = ""
            viewModel.delUsuario()
            navController.navigate("Menu")
            {
                popUpTo(0)
            }
        }
    }

}




