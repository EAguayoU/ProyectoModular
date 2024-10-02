package com.imbitbox.recolectora.views.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.imbitbox.recolectora.Interfaces.iSignedListener
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.classes.Signature.clSignaturePad
import com.imbitbox.recolectora.classes.setMyData
import com.imbitbox.recolectora.components.AlertDialogOnly
import com.imbitbox.recolectora.components.AlertDialogYesNo
import com.imbitbox.recolectora.components.BtnIcon
import com.imbitbox.recolectora.components.BtnStandar
import com.imbitbox.recolectora.components.HSpace
import com.imbitbox.recolectora.ui.theme.clrAzul
import com.imbitbox.recolectora.ui.theme.clrVerde
import com.imbitbox.recolectora.viewModel.clRegistroRecoleccionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date

@SuppressWarnings("LongParameterList")
@Composable
fun SignatureScreenView(modifier: Modifier = Modifier,
                        penMinWidth: Dp = 3.dp,
                        penMaxWidth: Dp = 7.dp,
                        penColor: Color = Color.Black,
                        velocityFilterWeight: Float = 0.9F,
                        clearOnDoubleClick: Boolean = false,
                        onReady: (svg: SignaturePadAdapter) -> Unit = {},
                        onStartSigning: () -> Unit = {},
                        onSigning: () -> Unit = {},
                        onSigned: () -> Unit = {},
                        onClear: () -> Unit = {},
) {
    AndroidView(
        modifier = modifier,

        factory = { context ->
            // Creates custom view
            clSignaturePad(context, null).apply {
                this.setOnSignedListener(object : iSignedListener {

                    override fun onStartSigning() {
                        onStartSigning()
                    }

                    override fun onSigning() {
                        onSigning()
                    }

                    override fun onSigned() {
                        onSigned()
                    }

                    override fun onClear() {
                        onClear()
                    }
                })
            }
        },
        update = {
            it.setMinWidth(penMinWidth.value)
            it.setMaxWidth(penMaxWidth.value)
            it.setPenColor(penColor.toArgb())
            it.setVelocityFilterWeight(velocityFilterWeight)
            it.setClearOnDoubleClick(clearOnDoubleClick)
            onReady(SignaturePadAdapter(it))
        },
    )
}

class SignaturePadAdapter(private val signaturePad: clSignaturePad) {
    fun clear() {
        signaturePad.clear()
    }

    val isEmpty: Boolean
        get() = signaturePad.isEmpty

    @Suppress("unused")
    fun getSignatureBitmap(): Bitmap {
        return signaturePad.getSignatureBitmap()
    }

    @Suppress("unused")
    fun getTransparentSignatureBitmap(): Bitmap {
        return signaturePad.getTransparentSignatureBitmap()
    }

    fun getSignatureSvg(): String {
        return signaturePad.getSignatureSvg()
    }
}
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}
@Composable
fun SignaturePadView(navController: NavController, sIdItinerarioDetalle: String, vmRegistroRecoleccion:clRegistroRecoleccionViewModel){
    var signaturePadAdapter: SignaturePadAdapter? = null
    val sMutableSvg = remember { mutableStateOf("") }
    var bFirma by remember { mutableStateOf(false) }
    var bLimpiar by remember { mutableStateOf(false) }
    var context = LocalContext.current

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Red,
            )
    ) {
        SignatureScreenView(
            onReady = {
                signaturePadAdapter = it
            },
        )
        Column(verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)) {
            BtnStandar(sText = "Guardar", nRoundedCorner = 25, containerColor = clrVerde,
                modifier = Modifier.size(width = 200.dp, height = 35.dp)) {
                sMutableSvg.value = signaturePadAdapter?.getSignatureSvg() ?: ""
                if (!sMutableSvg.value.contains("path")) {
                    bFirma = true
                } else {
                    GuardarSvg(context, sIdItinerarioDetalle, sMutableSvg.value, vmRegistroRecoleccion)
                    navController.popBackStack()
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            BtnStandar(sText = "Limpiar", nRoundedCorner = 25,
                modifier = Modifier.size(width = 200.dp, height = 35.dp)) {
                bLimpiar = true
            }


        }
    }


    if(bFirma) {
        AlertDialogOnly(
            onConfirmation = { bFirma = false },
            dialogTitle = "Se necesita firma",
            dialogText = "Favor de ingresar su firma",
            icon = Icons.Default.Warning
        )
    }
    if(bLimpiar){
        AlertDialogYesNo(
            onDismissRequest = { bLimpiar = false },
            onConfirmation = {
                sMutableSvg.value = ""
                signaturePadAdapter?.clear()
                bLimpiar = false
                             },
            dialogTitle = "Limpiar",
            dialogText = "Estas seguro que deseas limpiar tu firma",
            icon = Icons.Default.Warning
        )
    }
}
@SuppressLint("SimpleDateFormat")
fun GuardarSvg(context: Context, sIdItinerarioDetalle: String, sData: String, vmRegistroRecoleccion: clRegistroRecoleccionViewModel) {
    try {
        val timeStamp = SimpleDateFormat("yyyyMMdd").format(Date())
        val imageFileName = sIdItinerarioDetalle + "-" + timeStamp + "-Firma.svg"
        val sPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        File(sPath.path + "/" + imageFileName).writeText(sData)
        vmRegistroRecoleccion.saveFirma(sIdItinerarioDetalle, imageFileName)
        vmRegistroRecoleccion.saveRutaFirma(sIdItinerarioDetalle, sPath.path)
    }
    catch (error: Exception){
        println("Ocurrio un error al guardar la firma")
    }
}


