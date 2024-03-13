package com.andy.kotlindelivery.routers

import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.models.ResponseHttp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductosRoutes {

    @GET("productos/findByCategoria/{id_categoria}")
    fun findByCategoria(
        @Path("id_categoria") idCategoria: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Producto>>

    @Multipart //Es multipart porque se enviara una imagen
    @POST("productos/create")
    fun create(
        @Part images:  Array<MultipartBody.Part?>,
        @Part("producto") producto: RequestBody, //el nombre debe ser igual al del archivo controller del backend
        @Header("Authorization") token: String
    ): Call<ResponseHttp>


}