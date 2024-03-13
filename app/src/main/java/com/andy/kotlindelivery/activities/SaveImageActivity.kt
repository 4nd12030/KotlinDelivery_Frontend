package com.andy.kotlindelivery.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.andy.kotlindelivery.R
import com.github.dhaval2404.imagepicker.ImagePicker
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import com.andy.kotlindelivery.activities.client.home.ClientHomeActivity
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.UsuariosProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SaveImageActivity : AppCompatActivity() {

    var TAG = "SaveImageActivity"
    var circleImageUser: CircleImageView? = null
    var buttonNext: Button? = null
    var buttonConfirm: Button? = null

    private var imageFile: File? = null
    var userProvider: UsuariosProviders? =null
    var user: Usuario? = null
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_imagectivity)

        sharedPref = SharedPref(this)
        getUserFormSession()
        userProvider = UsuariosProviders(user?.sessionToken)

        circleImageUser = findViewById(R.id.cirlce_image)
        buttonConfirm = findViewById(R.id.btn_confirm)
        buttonNext = findViewById(R.id.btn_next)

        circleImageUser?.setOnClickListener{ selectImage() }
        buttonNext?.setOnClickListener{ goToClientHome() }
        buttonConfirm?.setOnClickListener{ saveImage() }
}

    private fun saveImage() {
        if(imageFile != null && user != null ) {
            userProvider?.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")
                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
        } else {
            Toast.makeText(this@SaveImageActivity, "Los datos del usuario no pueden ser nulas", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserInSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, Usuario::class.java)
        sharedPref?.save("user", user)
        goToClientHome()
    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //ELIMINA EL HISTORIAL DE PANTALLA
        startActivity(i)
    }

    private fun getUserFormSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            user = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
        }
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if(resultCode == Activity.RESULT_OK){
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                circleImageUser?.setImageURI(fileUri)
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
}