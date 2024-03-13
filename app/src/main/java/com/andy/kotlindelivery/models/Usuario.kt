package com.andy.kotlindelivery.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Usuario (
    @SerializedName("id") var id : String? = null,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("apellido") var apellido : String,
    @SerializedName("email") val email:String,
    @SerializedName("telefono") var telefono : String,
    @SerializedName("contrasena") val contrasena:String,
    @SerializedName("image") var image : String? = null,
    @SerializedName("session_token") val sessionToken:String? = null,
    @SerializedName("is_available") val is_available : Boolean? = null,
    @SerializedName("roles") val roles: ArrayList<Rol>? = null //array de objetos
) {
    override fun toString(): String {
        return "Usuario(id=$id, nombre='$nombre', apellido='$apellido', email='$email', telefono='$telefono', contrasena='$contrasena', image=$image, sessionToken=$sessionToken, is_available=$is_available, roles=$roles)"
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}