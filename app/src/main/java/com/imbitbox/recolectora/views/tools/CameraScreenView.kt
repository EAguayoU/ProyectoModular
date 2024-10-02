package com.imbitbox.recolectora.views.tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.classes.clCameraState
import com.imbitbox.recolectora.classes.clSavePhotoToGalleryUseCase
import com.imbitbox.recolectora.components.HSpace
import com.imbitbox.recolectora.extensions.rotateBitmap
import com.imbitbox.recolectora.viewModel.clCameraViewModel
import com.imbitbox.recolectora.viewModel.clRegistroRecoleccionViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.Executor
import kotlin.reflect.KFunction5

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CameraScreenView(
    navController: NavController,
    vmCamara: clCameraViewModel = koinViewModel(),
    vmRegistroRecoleccion: clRegistroRecoleccionViewModel,
    sIdItinerarioDetalle: String,
    sTipoImagen: String
) {
    val cameraState: clCameraState by vmCamara.state.collectAsStateWithLifecycle()

    CameraContent(
        navController,
        onPhotoCaptured = vmCamara::storePhotoInGallery,
        lastCapturedPhoto = cameraState.capturedImage,
        vmCamara,
        sIdItinerarioDetalle,
        sTipoImagen,
        vmRegistroRecoleccion
    )

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun CameraContent(
    navController: NavController,
    onPhotoCaptured: KFunction5<Bitmap, String, String, clRegistroRecoleccionViewModel, Boolean, Unit>,
    lastCapturedPhoto: Bitmap? = null,
    vmCamara: clCameraViewModel = koinViewModel(),
    sIdItinerarioDetalle: String,
    sTipoImagen: String,
    vmRegistroRecoleccion: clRegistroRecoleccionViewModel,
    )  {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if(vmCamara.state.value.capturedImage == null) {
                Image(painter = painterResource(id = R.drawable.camera),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            capturePhoto(
                                context,
                                cameraController,
                                onPhotoCaptured,
                                sIdItinerarioDetalle,
                                sTipoImagen,
                                vmRegistroRecoleccion,
                            )
                        },
                    colorFilter = ColorFilter.tint(color = androidx.compose.ui.graphics.Color.White)

                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues: PaddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
            if (lastCapturedPhoto != null) {
                LastPhotoPreview(
                    modifier = Modifier.align(alignment = BottomStart),
                    lastCapturedPhoto = lastCapturedPhoto,
                    navController,
                    vmCamara,
                    sIdItinerarioDetalle,
                    vmRegistroRecoleccion,
                    context
                )
            }
        }
    }
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: KFunction5<Bitmap, String, String, clRegistroRecoleccionViewModel, Boolean, Unit>,
    sIdItinerarioDetalle: String,
    sTipoImagen: String,
    vmRegistroRecoleccion: clRegistroRecoleccionViewModel
){
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            var xGrados = image.imageInfo.rotationDegrees
            when (xGrados){
                180 -> xGrados = 0
                0 -> xGrados = 180
            }
            val correctedBitmap: Bitmap = image
                .toBitmap()
                .rotateBitmap(xGrados)

            onPhotoCaptured(correctedBitmap, sIdItinerarioDetalle, sTipoImagen, vmRegistroRecoleccion, false)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

@Composable
private fun LastPhotoPreview(
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap,
    navController: NavController,
    vmCamara: clCameraViewModel = koinViewModel(),
    sIdItinerarioDetalle: String,
    vmRegistroRecoleccion: clRegistroRecoleccionViewModel,
    context: Context

): Boolean {
    var bSuccess: Boolean = false
    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }
    Box(contentAlignment = Center){
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = androidx.compose.ui.graphics.Color.Black)
                .fillMaxSize()

        ){
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Image(painter = painterResource(id = R.drawable.cancel),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            bSuccess = false
                            clSavePhotoToGalleryUseCase(context).deleteImage()
                            vmRegistroRecoleccion.deleteLastImgItinerario(sIdItinerarioDetalle.toInt())
                            navController.popBackStack("RegistroRecoleccion/${sIdItinerarioDetalle.toInt()}", false)
                            vmCamara.state.value.capturedImage = null
                        }
                )
                HSpace(80)
//
                Image(painter = painterResource(id = R.drawable.cam_check),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            bSuccess = true
                            navController.popBackStack("RegistroRecoleccion/${sIdItinerarioDetalle.toInt()}", false)
                            vmCamara.state.value.capturedImage = null
                        }
                )

            }
        }
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                bitmap = capturedPhoto,
                contentDescription = "Last captured photo",
            )
        }
    }
    return bSuccess
}

@Preview
@Composable
private fun Preview_CameraContent() {

}

