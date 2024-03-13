package com.andy.kotlindelivery.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.andy.kotlindelivery.R
import com.andy.kotlindelivery.activities.MainActivity
import com.andy.kotlindelivery.activities.SelectRolesActivity
import com.andy.kotlindelivery.activities.client.update.ClientUpdateActivity
import com.andy.kotlindelivery.models.Usuario
import com.andy.kotlindelivery.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView

class ClientProfileFragment : Fragment() {

    var myView: View? = null
    var buttonSeleccinarRol : Button? = null
    var buttonUpdateProfile : Button? = null
    var circleImageUser:CircleImageView? = null
    var textViewNombre: TextView? = null
    var textViewCorreo: TextView? = null
    var textViewTelefono: TextView? = null
    var imageViewLogout: ImageView?= null

    var sharedPref: SharedPref? =null
    var usuario: Usuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView =  inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        buttonSeleccinarRol = myView?.findViewById(R.id.btn_select_rol)
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        textViewNombre = myView?.findViewById(R.id.textview_name)
        textViewCorreo = myView?.findViewById(R.id.textview_email)
        textViewTelefono= myView?.findViewById(R.id.textview_phone)
        circleImageUser = myView?.findViewById(R.id.circle_image_user)
        imageViewLogout = myView?.findViewById(R.id.imageview_logout)

        buttonSeleccinarRol?.setOnClickListener { goToRolSelect() }
        buttonUpdateProfile?.setOnClickListener { goToUpdate() }
        imageViewLogout?.setOnClickListener { logout() }

        getUserFormSession()

        textViewNombre?.text = "${usuario?.nombre} ${usuario?.apellido}"
        textViewCorreo?.text = usuario?.email
        textViewTelefono?.text = usuario?.telefono


        if(!usuario?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(usuario?.image).into(circleImageUser!!)
        }
        return myView
    }


    private fun getUserFormSession() {
        //val sharedPref = SharedPref(this,)
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("user"), Usuario::class.java)
            Log.d("Fragment Client Profile", "Usuario: $usuario")
        }
    }

    private fun goToUpdate(){
        val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)
    }

    private  fun logout(){
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

    private fun goToRolSelect() {
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}