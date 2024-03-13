package com.andy.kotlindelivery.activities.client.products.details

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.utils.SharedPref
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientProductsDetailActivity : AppCompatActivity() {

    var TAG = "ClientProductsDetailActivity"
    var producto:  Producto? = null
    val gson = Gson()

    var imagenSlider: ImageSlider? = null
    var txtviewNombre: TextView? = null
    var txtviewDescripcion: TextView? = null
    var txtviewContador: TextView? = null
    var txtviewPrecio: TextView? = null
    var imageViewAdd: ImageView? = null
    var imageViewRemove: ImageView? = null
    var btnAddProducto: Button? = null

    var contador = 1
    var productoPrecio  =  0.0

    var sharedPref: SharedPref? = null
    var seleeccionarProducto = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)

        producto = gson.fromJson(intent.getStringExtra("producto"), Producto::class.java)
        //Log.d(TAG, "Producto $producto")

        imagenSlider  = findViewById(R.id.imageSlider)
        txtviewNombre = findViewById(R.id.txtview_nombre)
        txtviewDescripcion = findViewById(R.id.txtview_descripcion)
        txtviewContador= findViewById(R.id.textview_contador)
        txtviewPrecio = findViewById(R.id.textview_precio)
        imageViewAdd= findViewById(R.id.image_add)
        imageViewRemove= findViewById(R.id.image_remove)
        btnAddProducto= findViewById(R.id.btn_add_product)

        sharedPref = SharedPref(this)
        Log.d(TAG, "SahredPref al inicio ${sharedPref?.getData("order")} ")

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(producto?.imagen1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(producto?.imagen2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(producto?.imagen3, ScaleTypes.CENTER_CROP))
        //Log.d(TAG, "Array de producto $imageList")
        imagenSlider?.setImageList(imageList)

        txtviewNombre?.text = producto?.nombre
        txtviewDescripcion?.text = producto?.descripcion
        txtviewPrecio?.text = "$${producto?.precio}"

        imageViewAdd?.setOnClickListener { addItem() }
        imageViewRemove?.setOnClickListener { removeItem() }
        btnAddProducto?.setOnClickListener {  addToBag() }

        getPoductosFromSharedPref()

    }

    private fun addToBag(){
        val index = getIndexOf(producto?.id!!) //indice del producto
        Log.d(TAG, "addToBag ->Producto id:  ${producto?.id!!}")
        if(index == -1){  //este producto no existe aun en sharedpref
            if(producto?.quantity == null) {
                producto?.quantity = 1
            }
            seleeccionarProducto.add(producto!!)
        } else { //ya existe el producto en shared pref y se edita la cantidad
            seleeccionarProducto[index].quantity = contador
        }
        sharedPref?.save("order", seleeccionarProducto)
        //Log.d(TAG, "sharedPref:  ${sharedPref}")
        Log.d(TAG, "addToBag:  ${seleeccionarProducto}")
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show()
    }

    private fun  getPoductosFromSharedPref(){
        Log.d("getPoductosFrom", "SahredPref antes del if ${sharedPref?.getData("order")} ")

        if(!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object: TypeToken<ArrayList<Producto>>() {}.type
            seleeccionarProducto = gson.fromJson(sharedPref?.getData("order"), type)

            val index = getIndexOf(producto?.id!!)
            if(index != -1){
                producto?.quantity = seleeccionarProducto[index].quantity
                txtviewContador?.text = "${producto?.quantity}"

                productoPrecio = producto?.precio!! * producto?.quantity!!
                txtviewPrecio?.text = "${productoPrecio}"

                btnAddProducto?.setText("Editar producto")
                btnAddProducto?.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }

            for( p in seleeccionarProducto) {
                Log.d("getPoductosFrom", "Shared pref: $p")
            }
        }

        Log.d("getPoductosFrom", "SahredPref despues del if ${sharedPref?.getData("order")} ")
    }

    //METODO QUE COMPARA SI YA EXISTE EN SHARED PREF Y ASI PODER EDITAR LA CANTIDAD
    private fun getIndexOf(idProducto: String): Int{
        var pos = 0
        for(p in seleeccionarProducto)
            if(p.id == idProducto){
                return pos
        }
        pos ++
        return -1
    }

    private fun addItem(){
        contador++
        productoPrecio = producto?.precio!! * contador
        producto?.quantity = contador
        txtviewContador?.text = "${producto?.quantity}"
        txtviewPrecio?.text = "$${productoPrecio}"
    }

    private fun removeItem(){
        if(contador > 1) {
            contador--
            productoPrecio = producto?.precio!! * contador
            producto?.quantity = contador
            txtviewContador?.text = "${producto?.quantity}"
            txtviewPrecio?.text = "$${productoPrecio}"
        }
    }
}