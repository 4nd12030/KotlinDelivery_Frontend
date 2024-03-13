package com.andy.kotlindelivery.routers

import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface CategoriasRoutes {

    @GET("categorias/getAll")
    fun getAll(
        @Header("Authorization") token: String
    ): Call<ArrayList<Categoria>>

    @Multipart //Es multipart porque se enviara una imagen
    @POST("categorias/create")
    fun create(
        @Part image: MultipartBody.Part,
        @Part("categoria") categoria: RequestBody, //el nombre debe ser igual al del archivo controller del backend
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}