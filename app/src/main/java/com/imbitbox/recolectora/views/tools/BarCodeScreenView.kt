@file:OptIn(ExperimentalPermissionsApi::class)
package com.imbitbox.recolectora.views.tools

import android.Manifest
import android.annotation.SuppressLint
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.imbitbox.recolectora.classes.QrCodeAnalyzer
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.viewModel.QrScanUIState
import com.imbitbox.recolectora.viewModel.clQrScanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
typealias AndroidSize = android.util.Size

@SuppressLint("CoroutineCreationDuringComposition", "RestrictedApi",
    "UnrememberedGetBackStackEntry", "ResourceType"
)
@Composable
@ExperimentalGetImage
fun BarCodeScreenView(
    navController: NavController,
    viewModel: clQrScanViewModel,
    bIsVehiculo : Boolean,
    sParametro : String = ""
) {
    val uiState by viewModel.uiState.collectAsState()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current

    val contexto = LocalContext.current
    val dataStore = clDataStore(contexto)
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder().build()
    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
        .setTargetResolution(
            AndroidSize(previewView.width, previewView.height)
        )
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    val targetRect by remember { derivedStateOf { uiState.targetRect } }

    LaunchedEffect(targetRect) {
        imageAnalysis.setAnalyzer(
            Dispatchers.Default.asExecutor(),
            QrCodeAnalyzer(
                targetRect = targetRect.toAndroidRect(),
                previewView = previewView,
            ) { result ->
                viewModel.onQrCodeDetected(result)
            }
        )
    }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(uiState.lensFacing)
        .build()
    var camera by remember { mutableStateOf<Camera?>(null) }

    LaunchedEffect(uiState.lensFacing) {
        val cameraProvider = ProcessCameraProvider.getInstance(context)
        camera = withContext(Dispatchers.IO) {
            cameraProvider.get()
        }.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    Scaffold { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            previewView = previewView,
            bIsVehiculo = bIsVehiculo,
            onTargetPositioned = viewModel::onTargetPositioned
        )
    }

    if(uiState.detectedQR.isNotEmpty() ) {
        if (bIsVehiculo) {
            dataStore.saveStoreVehiculo(uiState.detectedQR)
//            navController.previousBackStackEntry
//                ?.savedStateHandle
            navController.popBackStack("RegistroVehiculo/${sParametro}",false)
        }
        else{
            dataStore.saveStoreSucursal(uiState.detectedQR)
            navController.popBackStack("RegistroRecoleccion/${sParametro}", false)
        }
        viewModel.DeleteCodeQr()
    }

}


@Composable
private fun Content(
    modifier: Modifier,
    previewView: PreviewView,
    uiState: QrScanUIState,
    bIsVehiculo: Boolean,
    onTargetPositioned: (Rect) -> Unit

) {
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                previewView
            }
        )
        val widthInPx: Float
        val heightInPx: Float
        val radiusInPx: Float
        with(LocalDensity.current) {
            widthInPx = 250.dp.toPx()
            heightInPx = 250.dp.toPx()
            radiusInPx = 16.dp.toPx()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .5f)),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .size(250.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                    .onGloballyPositioned {
                        onTargetPositioned(it.boundsInRoot())
                    }
            ) {
                val offset = Offset(
                    x = (size.width - widthInPx) / 2,
                    y = (size.height - heightInPx) / 2,
                )
                val cutoutRect = Rect(offset, Size(widthInPx, heightInPx))
                // Source
                drawRoundRect(
                    topLeft = cutoutRect.topLeft,
                    size = cutoutRect.size,
                    cornerRadius = CornerRadius(radiusInPx, radiusInPx),
                    color = Color.Transparent,
                    blendMode = BlendMode.Clear
                )
            }
        }
        //if (uiState.detectedQR.isEmpty() || uiState.detectedQR.isnotEmpty()) {

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .background(Color.White.copy(alpha = .6f), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = if(bIsVehiculo){"Escaneé el código QR de su Unidad"} else{"Escaneé el código QR de la Sucursal"},
        )
        // }
    }
}