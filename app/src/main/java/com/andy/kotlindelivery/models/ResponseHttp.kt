package com.andy.kotlindelivery.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class ResponseHttp(
    @SerializedName("message") val message : String,
    @SerializedName("succes") val isSucces : Boolean,
    @SerializedName("data") val data : JsonObject,
    @SerializedName("error") val error : String,
    ) {
    override fun toString(): String {
        return "ResponseHttp(message='$message', isSucces=$isSucces, data=$data, error='$error')"
    }
}