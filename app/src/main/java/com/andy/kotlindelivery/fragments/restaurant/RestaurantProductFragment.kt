package com.andy.kotlindelivery.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.adapters.CategoriasAdapters
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.Producto
import com.andy.kotlindelivery.models.ResponseHttp
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.CategoriasProviders
import com.andy.kotlindelivery.providers.ProductosProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantProductFragment : Fragment() {
    var TAG = "RestaurantProductFragment"
    var gson = Gson()

    var myView: View? = null
    var editTextNombre: EditText? = null
    var editTextDescription: EditText? = null
    var editTextPreeio: EditText? = null
    var imageViewProduct1: ImageView? = null
    var imageViewProduct2: ImageView? = null
    var imageViewProduct3: ImageView? = null
    var spinnerCategoria: Spinner? = null
    var btnCreateProduct : Button? = null

    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null

    var categoriasProvider: CategoriasProviders? = null
    var productosProviders: ProductosProviders? = null
    var usuario: Usuario? = null
    var sharedPref: SharedPref? = null
    var categorias = ArrayList<Categoria>()
    var idCategoria = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_product, container, false)
        editTextNombre= myView?.findViewById(R.id.edittxt_nombre)
        editTextDescription= myView?.findViewById(R.id.edittxt_description)
        editTextPreeio = myView?.findViewById(R.id.edittxt_precio)
        imageViewProduct1= myView?.findViewById(R.id.imageview_prodcut1)
        imageViewProduct2 = myView?.findViewById(R.id.imageview_prodcut2)
        imageViewProduct3 =myView?.findViewById(R.id.imageview_prodcut3)
        spinnerCategoria = myView?.findViewById(R.id.spinner_categoria)
        btnCreateProduct = myView?.findViewById(R.id.btn_create_product)

        btnCreateProduct?.setOnClickListener { createProduct()  }
        imageViewProduct1?.setOnClickListener { selectImage(101) }
        imageViewProduct2?.setOnClickListener { selectImage(102) }
        imageViewProduct3?.setOnClickListener { selectImage(103) }

        sharedPref = SharedPref(requireActivity())
        getUserFromSession()
        categoriasProvider = CategoriasProviders(usuario?.sessionToken!!)
        productosProviders = ProductosProviders(usuario?.sessionToken!!)
        getCategorias()

        return myView
    }
/////////////////////////////////////////////////////////////////////////
    private fun createProduct() {
        val nombreProducto = editTextNombre?.text.toString()
        val descriptionProducto = editTextDescription?.text.toString()
        val precioText = editTextPreeio?.text.toString()
        val files = ArrayList<File>()

        if ( isValidForm(nombreProducto, descriptionProducto,precioText) ){
            val producto = Producto(
                nombre = nombreProducto,
                descripcion = descriptionProducto,
                precio = precioText.toDouble(),
                idCategoria = idCategoria
            )
            Log.d(TAG, "Producto $producto")
            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)
            Log.d(TAG, "Imagenes $files")
            ProgressDialogFragment.showProgressBar(requireActivity())
            productosProviders?.create(files, producto)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG, "Response: ${response}")
                    Log.d(TAG, "Body: ${response.body()}")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                    if(response.body()?.isSucces == true){
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                   ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }
/////////////////////////////////////////////////////////////////////////////

 //////////////////////////////////////
    private fun getCategorias(){
        Log.d(TAG,  "getCategorias() : ${categoriasProvider}")
        categoriasProvider?.getAll()?.enqueue(object: Callback<ArrayList<Categoria>> {
            override fun onResponse(
                call: Call<ArrayList<Categoria>>,
                response: Response<ArrayList<Categoria>>
            ) {
                if(response.body() != null){
                    categorias = response.body()!!
                    val arrayAdapter = ArrayAdapter<Categoria>(requireActivity(), android.R.layout.simple_dropdown_item_1line, categorias)
                    spinnerCategoria?.adapter = arrayAdapter
                    spinnerCategoria?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, l: Long ) {
                            idCategoria = categorias[position].id!!
                            Log.d(TAG,  "Id categoria : $idCategoria")
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Categoria>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
 ///////////////////////////////////////////////////////////////////////////////

////FUNCIONES PARA SELECCIONAR LA IMAGEN DESDE GALERIA
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            if(requestCode == 101){
                imageFile1 = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                imageViewProduct1?.setImageURI(fileUri)
            } else if(requestCode == 102){
                imageFile2 = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                imageViewProduct2?.setImageURI(fileUri)
            } else if(requestCode == 103){
                imageFile3 = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                imageViewProduct3?.setImageURI(fileUri) }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
}

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .start(requestCode)
    }
/////////////////////////////////////////////////////////////////

//////////////funcion de validacion
    private fun isValidForm(nombre: String, descripcion: String, precio: String): Boolean{
        if(nombre.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa el nombre del producto", Toast.LENGTH_LONG).show()
            return false
        }
        if(descripcion.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa la descripcion del producto", Toast.LENGTH_LONG).show()
            return false
        }
        if(precio.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa el precio del producto", Toast.LENGTH_LONG).show()
            return false
        }
        if(imageFile1 == null){
            Toast.makeText(requireContext(), "Ingresa al menos la primer imagen", Toast.LENGTH_LONG).show()
            return false
        }
        if(idCategoria.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa la categoria del producto", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
//////////////////////////////////

////Funcion para limpiar formulario
    private fun resetForm() {
        editTextNombre?.setText("")
        editTextDescription?.setText("")
        editTextPreeio?.setText("")
        imageFile1 = null
        imageFile2 = null
        imageFile3 = null
        imageViewProduct1?.setImageResource(R.drawable.ic_image)
        imageViewProduct2?.setImageResource(R.drawable.ic_image)
        imageViewProduct3?.setImageResource(R.drawable.ic_image)
    }
 //////////////////

/////funciones de sesion
    private fun getUserFromSession() {
  //val sharedPref = SharedPref(this,)

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
            Log.d(TAG, "Usuario: $usuario")
        }
    }
///////////////////////////////


}