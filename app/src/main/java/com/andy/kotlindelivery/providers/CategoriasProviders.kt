package com.andy.kotlindelivery.providers

import com.andy.kotlindelivery.api.ApiRoutes
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.routers.CategoriasRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriasProviders(val token: String) {
    private var categoriasRoutes: CategoriasRoutes? = null

    init {
        val api = ApiRoutes()
        categoriasRoutes = api.getCategoriasRoutesGetToken(token)
    }

    fun getAll(): Call<ArrayList<Categoria>> ?{
        return categoriasRoutes?.getAll(token)
    }

    fun create(file: File, categoria: Categoria): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), categoria.toJson())

        return categoriasRoutes?.create(image, requestBody, token!!)
    }

}