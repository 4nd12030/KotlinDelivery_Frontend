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
import com.andy.kotlindelivery.activities.client.products.list.ClientProductsListActivity
import com.andy.kotlindelivery.activities.restaurant.home.RestaurantHomeActivity
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.utils.SharedPref
import com.bumptech.glide.Glide

class CategoriasAdapters (val context: Activity, val categorias:ArrayList<Categoria>): RecyclerView.Adapter<CategoriasAdapters.CategoriasViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categorias,  parent, false) ///instacias la vista en la que se esta trabajando
        return CategoriasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categorias.size
    }

    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {
        val categoria = categorias[position] // cada una de las categorias
        Log.d("Categorias adapter",  "Categoiras : $categoria")

        holder.textViewCategoria.text = categoria.nombre
        Glide.with(context).load(categoria.image).into(holder.imageViewCategoria)
         holder.itemView.setOnClickListener { goToProductos( categoria ) }
    }

    private fun goToProductos( categoria: Categoria) {
        val i =  Intent(context, ClientProductsListActivity::class.java)
        i.putExtra("idCategoria", categoria.id)
        context.startActivity(i)
    }

    class CategoriasViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewCategoria: TextView
        val imageViewCategoria: ImageView

        init {
            textViewCategoria = view.findViewById(R.id.textview_category)
            imageViewCategoria = view.findViewById(R.id.imageview_category)
        }
    }
}