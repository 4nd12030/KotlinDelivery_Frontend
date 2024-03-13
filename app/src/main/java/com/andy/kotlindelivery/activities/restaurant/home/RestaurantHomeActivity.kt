package com.andy.kotlindelivery.activities.restaurant.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.activities.MainActivity
import com.andy.kotlindelivery.fragments.client.ClientProfileFragment
import com.andy.kotlindelivery.fragments.restaurant.RestaurantCategoryFragment
import com.andy.kotlindelivery.fragments.restaurant.RestaurantOrdersFragment
import com.andy.kotlindelivery.fragments.restaurant.RestaurantProductFragment
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class RestaurantHomeActivity : AppCompatActivity() {

    private val TAG = "RestaurantHomeActivity"
    // var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_home)

        sharedPref = SharedPref(this)
        //Abre el fragment por defecto
        openFragment(RestaurantOrdersFragment())

        //buttonLogout = findViewById(R.id.btn_logout)
        //buttonLogout?.setOnClickListener { logout() }

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId){
                R.id.item_home -> {
                    openFragment(RestaurantOrdersFragment())
                    true
                }
                R.id.item_category -> {
                    openFragment(RestaurantCategoryFragment())
                    true
                }
                R.id.item_product -> {
                    openFragment(RestaurantProductFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }

        getUserFormSession()
    }

    /// barra de navegacion
    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    private  fun logout(){
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFormSession() {
        //val sharedPref = SharedPref(this,)
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            val user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
            Log.d(TAG, "Usuario: $user")
        }
    }

}