plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
//    id("androidx.room")
}

android {

    namespace = "com.imbitbox.recolectora"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.imbitbox.recolectora"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    //implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.core.android)

    //-- Lottie images
    //noinspection UseTomlInstead
    implementation("com.airbnb.android:lottie-compose:6.0.1")

    //-- Retrofit (Consumo apis)
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //-- Dagger Hilt (inyección de dependencias)
    //noinspection UseTomlInstead,GradleDependency
    implementation ("com.google.dagger:hilt-android:2.46.1")
    //implementation(libs.androidx.lifecycle.runtime.compose.android)
    //noinspection UseTomlInstead
    kapt("com.google.dagger:hilt-compiler:2.48.1")

    //Base de datos Locales con Room
    //noinspection UseTomlInstead
    implementation("androidx.room:room-ktx:2.6.1")
    //noinspection KaptUsageInsteadOfKsp,UseTomlInstead
    kapt("androidx.room:room-compiler:2.6.1")


    //-- Coil (Imagenes por URL)
    //noinspection UseTomlInstead
    implementation ("io.coil-kt:coil-compose:2.4.0")
    //-- DataStore
    //noinspection UseTomlInstead
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    //Permisos Google
    //noinspection UseTomlInstead
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")

    //Camara y Codigo Barras
    //noinspection UseTomlInstead
    implementation ("androidx.camera:camera-lifecycle:1.3.3")
    //noinspection UseTomlInstead
    implementation ("androidx.camera:camera-camera2:1.3.3")
    //noinspection UseTomlInstead
    //noinspection UseTomlInstead
    implementation ("androidx.camera:camera-view:1.3.3")
    //noinspection UseTomlInstead
    implementation ("androidx.camera:camera-extensions:1.3.3")
    //noinspection UseTomlInstead
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    //Obtenemos Geolocalizacion
    //noinspection UseTomlInstead
    implementation("com.google.android.gms:play-services-location:21.3.0")


    ////DEPENDENCY INJECTION////
    ////KOIN BASE////
    //noinspection UseTomlInstead
    implementation ("io.insert-koin:koin-core:3.5.3")
    //noinspection UseTomlInstead
    implementation ("io.insert-koin:koin-android:3.5.3")
    //noinspection UseTomlInstead
    implementation ("io.insert-koin:koin-androidx-compose:3.4.6")
    //noinspection UseTomlInstead
    implementation ("io.insert-koin:koin-annotations:1.3.1")
    //noinspection UseTomlInstead
    kapt("io.insert-koin:koin-ksp-compiler:1.3.1")

    //LifeCycle
    //noinspection UseTomlInstead
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //noinspection UseTomlInstead
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    //noinspection UseTomlInstead
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    //noinspection UseTomlInstead
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    //noinspection UseTomlInstead
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")

    implementation("commons-net:commons-net:3.11.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}