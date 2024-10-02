package com.imbitbox.recolectora.models.tools

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class clDataStore(private val context: Context) : ViewModel() {
    companion object{
        private val Context.dataStore: DataStore<Preferences>  by preferencesDataStore("appDataStore")
        val bStoreBoard = booleanPreferencesKey("bStoreBoard")
        val bStoreLogin = booleanPreferencesKey("bStoreLogin")
        val sStoreUser = stringPreferencesKey("sStoreUser")
        val sStoreRuta = stringPreferencesKey("sStoreRuta")
        val sStoreVehiculo = stringPreferencesKey("sStoreVehiculo")
        val sStoreSucursal = stringPreferencesKey("sStoreSucursal")
    }

    val getStoreBoard: Flow<Boolean> = context.dataStore.data
        .map { preference ->
            preference[bStoreBoard] ?: false
        }
    val getStoreLogin: Flow<Boolean> = context.dataStore.data
        .map { preference ->
            preference[bStoreLogin] ?: false
        }
    val getStoreUser: Flow<String> = context.dataStore.data
        .map { preference ->
            preference[sStoreUser] ?: ""
        }
    val getStoreRuta: Flow<String> = context.dataStore.data
        .map { preference ->
            preference[sStoreRuta] ?: ""
        }
    val getStoreVehiculo: Flow<String> = context.dataStore.data
        .map { preference ->
            preference[sStoreVehiculo] ?: ""
        }
    val getStoreSucursal: Flow<String> = context.dataStore.data
        .map { preference ->
            preference[sStoreSucursal] ?: ""
        }

    fun saveStoreBoard(value: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[bStoreBoard] = value
                }
            }
        }
    }
    fun saveStoreLogin(value: Boolean){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[bStoreLogin] = value
                }
            }
        }
    }
    fun saveStoreUser(value: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[sStoreUser] = value
                }
            }
        }
    }
    fun saveStoreRuta(value: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[sStoreRuta] = value
                }
            }
        }
    }
    fun saveStoreVehiculo(value: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[sStoreVehiculo] = value
                }
            }
        }
    }
    fun saveStoreSucursal(value: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[sStoreSucursal] = value
                }
            }
        }
    }
}