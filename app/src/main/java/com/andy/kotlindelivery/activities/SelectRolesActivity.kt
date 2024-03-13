package com.andy.kotlindelivery.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.adapters.RolesAdapters
import com.andy.kotlindelivery.models.Rol
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.utils.SharedPref
import com.google.gson.Gson

class SelectRolesActivity : AppCompatActivity() {

    var recyclerViewRoles: RecyclerView? = null
    var user: Usuario? = null
    var adapter: RolesAdapters? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles = findViewById(R.id.recyclerview_roles)
        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapters(this, user?.roles!!)
        recyclerViewRoles?.adapter = adapter
    }

    private fun getUserFromSession() {

        val sharedPref = SharedPref(this,)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            user = gson.fromJson(sharedPref.getData("user"), Usuario::class.java)
        }
    }
}