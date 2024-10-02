package com.imbitbox.recolectora.classes
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import coil.ImageLoader
import com.google.android.datatransport.runtime.dagger.Component
import com.imbitbox.recolectora.viewModel.clRegistroRecoleccionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date

@Component.Factory
class clSavePhotoToGalleryUseCase(
    private val context: Context,
) {
    @SuppressLint("SimpleDateFormat")
    suspend fun call(capturePhotoBitmap: Bitmap, sIdItinerarioDetalle: String, sTipoImagen : String, vmRegistroRecoleccion: clRegistroRecoleccionViewModel, bEliminar: Boolean): Result<Unit> = withContext(Dispatchers.IO) {

        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val imageFileName = sIdItinerarioDetalle + "-" + timeStamp + "-" + sTipoImagen + ".jpg"
        val sPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val resolver: ContentResolver = context.applicationContext.contentResolver

        val imageCollection: Uri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        // Publish a new image.
        val nowTimestamp: Long = System.currentTimeMillis()
        val imageContentValues: ContentValues = ContentValues().apply {

            put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.DATE_TAKEN, nowTimestamp)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.Images.Media.DATE_TAKEN, nowTimestamp)
                put(MediaStore.Images.Media.DATE_ADDED, nowTimestamp)
                put(MediaStore.Images.Media.DATE_MODIFIED, nowTimestamp)
                put(MediaStore.Images.Media.AUTHOR, "Your Name")
                put(MediaStore.Images.Media.DESCRIPTION, "Your description")
            }
        }

        val imageMediaStoreUri: Uri? = resolver.insert(imageCollection, imageContentValues)

        // Write the image data to the new Uri.
        val result: Result<Unit> = imageMediaStoreUri?.let { uri ->
            kotlin.runCatching {
                resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
                    checkNotNull(outputStream) { "Couldn't create file for gallery, MediaStore output stream is null" }
                    capturePhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageContentValues.clear()
                    imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, imageContentValues, null, null)
                    setMyData(resolver,uri)
                }
                vmRegistroRecoleccion.saveImage(sIdItinerarioDetalle, sTipoImagen, imageFileName, sPath.path)

                Result.success(Unit)


            }.getOrElse { exception: Throwable ->
                exception.message?.let(::println)
                resolver.delete(uri, null, null)
                Result.failure(exception)
            }
        } ?: run {
            Result.failure(Exception("Couldn't create file for gallery"))
        }

        return@withContext result
    }

    fun deleteImage(){
        myImage.Uri?.let { myImage.ImageResolver?.delete(it,null,null) }
    }
}

//Carga Valores de imagen para guardarla o no
data class clImage(
     var ImageResolver: ContentResolver?,
     var Uri: Uri?
 )

var myImage by mutableStateOf(clImage(null, null))
fun setMyData(ImageResolver: ContentResolver, Uri: Uri) {
    myImage = myImage.copy(ImageResolver = ImageResolver)
    myImage = myImage.copy(Uri = Uri)
}


