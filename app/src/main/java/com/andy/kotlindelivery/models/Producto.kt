package com.andy.kotlindelivery.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Producto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio")  val precio: Double,
    @SerializedName("image1") val imagen1: String? = null,
    @SerializedName("image2") val imagen2: String? = null,
    @SerializedName("image3") val imagen3: String? = null,
    @SerializedName("id_categoria")  val idCategoria: String,
    @SerializedName("quantity")  var quantity: Int? = null,
) {


    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Producto(id=$id, nombre='$nombre', descripcion='$descripcion', precio=$precio, imagen1=$imagen1, imagen2=$imagen2, imagen3=$imagen3, idCategoria='$idCategoria', quantity=$quantity)"
    }
}