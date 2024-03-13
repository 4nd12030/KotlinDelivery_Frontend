package com.andy.kotlindelivery.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.activities.client.home.ClientHomeActivity
import com.andy.kotlindelivery.activities.delivery.home.DeliveryHomeActivity
import com.andy.kotlindelivery.activities.restaurant.home.RestaurantHomeActivity
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.UsuariosProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var imageViewGotoRegister: ImageView? = null
    var editTxtEmail: EditText? = null
    var editTxTContrasena: EditText? = null
    var btnLogin: Button? = null

    var usuarioProvider = UsuariosProviders()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageViewGotoRegister = findViewById(R.id.imgview_goregister)
        editTxtEmail = findViewById(R.id.edittxt_email)
        editTxTContrasena = findViewById(R.id.edittxt_contrasena)
        btnLogin = findViewById(R.id.btn_login)

        imageViewGotoRegister?.setOnClickListener{ goToRegister() }
        btnLogin?.setOnClickListener { login() }

        getUserFromSession()
    }

    private fun login(){
        val email = editTxtEmail?.text.toString() //NULL POINTE EXCEPTION
        val contrasena = editTxTContrasena?.text.toString()

        if (isValidForm(email,contrasena)) {
            usuarioProvider.login(email,contrasena)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d("MainActivity", "Respuesta: ${response.body()}" )
                    Log.d("MainActivity", "Datos enviados: ${email} " )
                    Log.d("MainActivity", "Datos enviados: ${contrasena} " )

                    if(response.body()?.isSucces == true) {
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        saveUserInSession(response.body()?.data.toString())
                        //goToClientHome()
                    }
                    else {
                        Toast.makeText(this@MainActivity, "Los datos son incorrectos1", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("MainActivity", "Hubo un error ${t.message}")
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        } else {
            Toast.makeText(this@MainActivity, "No es valido", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //ELIMINA EL HISTORIAL DE PANTALLA
        startActivity(i)
    }

    private fun goToRestaurantHome() {
        val i = Intent(this,  RestaurantHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToDeliveryHome() {
        val i = Intent(this, DeliveryHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToRolSelect() {
        val i = Intent(this, SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
    private fun saveUserInSession(data: String) {
        val sharedPreferences = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPreferences.save("user", user)

        if (user.roles?.size!! > 1){ //tiene mas de un rol
            goToRolSelect()
        } else { //tiene un rol
            goToClientHome()
        }
    }
    fun String.isEmailValid() : Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    //Ya que la sesion ha sido iniciada nos mandara directamente a la pantalla de ClientHomeActivity
    private fun getUserFromSession() {

        val sharedPref = SharedPref(this,)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            val user = gson.fromJson(sharedPref.getData("user"), Usuario::class.java)
            Log.d("MainActivity","Usuario: $user")

            if(!sharedPref.getData("rol").isNullOrBlank()) {
                val rol = sharedPref.getData("rol")?.replace("\"", "")
                Log.d("MainActivity", "Rol $rol")
                when( rol ){
                    "RESTAURANTE" -> goToRestaurantHome()
                    "REPARTIDOR" -> goToDeliveryHome()
                    "CLIENTE" -> goToClientHome()
                    else -> goToClientHome()
                }

            }
        }
    }

    private fun isValidForm(email:String, contrasena:String) : Boolean{
        if(email.isBlank()) {
            Toast.makeText(this,"Debes ingresar el email", Toast.LENGTH_LONG).show()
            return false
        }
        if(contrasena.isBlank()) {
            Toast.makeText(this,"Debes ingresar la contraseña", Toast.LENGTH_LONG).show()
            return false
        }
        if(!email.isEmailValid()) {
            return false
        }
        return true
    }

    private fun goToRegister(){
        val i = Intent( this, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }
}