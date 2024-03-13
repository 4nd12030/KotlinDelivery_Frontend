package com.andy.kotlindelivery.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Categoria(
    val id: String? = null,
    val nombre: String,
    val image: String? = null
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        //return "Categoria(id=$id, nombre=$nombre, image=$image)"
        return nombre
    }
}