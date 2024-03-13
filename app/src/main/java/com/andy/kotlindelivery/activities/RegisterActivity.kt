package com.andy.kotlindelivery.activities

import android.content.ContentValues.TAG
import android.content.Intent
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
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.UsuariosProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    var imageViewLogin: ImageView? = null
    var editTxtNombre : EditText? = null
    var editTxtApellido : EditText? = null
    var editTxtEmailRegs : EditText? = null
    var editTxtTelefono : EditText? = null
    var editTxtContrasenaRegs : EditText? = null
    var editTxtContrasenaConfirm : EditText? = null
    var btnRegistro : Button? = null

    //
    var usuariosProviders = UsuariosProviders()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageViewLogin = findViewById(R.id.imgview_gologin)
        editTxtNombre  = findViewById(R.id.edittxt_nombre)
        editTxtApellido = findViewById(R.id.edittxt_apellido)
        editTxtEmailRegs = findViewById(R.id.edittxt_emailregister)
        editTxtTelefono  = findViewById(R.id.edittxt_telelfono)
        editTxtContrasenaRegs = findViewById(R.id.edittxt_contrasenaregister)
        editTxtContrasenaConfirm = findViewById(R.id.edittxt_contrasenaconfirm)
        btnRegistro = findViewById(R.id.btn_registro)

        imageViewLogin?.setOnClickListener{ gotoLogin() }
        btnRegistro?.setOnClickListener { register() }
    }

    private fun register(){
        var nombre = editTxtNombre?.text.toString()
        var apellido = editTxtApellido?.text.toString()
        var emailRegistro = editTxtEmailRegs?.text.toString()
        var telefono = editTxtTelefono?.text.toString()
        var contrasenaRegistro = editTxtContrasenaRegs?.text.toString()
        var contrasenaConfirmacion = editTxtContrasenaConfirm?.text.toString()

        if(isValidForm(nombre=nombre,
                apellido=apellido,
                emailRegistro=emailRegistro,
                telefono=telefono,
                contrasenaRegistro=contrasenaRegistro,
                contrasenaConfirma=contrasenaConfirmacion)){

            val usuario = Usuario(
                nombre = nombre,
                apellido = apellido,
                email = emailRegistro,
                telefono = telefono,
                contrasena = contrasenaRegistro )

            usuariosProviders.register(usuario)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp>  ) {

                    if(response.body()?.isSucces == true) {
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    Log.d( TAG, "Response: $response" )
                    Log.d( TAG, "Body: ${response.body()}" )
                    Log.d(TAG, "Los datos son: ${usuario} ")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        // Toast.makeText(this, "El formulario es valido", Toast.LENGTH_SHORT).show()
        }
    }

    ////////////////////////////////////////
    private fun goToClientHome() {
        //val i = Intent(this, ClientHomeActivity::class.java)
        val i = Intent(this, SaveImageActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
    private fun saveUserInSession(data: String) {
        val sharedPreferences = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPreferences.save("user", user)

    }
    ///////////////////////////////

    fun String.isEmailValid() : Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(
        nombre : String,
        apellido : String,
        emailRegistro : String,
        telefono : String,
        contrasenaRegistro : String,
        contrasenaConfirma : String

    ) : Boolean{
        if(nombre.isBlank()) {
            Toast.makeText(this,"Debes ingresar el nombre", Toast.LENGTH_LONG).show()
            return false
        }
        if(apellido.isBlank()) {
            Toast.makeText(this,"Debes ingresar el apellido", Toast.LENGTH_LONG).show()
            return false
        }
        if(emailRegistro.isBlank()) {
            Toast.makeText(this,"Debes ingresar el email", Toast.LENGTH_LONG).show()
            return false
        }
        if(telefono.isBlank()) {
            Toast.makeText(this,"Debes ingresar el telefono", Toast.LENGTH_LONG).show()
            return false
        }
        if(contrasenaRegistro.isBlank()) {
            Toast.makeText(this,"Debes ingresar la contraseña", Toast.LENGTH_LONG).show()
            return false
        }
        if(contrasenaConfirma.isBlank()) {
            Toast.makeText(this,"Debes volver a introducir la contraseña", Toast.LENGTH_LONG).show()
            return false
        }

        if(!emailRegistro.isEmailValid()) {
            return false
        }
        if(contrasenaRegistro != contrasenaConfirma) {
            Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
        }
        return true
    }


    private fun gotoLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}