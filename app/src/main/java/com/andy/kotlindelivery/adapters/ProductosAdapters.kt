package com.andy.kotlindelivery.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.activities.client.products.details.ClientProductsDetailActivity
import com.andy.kotlindelivery.activities.client.products.list.ClientProductsListActivity
import com.andy.kotlindelivery.activities.restaurant.home.RestaurantHomeActivity
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson

class ProductosAdapters (val context: Activity, val productos:ArrayList<Producto>): RecyclerView.Adapter<ProductosAdapters.ProductosViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_productos,  parent, false) ///instacias la vista en la que se esta trabajando
        return ProductosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {
        val producto = productos[position] // cada una de las categorias
        Log.d("Productos adapter",  "Productos : $producto")

        holder.textViewNombre.text = producto.nombre
        holder.textViewPrecio.text = "$${producto.precio}"
        Glide.with(context).load(producto.imagen1).into(holder.imageViewProducto)
        holder.itemView.setOnClickListener { goToDetail( producto ) }
    }

    private fun goToDetail( producto: Producto) {
        val i =  Intent(context, ClientProductsDetailActivity::class.java)
        i.putExtra("producto", producto.toJson())
        context.startActivity(i)
    }

    class ProductosViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewNombre: TextView
        val textViewPrecio: TextView
        val imageViewProducto: ImageView

        init {
            textViewNombre = view.findViewById(R.id.txtview_nombre)
            textViewPrecio = view.findViewById(R.id.txtview_precio)
            imageViewProducto = view.findViewById(R.id.imageview_producto)
        }
    }
}