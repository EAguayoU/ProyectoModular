package com.imbitbox.recolectora.viewModel
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imbitbox.recolectora.classes.clCameraState
import com.imbitbox.recolectora.classes.clSavePhotoToGalleryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class clCameraViewModel(
    private val savePhotoToGalleryUseCase: clSavePhotoToGalleryUseCase
) : ViewModel() {

    private var _state = MutableStateFlow(clCameraState())
    var state = _state.asStateFlow()

    fun storePhotoInGallery(bitmap: Bitmap, sIdItinerarioDetalle: String, sTipoImagen: String, vmRegistroRecoleccion: clRegistroRecoleccionViewModel, bEliminar: Boolean) {
        viewModelScope.launch {
            savePhotoToGalleryUseCase.call(bitmap, sIdItinerarioDetalle, sTipoImagen, vmRegistroRecoleccion, bEliminar)
            updateCapturedPhotoState(bitmap)
        }
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }



}