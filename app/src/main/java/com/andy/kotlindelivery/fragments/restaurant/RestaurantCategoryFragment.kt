package com.andy.kotlindelivery.fragments.restaurant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.CategoriasProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantCategoryFragment : Fragment() {

    var TAG = "RestaurantCategoryFragment"
    var categoriasProvider: CategoriasProviders? = null
    var sharedPref: SharedPref? = null
    var usuario: Usuario? = null

    var myView: View? = null
    var imageViewCategory: ImageView? =null
    var editTextCategory: EditText? = null
    var buttonCrearCategory: Button? = null

    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_category, container, false)

        sharedPref = SharedPref(requireActivity())

        imageViewCategory = myView?.findViewById(R.id.image_view_category)
        editTextCategory = myView?.findViewById(R.id.edittxt_category)
        buttonCrearCategory = myView?.findViewById(R.id.btn_create_category)

        getUserFromSession()
        categoriasProvider = CategoriasProviders(usuario?.sessionToken!!)
        Log.d(TAG, "Usuario con Token: ${usuario?.sessionToken!!}")

        imageViewCategory?.setOnClickListener{ selectImage()  }
        buttonCrearCategory?.setOnClickListener { createCategory() }

        return myView
    }

    /////FUNCION  QUE INICIA LA CREACION DE LA CATEGORIA
    private fun createCategory() {
        val categoria = editTextCategory?.text.toString()

        if(imageFile != null){
            val category = Categoria(nombre = categoria)
            Log.d(TAG, "Categoria: $category")
            categoriasProvider?.create(imageFile!!, category)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    if(response.body()?.isSucces == true){
                        clearForm()
                    }

                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })

        } else {
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_LONG).show()
        }
    }
    ///////////////////////////////////////

    private fun clearForm(){
        editTextCategory?.setText("")
        imageFile = null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }

    //////////FUNCIONES DE SESION
    private fun getUserFromSession() {
        //val sharedPref = SharedPref(this,)
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
            Log.d(TAG, "Usuario: $usuario")
        }
    }

    //////////////////////////////////////

    ///////////SELECCIONAR IMAGEN
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if(resultCode == Activity.RESULT_OK){
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                imageViewCategory?.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR){
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(requireContext(), "Tarea se cancelo", Toast.LENGTH_LONG).show()
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

    //////////////////////////////////

}