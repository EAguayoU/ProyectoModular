package com.imbitbox.recolectora.models.tools
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class clBarCode(private val context: Context, navController: NavController) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    val scanner = BarcodeScanning.getClient(options)
    var sCodeValue: String = ""
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image
            ?.let { image ->
                scanner.process(
                    InputImage.fromMediaImage(
                        image, imageProxy.imageInfo.rotationDegrees
                    )
                ).addOnSuccessListener { barcode ->
                    barcode?.takeIf { it.isNotEmpty() }
                        ?.mapNotNull { it.rawValue }
                        ?.joinToString(",")
                        ?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            sCodeValue = it
                            if(sCodeValue != ""){
                                imageProxy.close()
                                println(sCodeValue)
                            }
                        }
                }.addOnCompleteListener {
                    imageProxy.close()

                }


            }
    }

}

//public static final int FORMAT_UNKNOWN = -1;
//public static final int FORMAT_ALL_FORMATS = 0;
//public static final int FORMAT_CODE_128 = 1;
//public static final int FORMAT_CODE_39 = 2;
//public static final int FORMAT_CODE_93 = 4;
//public static final int FORMAT_CODABAR = 8;
//public static final int FORMAT_DATA_MATRIX = 16;
//public static final int FORMAT_EAN_13 = 32;
//public static final int FORMAT_EAN_8 = 64;
//public static final int FORMAT_ITF = 128;
//public static final int FORMAT_QR_CODE = 256;
//public static final int FORMAT_UPC_A = 512;
//public static final int FORMAT_UPC_E = 1024;
//public static final int FORMAT_PDF417 = 2048;
//public static final int FORMAT_AZTEC = 4096;