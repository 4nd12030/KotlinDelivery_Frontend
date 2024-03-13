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
import com.andy.kotlindelivery.activities.client.shopping_bag.ClientShoppingBagActivity
import com.andy.kotlindelivery.activities.restaurant.home.RestaurantHomeActivity
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlin.math.sign

class ShoppingBagAdapters (val context: Activity, val productos:ArrayList<Producto>): RecyclerView.Adapter<ShoppingBagAdapters.ShoppingBagViewHolder>() {

    val sharedPref = SharedPref(context)

    init{
        (context as ClientShoppingBagActivity).setTotal(getTotal())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_bag ,  parent, false) ///instacias la vista en la que se esta trabajando
        return ShoppingBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {
        val producto = productos[position] // cada una de las categorias
        Log.d("Productos adapter",  "Productos : $producto")

        holder.textViewNombre.text = producto.nombre
        holder.textViewPrecio.text = "$${producto.precio * producto.quantity!!}"
        holder.textViewContador.text = "${producto.quantity}"
        Glide.with(context).load(producto.imagen1).into(holder.imageViewProducto)

        holder.imageViewAdd.setOnClickListener { addItem(producto, holder) }
        holder.imageViewRemove.setOnClickListener { removeItem(producto, holder) }
        holder.imageViewDelete.setOnClickListener { deleteProducto(position) }
//        holder.itemView.setOnClickListener { goToDetail( producto ) }

    }

    private fun getTotal(): Double{
        var total = 0.0
        for(p in productos) {
            total = total + (p.quantity!! + p.precio)
        }

        return total
    }


    private fun deleteProducto(position: Int) {
        productos.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, productos.size)
        sharedPref.save("order", productos)
        (context as ClientShoppingBagActivity).setTotal(getTotal())
    }


    ///// FUNCIONES PARA INCREMENTAR Y DECREMENTAR LA CANTIDAD DE PRODUCTOS
    private fun getIndexOf(idProducto: String): Int{
        var pos = 0
        for(p in productos)
            if(p.id == idProducto){
                return pos
            }
        pos ++
        return -1
    }

    private fun addItem(producto: Producto, holder: ShoppingBagViewHolder){
        val index = getIndexOf(producto.id!!)
        producto.quantity = producto.quantity!! +1
        productos[index].quantity = producto.quantity

        holder.textViewContador.text = "${producto?.quantity}"
        holder.textViewPrecio.text = "$${producto.quantity!! * producto.precio}"

        sharedPref.save("order", productos)
        (context as ClientShoppingBagActivity).setTotal(getTotal())
    }

    private fun removeItem(producto: Producto, holder: ShoppingBagViewHolder){
        if(producto.quantity!! > 1) {

            val index = getIndexOf(producto.id!!)
            producto.quantity = producto.quantity!! -1
            productos[index].quantity = producto.quantity

            holder.textViewContador.text = "${producto?.quantity}"
            holder.textViewPrecio.text = "$${producto.quantity!! * producto.precio}"

            sharedPref.save("order", productos)
            (context as ClientShoppingBagActivity).setTotal(getTotal())
        }
    }


//    private fun goToDetail( producto: Producto) {
//        val i =  Intent(context, ClientProductsDetailActivity::class.java)
//        i.putExtra("producto", producto.toJson())
//        context.startActivity(i)
//    }

    ///INICIALIZA Y ASIGNA LAS VARIABLES DEL RECYCLERVIEW
    class ShoppingBagViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewNombre: TextView
        val textViewPrecio: TextView
        val imageViewProducto: ImageView
        val textViewContador: TextView
        val imageViewRemove: ImageView
        val imageViewAdd: ImageView
        val imageViewDelete: ImageView

        init {
            textViewNombre = view.findViewById(R.id.textview_nombre)
            textViewPrecio = view.findViewById(R.id.textview_precio)
            textViewContador = view.findViewById(R.id.textview_contador)
            imageViewRemove = view.findViewById(R.id.image_remove)
            imageViewAdd = view.findViewById(R.id.image_add)
            imageViewDelete = view.findViewById(R.id.image_delete)
            imageViewProducto = view.findViewById(R.id.imageview_producto)
        }
    }
}