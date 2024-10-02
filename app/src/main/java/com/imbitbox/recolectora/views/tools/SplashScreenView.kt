package com.imbitbox.recolectora.views.tools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.components.LoaderData
import com.imbitbox.recolectora.models.tools.clDataStore
import kotlinx.coroutines.delay

@Composable
fun SplashScreenView(navController: NavController) {
    val context = LocalContext.current
    val objDataStore = clDataStore(context)
    val bStoreData = objDataStore.getStoreBoard.collectAsState(initial = false)
    val bStoreLogin = objDataStore.getStoreLogin.collectAsState(initial = false)
    var sScreen by remember { mutableStateOf("") }

    if (bStoreData.value) {
       sScreen = "Login"
        if(bStoreLogin.value){
            sScreen = "Menu"
        }
    } else {
       sScreen = "Intro"
    }

    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.navigate(sScreen){
           // popUpTo(0)
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        LoaderData(modifier = Modifier.fillMaxSize(), image = R.raw.truck_lottie, bIsLottie = true )
    }
}
