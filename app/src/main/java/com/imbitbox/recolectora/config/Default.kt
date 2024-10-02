package com.imbitbox.recolectora.config

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.*
import org.koin.androidx.viewmodel.dsl.viewModel
public fun KoinApplication.defaultModule(): KoinApplication = modules(defaultModule)
public val defaultModule : Module = module {
    factory() { com.imbitbox.recolectora.classes.clSavePhotoToGalleryUseCase(context=get()) }
    viewModel() { com.imbitbox.recolectora.viewModel.clCameraViewModel(savePhotoToGalleryUseCase=get()) }
}