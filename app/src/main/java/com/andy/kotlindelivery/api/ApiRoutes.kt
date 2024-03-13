package com.andy.kotlindelivery.api

import com.andy.kotlindelivery.routers.CategoriasRoutes
import com.andy.kotlindelivery.routers.ProductosRoutes
import com.andy.kotlindelivery.routers.UsuariosRoutes

class ApiRoutes {
    val API_URL = "http://192.168.100.92:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsuariosRoutes(): UsuariosRoutes {
        return retrofit.getClient(API_URL).create(UsuariosRoutes::class.java)
    }

    fun getUsuariosRoutesGetToken(token: String): UsuariosRoutes{
        return retrofit.getClientWebToken(API_URL, token).create(UsuariosRoutes::class.java)
    }

    fun getCategoriasRoutesGetToken(token: String): CategoriasRoutes{
        return retrofit.getClientWebToken(API_URL, token).create(CategoriasRoutes::class.java)
    }

    fun getProductosRoutes(token: String): ProductosRoutes{
        return retrofit.getClientWebToken(API_URL, token).create(ProductosRoutes::class.java)
    }

}