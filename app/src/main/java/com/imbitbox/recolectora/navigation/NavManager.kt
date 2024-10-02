package com.imbitbox.recolectora.navigation

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.imbitbox.recolectora.viewModel.clCameraViewModel
import com.imbitbox.recolectora.viewModel.clItinerarioDetalleViewModel
import com.imbitbox.recolectora.viewModel.clItinerarioViewModel
import com.imbitbox.recolectora.viewModel.clQrScanViewModel
import com.imbitbox.recolectora.viewModel.clRegistroRecoleccionViewModel
import com.imbitbox.recolectora.viewModel.clUsuarioViewModel
import com.imbitbox.recolectora.viewModel.clVehiculoViewModel
import com.imbitbox.recolectora.views.recoleccion.ItinerarioView
import com.imbitbox.recolectora.views.recoleccion.MenuView
import com.imbitbox.recolectora.views.recoleccion.RegistroRecoleccionDetalleView
import com.imbitbox.recolectora.views.recoleccion.RegistroRecoleccionView
import com.imbitbox.recolectora.views.recoleccion.RegistroVehiculoView
import com.imbitbox.recolectora.views.recoleccion.SeleccionRutaView
import com.imbitbox.recolectora.views.security.LoginView
import com.imbitbox.recolectora.views.onboarding.OnBoardingView
import com.imbitbox.recolectora.views.recoleccion.DatosSucursalView
import com.imbitbox.recolectora.views.tools.BarCodeScreenView
import com.imbitbox.recolectora.views.tools.CameraScreenView
import com.imbitbox.recolectora.views.tools.SignaturePadView
import com.imbitbox.recolectora.views.tools.SignatureScreenView
import com.imbitbox.recolectora.views.tools.SplashScreenView
import org.koin.androidx.compose.koinViewModel

//@AndroidEntryPoint
@OptIn(ExperimentalGetImage::class) @Composable
fun NavManager() {
    val viewModelUsuario: clUsuarioViewModel = viewModel()
    val viewModelVehiculo: clVehiculoViewModel = viewModel()
    val viewModelItinerario: clItinerarioViewModel = viewModel()
    val viewModelBarCode: clQrScanViewModel = viewModel()
    val viewModelItinerarioDetalle: clItinerarioDetalleViewModel = viewModel()
    val viewModelRegistroRecoleccion: clRegistroRecoleccionViewModel = viewModel()
    val viewModelCamaraView: clCameraViewModel = koinViewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "SplashScreen") {
        composable("Login"){
            LoginView(navController, viewModelUsuario)
        }
        composable("RegistroVehiculo/{sUsuario}", arguments = listOf(
            navArgument("sUsuario"){type = NavType.StringType}
        )){
            val sUsuario = it.arguments?.getString("Usuario") ?: ""
            RegistroVehiculoView(navController, viewModelVehiculo, sUsuario)
        }
        composable("Itinerario", arguments = listOf(
//        composable("Itinerario/{nIdRuta}/{nIdItinerario}/{nIdVehiculo}/{sUsuario}/{nIdUsuario}", arguments = listOf(
//            navArgument("nIdRuta"){type = NavType.IntType},
//            navArgument("nIdItinerario"){type = NavType.IntType},
//            navArgument("nIdVehiculo"){type = NavType.IntType},
//            navArgument("sUsuario"){type = NavType.StringType},
//            navArgument("nIdUsuario"){type = NavType.IntType},
        )){
//            val nIdRuta = it.arguments?.getInt("nIdRuta") ?: 0
//            val nIdItinerario = it.arguments?.getInt("IdItinerario") ?: 0
//            val nIdVehiculo = it.arguments?.getInt("IdVehiculo") ?: 0
//            val sUsuario = it.arguments?.getString("Usuario") ?: ""
//            val nIdUsuario = it.arguments?.getInt("nIdUsuario") ?: 0
//            ItinerarioView(navController,viewModelItinerarioDetalle, viewModelRegistroRecoleccion, nIdRuta, nIdItinerario, nIdVehiculo, sUsuario, nIdUsuario)
            ItinerarioView(navController,viewModelItinerarioDetalle, viewModelRegistroRecoleccion)
        }
        composable("Menu"){
            MenuView(navController)
        }
        composable("DatosSucursal/{nIdItinerarioDetalle}", arguments = listOf(
            navArgument("nIdItinerarioDetalle"){type = NavType.IntType},
        )){
            val nIdItinerarioDetalle = it.arguments?.getInt("nIdItinerarioDetalle") ?: 0
            DatosSucursalView(navController, nIdItinerarioDetalle, viewModelItinerarioDetalle)
        }
        composable("SeleccionRuta/{nIdEmpleado}", arguments = listOf(
            navArgument("nIdEmpleado"){ type = NavType.IntType }
        )){
            val nIdEmpleado = it.arguments?.getInt("nIdEmpleado") ?: 0
            SeleccionRutaView(navController,viewModelItinerario, nIdEmpleado)
        }
        composable("RegistroRecoleccion/{nIdItinerarioDetalle}", arguments = listOf(
            navArgument("nIdItinerarioDetalle"){type = NavType.IntType},
        )){
            val nIdItinerarioDetalle = it.arguments?.getInt("nIdItinerarioDetalle") ?: 0
            RegistroRecoleccionView(navController, nIdItinerarioDetalle ,viewModelItinerarioDetalle, viewModelRegistroRecoleccion)
        }
        composable("RegistroRecoleccionDetalle/{nIdSucursal}/{nIdItinerarioDetalle}", arguments = listOf(
            navArgument("nIdSucursal"){type = NavType.IntType},
            navArgument("nIdItinerarioDetalle"){type = NavType.IntType},
            )){
            val nIdSucursal = it.arguments?.getInt("nIdSucursal") ?: 0
            val nIdItinerarioDetalle = it.arguments?.getInt("nIdItinerarioDetalle") ?: 0
            RegistroRecoleccionDetalleView(navController, nIdSucursal, nIdItinerarioDetalle, viewModelRegistroRecoleccion)
        }
        composable("Intro"){
            OnBoardingView(navController)
        }
        composable("SplashScreen"){
            SplashScreenView(navController)
        }
        composable("BarCodeScreen/{bIsVehiculo}/{sParametro}", arguments = listOf(
            navArgument("bIsVehiculo"){type = NavType.BoolType},
            navArgument("sParametro"){type = NavType.StringType}
        )){
            val bIsVehiculo = it.arguments?.getBoolean("bIsVehiculo") ?: false
            val sParametro = it.arguments?.getString("sParametro") ?: ""
            BarCodeScreenView(navController, viewModelBarCode, bIsVehiculo, sParametro)
        }
        composable("CameraScreen/{sIdItinerarioDetalle}/{sTipoImagen}", arguments = listOf(
            navArgument("sIdItinerarioDetalle"){type = NavType.StringType},
            navArgument("sTipoImagen"){type = NavType.StringType}
        )){
            val sIdItinerarioDetalle = it.arguments?.getString("sIdItinerarioDetalle") ?: ""
            val sTipoImagen = it.arguments?.getString("sTipoImagen") ?: ""
            CameraScreenView(navController,viewModelCamaraView,viewModelRegistroRecoleccion,sIdItinerarioDetalle,sTipoImagen)
        }
        composable("SignaturePad/{sIdItinerarioDetalle}", arguments = listOf(
            navArgument("sIdItinerarioDetalle"){type = NavType.StringType},
        )){
            val sIdItinerarioDetalle = it.arguments?.getString("sIdItinerarioDetalle") ?: ""
            SignaturePadView(navController, sIdItinerarioDetalle,viewModelRegistroRecoleccion)
        }
    }
}
