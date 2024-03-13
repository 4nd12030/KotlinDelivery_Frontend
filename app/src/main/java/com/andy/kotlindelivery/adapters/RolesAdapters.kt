package com.andy.kotlindelivery.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.activities.client.home.ClientHomeActivity
import com.andy.kotlindelivery.activities.delivery.home.DeliveryHomeActivity
import com.andy.kotlindelivery.activities.restaurant.home.RestaurantHomeActivity
import com.andy.kotlindelivery.models.Rol
import com.andy.kotlindelivery.utils.SharedPref
import com.bumptech.glide.Glide

class RolesAdapters (val context: Activity, val roles:ArrayList<Rol>): RecyclerView.Adapter<RolesAdapters.RolesViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles,  parent, false) ///instacias la vista en la que se esta trabajando
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]

        holder.textViewRol.text = rol.nombre
        Glide.with(context).load(rol.imagen).into(holder.imageViewRol)

        holder.itemView.setOnClickListener { goToRol( rol ) }
    }

    private fun goToRol(rol: Rol) {
        val typeActivity = when( rol.nombre) {
            "RESTAURANTE" -> RestaurantHomeActivity::class.java
            "REPARTIDOR"  -> DeliveryHomeActivity::class.java
            else -> ClientHomeActivity::class.java
        }
        sharedPref.save("rol", rol.nombre)

        val i =  Intent(context, typeActivity)
        context.startActivity(i)
    }

    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewRol: TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.textView_rol)
            imageViewRol = view.findViewById(R.id.imageView_rol)
        }
    }
}