package com.imbitbox.recolectora.di

import android.content.Context
import androidx.room.Room
import com.imbitbox.recolectora.Interfaces.IItinerario
import com.imbitbox.recolectora.Interfaces.iItinerarioDetalleDB
import com.imbitbox.recolectora.Interfaces.IUsuario
import com.imbitbox.recolectora.Interfaces.iItinerarioDetalle
import com.imbitbox.recolectora.Interfaces.iRegistroRecoleccion
import com.imbitbox.recolectora.Interfaces.iVehiculo
import com.imbitbox.recolectora.dataBase.clDataBaseRoom
import com.imbitbox.recolectora.helpers.clConstant.Companion.sUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//Configuracion de Injeccion de dependencias
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //APIS
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(sUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun providesApi(retrofit: Retrofit) : IUsuario {
        return retrofit.create(IUsuario::class.java)
    }
    @Singleton
    @Provides
    fun providesItinerarioApi(retrofit: Retrofit) : IItinerario {
        return retrofit.create(IItinerario::class.java)
    }
    @Singleton
    @Provides
    fun providesVehiculoApi(retrofit: Retrofit) : iVehiculo {
        return retrofit.create(iVehiculo::class.java)
    }
    @Singleton
    @Provides
    fun providesItinerarioDetalleApi(retrofit: Retrofit) : iItinerarioDetalle {
        return retrofit.create(iItinerarioDetalle::class.java)
    }
    @Singleton
    @Provides
    fun providesRegistroRecoleccionApi(retrofit: Retrofit) : iRegistroRecoleccion {
        return retrofit.create(iRegistroRecoleccion::class.java)
    }


    //DATABASE ROOM
    @Singleton
    @Provides
    fun providesItinerarioDetalleDB(objDataBaseRoom: clDataBaseRoom): iItinerarioDetalleDB{
        return objDataBaseRoom.clDataBaseRoom()
    }

    @Singleton
    @Provides
    fun providesItinerarioDetalleRoom(@ApplicationContext context: Context):clDataBaseRoom {
        return Room.databaseBuilder(
            context,
            clDataBaseRoom::class.java,
            "Recolectora_db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}