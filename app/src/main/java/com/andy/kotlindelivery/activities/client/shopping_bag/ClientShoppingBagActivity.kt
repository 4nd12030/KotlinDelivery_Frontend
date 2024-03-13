package com.andy.kotlindelivery.activities.client.shopping_bag

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.adapters.ShoppingBagAdapters
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientShoppingBagActivity : AppCompatActivity() {

    var recyclerViewShoppingBag: RecyclerView? = null
    var btnNext: Button? = null
    var textViewTotal: TextView? = null
    var toolbar: Toolbar? = null

    var adapter: ShoppingBagAdapters? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var seleeccionarProducto = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shopping_bag)

        sharedPref = SharedPref(this)

        recyclerViewShoppingBag  = findViewById(R.id.recyclerview_shopping_bag)
        btnNext  = findViewById(R.id.btn_next)
        textViewTotal = findViewById(R.id.textview_total)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Tu orden"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //habilita la flecha hacia atras

        recyclerViewShoppingBag?.layoutManager = LinearLayoutManager(this)
        getPoductosFromSharedPref()
    }

    fun setTotal(total: Double){
        textViewTotal?. text = "$${total}"
    }

    private fun  getPoductosFromSharedPref(){
        Log.d("getPoductosFrom", "SahredPref antes del if ${sharedPref?.getData("order")} ")

        if(!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object: TypeToken<ArrayList<Producto>>() {}.type
            seleeccionarProducto = gson.fromJson(sharedPref?.getData("order"), type)

            adapter = ShoppingBagAdapters(this, seleeccionarProducto)
            recyclerViewShoppingBag?.adapter = adapter
        }

    }


}