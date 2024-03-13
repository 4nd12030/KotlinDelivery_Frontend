package com.andy.kotlindelivery.providers

import com.andy.kotlindelivery.api.ApiRoutes
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.routers.CategoriasRoutes
import com.andy.kotlindelivery.routers.ProductosRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductosProviders(val token: String) {
    private var productosRoutes: ProductosRoutes? = null

    init {
        val api = ApiRoutes()
        productosRoutes = api.getProductosRoutes(token)
    }

    fun findByCategoria(idCategoria: String): Call<ArrayList<Producto>> ?{
        return productosRoutes?.findByCategoria(idCategoria, token)
    }

    fun create(files: List<File>, producto: Producto): Call<ResponseHttp>? {
        val images = arrayOfNulls< MultipartBody.Part>(files.size)

        for (i in 0 until files.size) {
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }

        val requestBody = RequestBody.create(MediaType.parse("text/plain"), producto.toJson())
        return productosRoutes?.create(images, requestBody, token!!)
    }

}