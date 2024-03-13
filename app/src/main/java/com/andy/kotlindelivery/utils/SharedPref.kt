package com.andy.kotlindelivery.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SharedPref(activity:Activity) {
    private var prefs: SharedPreferences? = null

    init {
        prefs = activity.getSharedPreferences("com.andy.kotlindelivery", Context.MODE_PRIVATE)
    }

    //metodo que guarda la sesion
    fun save(key:String, objeto: Any) {
        try{
            val gson = Gson()
            val json = gson.toJson(objeto)
            with(prefs?.edit()) {
                this?.putString(key, json)
                this?.commit()
            }
        }catch (e: Exception) {
            Log.d("ERROR", "Err: ${e.message}")
        }
    }

    //metodo que obtiene la data  del metodo  save()
    fun getData(key: String): String? {
       val data = prefs?.getString(key, "")
        return data
    }

    // FUNCION PARA CERRAR SESION
    fun remove(key: String){
        prefs?.edit()?.remove(key)?.apply()
    }

}
