package com.andy.kotlindelivery.providers

import com.andy.kotlindelivery.api.ApiRoutes
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.routers.UsuariosRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsuariosProviders(val token: String? = null) {
    private var usuariosRoutes: UsuariosRoutes? = null
    private var usuariosRoutesToken: UsuariosRoutes? = null

    init {
        val api =ApiRoutes()
        usuariosRoutes =api.getUsuariosRoutes()
        if(token != null){
            usuariosRoutesToken = api.getUsuariosRoutesGetToken(token!!)
        }

    }

    fun register(usuario: Usuario): Call<ResponseHttp>?{
        return usuariosRoutes?.register(usuario)
    }

    fun login(email: String, contrasena:String): Call<ResponseHttp>?{
        return usuariosRoutes?.login(email, contrasena)
    }

    fun update(file:File, usuario: Usuario):Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), usuario.toJson())

        return usuariosRoutesToken?.update(image, requestBody, token!!)
    }

    fun updateWhitoutImage(usuario: Usuario): Call<ResponseHttp>?{
        return usuariosRoutesToken?.updateWithoutImage(usuario, token!!)
    }
}