package com.andy.kotlindelivery.routers

import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UsuariosRoutes {

    @POST("usuarios/create")
    fun register (@Body usuario: Usuario) : Call<ResponseHttp>

    @FormUrlEncoded
    @POST("usuarios/login")
    fun login(@Field("email") email:String,
              @Field("contrasena") contrasena: String) : Call<ResponseHttp>

    @Multipart
    @PUT("usuarios/update")
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("usuarios/updateWithoutImage")
    fun updateWithoutImage(
        @Body usuario: Usuario,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
}