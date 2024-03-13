package com.andy.kotlindelivery.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.activities.client.shopping_bag.ClientShoppingBagActivity
import com.andy.kotlindelivery.adapters.CategoriasAdapters
import com.andy.kotlindelivery.models.Categoria
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.providers.CategoriasProviders
import com.andy.kotlindelivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientCategoriesFragment : Fragment() {

    val gson = Gson()
    var TAG = "ClientCategoriesFragment"

    var myView: View? = null
    var recyclerViewCategorias: RecyclerView? = null
    var categoriasProvider: CategoriasProviders? = null
    var adapter: CategoriasAdapters? = null
    var usuario: Usuario? = null
    var sharedPref: SharedPref? = null
    var categorias = ArrayList<Categoria>()
    var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_categories, container, false)

        setHasOptionsMenu(true)

        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        toolbar?.title = "Categorias"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        recyclerViewCategorias = myView?.findViewById(R.id.recyclerview_categorias)
        recyclerViewCategorias?.layoutManager = LinearLayoutManager(requireContext())
        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriasProvider = CategoriasProviders(usuario?.sessionToken!!)
        Log.d(TAG,  "categoriasProvider : ${usuario?.sessionToken!!}")

        getCategorias()
        return myView
    }

    ////////
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_bag, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_shopping_bag){
            goToShoppingBag()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToShoppingBag(){
        val i = Intent(requireContext(), ClientShoppingBagActivity::class.java)
        startActivity(i)
    }
    ////////////////////////////////////////////////////


    private fun getCategorias(){
        Log.d(TAG,  "getCategorias() : ${categoriasProvider}")
        categoriasProvider?.getAll()?.enqueue(object: Callback<ArrayList<Categoria>> {
            override fun onResponse(call: Call<ArrayList<Categoria>>, response: Response<ArrayList<Categoria>>
            ) {
                if(response.body() != null){
                    categorias = response.body()!!
                    adapter = CategoriasAdapters(requireActivity(), categorias)
                    recyclerViewCategorias?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Categoria>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getUserFromSession() {
        //val sharedPref = SharedPref(this,)

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
            Log.d(TAG, "Usuario: $usuario")
        }
    }

}