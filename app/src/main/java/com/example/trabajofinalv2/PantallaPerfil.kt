package com.example.trabajofinalv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.view.Gravity
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class PantallaPerfil : Fragment() {

    lateinit var fotoPerfil: ImageView
    lateinit var usernameText: TextView
    lateinit var userDescription: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pantalla_perfil, container, false)

        val menuButton = view.findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener { v ->
            showPopupMenu(v)
        }

        //Establecer nombre de usuario
        usernameText = view.findViewById(R.id.username)
        obtenerUsername(usernameText)

        //Establecer descripcion
        userDescription = view.findViewById(R.id.descp)
        obtenerDescription(userDescription)

        //Establecer foto de perfil
        fotoPerfil = view.findViewById(R.id.image)
        val imageUrl: String? = obtenerUrlDeLaImagenDelUsuario(fotoPerfil)
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(fotoPerfil)
        } else {
            fotoPerfil.setImageResource(R.drawable.placeholder_image)
        }

        return view
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_desplegable, popup.menu)
        popup.setOnMenuItemClickListener{ item: MenuItem? ->

            when (item!!.itemId) {
                R.id.añadirreceta -> {
                    //perfil a añadir receta
                    findNavController().navigate(R.id.action_pantallaMenuInferior_to_pantallaAnadirReceta)
                }
                R.id.recetaguradada -> {
                    //perfil a receta guardada
                }
                R.id.editarperfil -> {
                    findNavController().navigate(R.id.action_pantallaMenuInferior_to_pantallaEditarPefil)
                }
            }
            true
        }
        popup.show()
    }
}

private fun obtenerUrlDeLaImagenDelUsuario(imageView: ImageView): String? {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Comprueba si el usuario tiene una URL de imagen en la base de datos
                if (snapshot.hasChild("profileImageUrl")) {
                    val imageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
                    if (imageUrl != null) {
                        Picasso.get().load(imageUrl).into(imageView)
                    } else {
                        imageView.setImageResource(R.drawable.placeholder_image)
                    }
                } else {
                    imageView.setImageResource(R.drawable.placeholder_image)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    return null
}

private fun obtenerUsername(usernameText: TextView){
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.hasChild("username")) {
                    val username = snapshot.child("username").value.toString()
                    usernameText.text = "$username"
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }}

private fun obtenerDescription(descriptionText: TextView){
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.hasChild("username")) {
                    val descripcion = snapshot.child("description").value.toString()
                    descriptionText.text = "$descripcion"
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }}