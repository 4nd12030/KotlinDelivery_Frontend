package com.andy.kotlindelivery.activities.client.update

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.UsuariosProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {

    var circleImageUsuario: CircleImageView? = null
    var editTextNombre: EditText? = null
    var editTextApellido: EditText? = null
    var editTextTelefono: EditText? = null
    var buttonActualizar: Button? = null
    val TAG = "Client Update Activity"

    var sharedPref: SharedPref? = null
    var usuario: Usuario? = null

    private var imageFile: File? = null
    var userProvider : UsuariosProviders? = null

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)

        sharedPref = SharedPref(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Editar perfil"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // nos muestra la fecla de ir hacia atras

        circleImageUsuario = findViewById(R.id.circle_image_user)
        editTextNombre = findViewById(R.id.edittxt_nombre)
        editTextApellido = findViewById(R.id.edittxt_apellido)
        editTextTelefono = findViewById(R.id.edittxt_telelfono)
        buttonActualizar = findViewById(R.id.btn_update)

        getUserFromSession()

        userProvider = UsuariosProviders(usuario?.sessionToken)

        editTextNombre?.setText(usuario?.nombre)
        editTextApellido?.setText(usuario?.apellido)
        editTextTelefono?.setText(usuario?.telefono)

        if (!usuario?.image.isNullOrBlank()) {
            Glide.with(this).load(usuario?.image).into(circleImageUsuario!!)
        }

        circleImageUsuario?.setOnClickListener { selectImage() }
        buttonActualizar?.setOnClickListener { updateData() }

    }

    private fun getUserFromSession() {
        //val sharedPref = SharedPref(this,)
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
            Log.d(TAG, "Usuario: $usuario")
        }
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if(resultCode == Activity.RESULT_OK){
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                circleImageUsuario?.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR){
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "Tarea se cancelo", Toast.LENGTH_LONG).show()
            }
        }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

    private fun updateData() {

        var nombre = editTextNombre?.text.toString()
        var apellido = editTextApellido?.text.toString()
        var telefono = editTextTelefono?.text.toString()

        usuario?.nombre = nombre
        usuario?.apellido = apellido
        usuario?.telefono = telefono

        if(imageFile != null) {
            userProvider?.update(imageFile!!, usuario!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")
                    Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    if(response.body()?.isSucces == true){
                        saveUserInSession(response.body()?.data.toString())
                    }
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            userProvider?.updateWhitoutImage(usuario!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")
                    Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    if(response.body()?.isSucces == true){
                        saveUserInSession(response.body()?.data.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    private fun saveUserInSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPref?.save("user", user)
    }

}