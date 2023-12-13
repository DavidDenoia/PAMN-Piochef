package com.example.trabajofinalv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class PantallaPerfil : Fragment(R.layout.fragment_pantalla_perfil) {

    lateinit var fotoPerfil: ImageView
    lateinit var usernameText: TextView
    lateinit var userDescription: TextView
    private var userEmail: String = obtenerUserEmail()
    
    private lateinit var adapter:UserRecipeAdapter
    private val viewModel by lazy{
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
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

        fun obtenerUserEmail(){
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists() && snapshot.hasChild("email")) {
                            userEmail = snapshot.child("email").value.toString()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("a",usernameText.text.toString())
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("username") && snapshot.hasChild("email")) {
                        val username = snapshot.child("username").value.toString()
                        val emailnow = snapshot.child("email").value.toString()
                        Log.d("a",username)
                        adapter = UserRecipeAdapter(requireContext(), emailnow, object : UserRecipeAdapter.OnRecipeClickListener{
                            override fun onRecipeClick(recipeName: String, user: String,email:String) {
                                obtainRecipeId(recipeName) { recipeId ->
                                    if (recipeId != null) {
                                        val bundle = bundleOf(
                                            "recipeName" to recipeName,
                                            "recipeId" to recipeId,
                                            "email" to email,
                                            "user" to user
                                        )
                                        findNavController().navigate(R.id.action_pantallaMenuInferior_to_verRecetas, bundle)
                                    } else {
                                    }
                                }
                            }
                        })
                        val recyclerView = view?.findViewById<RecyclerView>(R.id.recetasRecyclerView)
                        recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
                        recyclerView?.addItemDecoration(
                            DividerItemDecoration(requireContext(),
                                DividerItemDecoration.VERTICAL)
                        )
                        recyclerView?.adapter = adapter

                        observeData()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }



    }
    fun observeData(){
        viewModel.fetchRecipeData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
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
                    findNavController().navigate(R.id.action_pantallaMenuInferior_to_guardarrecetas)
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
                if (snapshot.exists() && snapshot.hasChild("description")) {
                    val descripcion = snapshot.child("description").value.toString()
                    descriptionText.text = "$descripcion"
                } else {
                    descriptionText.text = ""
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }}

private fun obtainRecipeId(recipeName: String, callback: (String?) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("recipes")

    databaseReference.orderByChild("recipeName").equalTo(recipeName)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (recipeSnapshot in dataSnapshot.children) {
                        val recipeId = recipeSnapshot.key
                        callback(recipeId)
                        return
                    }
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
}

private fun obtenerUserEmail(): String {
    var userEmail: String = ""
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
        userEmail = user.email ?: ""
    }
    return userEmail
}