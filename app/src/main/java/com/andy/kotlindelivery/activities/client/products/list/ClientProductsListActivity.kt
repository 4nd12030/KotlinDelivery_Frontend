package com.andy.kotlindelivery.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.adapters.CategoriasAdapters
import com.andy.kotlindelivery.adapters.ProductosAdapters
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.CategoriasProviders
import com.andy.kotlindelivery.providers.ProductosProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {

    var TAG = "ClientProductsListActivity"
    var recyclerViewProductos: RecyclerView? = null
    var adapter: ProductosAdapters? = null
    var usuario: Usuario? = null
    var productosProviders: ProductosProviders? = null
    var productos: ArrayList<Producto> = ArrayList()
    var toolbar: Toolbar? = null
    var sharedPref: SharedPref? = null

    var idCategoria: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)

        idCategoria = intent.getStringExtra("idCategoria")
        Log.d(TAG,"Id categoria: $idCategoria")

        recyclerViewProductos = findViewById(R.id.recyclerview_productos)
        recyclerViewProductos?.layoutManager = GridLayoutManager(this, 2)

        toolbar =findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Productos"
        setSupportActionBar(toolbar)

        sharedPref = SharedPref(this)
        getUserFromSession()
        productosProviders = ProductosProviders(usuario?.sessionToken!!)

        getProductos()
    }

    private fun getProductos(){
        productosProviders?.findByCategoria(idCategoria!!)?.enqueue(object: Callback<ArrayList<Producto>> {
            override fun onResponse(
                call: Call<ArrayList<Producto>>,
                response: Response<ArrayList<Producto>>
            ) {
                Log.d(TAG,"Response: ${response.body()}")
                if(response.body() != null){
                    productos = response.body()!!
                    adapter = ProductosAdapters(this@ClientProductsListActivity, productos)
                    recyclerViewProductos?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Producto>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity, t.message, Toast.LENGTH_LONG).show()
                Log.d(TAG,"Error: ${t.message}")
            }

        })
    }


    /////funciones de sesion
    private fun getUserFromSession() {
        //val sharedPref = SharedPref(this,)
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
            Log.d(TAG, "Usuario: $usuario")
        }
    }

}