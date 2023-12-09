package com.example.trabajofinalv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class VerRecetas : Fragment() {
    private var botonPreparacion: Button? = null
    private var botonIngredientes: Button? = null
    private var contenedorPreparacion: FrameLayout? = null
    private var contenedorIngredientes: FrameLayout? = null
    private var tiempoPreparacionTextView: TextView? = null
    private var nombreUsuario: TextView? = null
    private var descripcion: TextView? = null
    private var steps: TextView? = null
    private var ingredients: TextView? = null
    private var user: String? = null
    private lateinit var deleteImageView: ImageView
    private lateinit var editImageView: ImageView
    private lateinit var imagen: ImageView
    private lateinit var fotoPerfil: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verrecetas, container, false)

        // Referencias a los elementos del layout
        contenedorPreparacion = view.findViewById(R.id.contenedorPreparacion)
        contenedorIngredientes = view.findViewById(R.id.contenedorIngredientes)
        tiempoPreparacionTextView = view.findViewById(R.id.textoSuperpuesto)
        nombreUsuario = view.findViewById(R.id.nombreUsuario)
        descripcion = view.findViewById(R.id.description)
        steps = view.findViewById(R.id.stepsList)
        ingredients = view.findViewById(R.id.ingredientsList)
        imagen = view.findViewById(R.id.imagenDecorativa)
        fotoPerfil = view.findViewById(R.id.imagenUsuario)

        // Obtener datos de la receta
        user = arguments?.getString("user")
        val recipeName = arguments?.getString("recipeName")
        val recipeId = arguments?.getString("recipeId")
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Referencia a la base de datos
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId ?: "")

        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                checkUserSession()
                val recipeImage = dataSnapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>() {})
                if (recipeImage != null && recipeImage.isNotEmpty()) {
                    Picasso.get().load(recipeImage[0]).into(imagen)
                }
                val tiempoPreparacion = dataSnapshot.child("preparationTime").getValue(String::class.java)
                val tiempoMin = tiempoPreparacion + "min."
                tiempoPreparacionTextView?.text = tiempoMin

                val imagenUser = dataSnapshot.child("userImage").getValue(String::class.java)
                if (imagenUser != null && imagenUser.isNotEmpty()) {
                    Picasso.get().load(imagenUser).into(fotoPerfil)
                }

                val nombre = dataSnapshot.child("userName").getValue(String::class.java)
                nombreUsuario?.text = nombre

                Log.d("verRecetas", "Nombre del usuario ${nombre}")

                val recipeDescription = dataSnapshot.child("recipeDescription").getValue(String::class.java)
                descripcion?.append(recipeDescription)

                val stepsList = dataSnapshot.child("steps").getValue(object : GenericTypeIndicator<List<String>>() {})
                val stringBuilder = StringBuilder()
                var count = 1
                if (stepsList != null) {
                    for (step in stepsList) {
                        stringBuilder.append("${count}- $step\n")
                        count += 1
                    }
                }
                steps?.append(stringBuilder.toString())


                val ingredientList = dataSnapshot.child("ingredients").getValue(object : GenericTypeIndicator<ArrayList<ArrayList<String>>>() {})
                val stringBuilderIngre = StringBuilder()
                if (ingredientList != null) {
                    for (step in ingredientList) {
                            stringBuilderIngre.append("- ${step[1]} ${step[2]} ${step[0]}")
                    }
                }
                ingredients?.text = stringBuilderIngre.toString()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de lectura de la base de datos si es necesario
                Log.e("VerRecetas", "Error al obtener datos de la base de datos: ${databaseError.message}")
            }
        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botonPreparacion = view?.findViewById<Button>(R.id.botonPreparacion)
        botonIngredientes = view?.findViewById<Button>(R.id.botonIngredientes)
        contenedorPreparacion = view?.findViewById<FrameLayout>(R.id.contenedorPreparacion)
        contenedorIngredientes = view?.findViewById<FrameLayout>(R.id.contenedorIngredientes)

        botonPreparacion?.setOnClickListener {
            mostrarPreparacion()
            botonPreparacion?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.custom_color_primary))
            botonPreparacion?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            botonIngredientes?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            botonIngredientes?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        botonIngredientes?.setOnClickListener {
            mostrarIngredientes()
            botonIngredientes?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.custom_color_primary))
            botonIngredientes?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            botonPreparacion?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            botonPreparacion?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        mostrarPreparacion()

        deleteImageView = view.findViewById(R.id.deleteImageView)
        editImageView = view.findViewById(R.id.editImageView)
        checkUserSession()
    }

    private fun mostrarPreparacion(){
        contenedorPreparacion?.visibility = View.VISIBLE
        contenedorIngredientes?.visibility = View.GONE
        Log.d("MostrarPreparacion", "Mostrando la preparación de la receta")
    }



    private fun mostrarIngredientes() {

        contenedorPreparacion?.visibility = View.GONE
        contenedorIngredientes?.visibility = View.VISIBLE
        Log.d("Mostrar ingredientes", "Mostrando los ingredientes de la receta")
    }

    private fun checkUserSession(){
        val currentUser = FirebaseAuth.getInstance().currentUser?.email
        if(currentUser != null && user==currentUser){
            deleteImageView.visibility = View.VISIBLE
            editImageView.visibility = View.VISIBLE

            deleteImageView.setOnClickListener{
                Log.d("VerRecetas", "Se hizo clic en DeleteImageView")
            }

            editImageView.setOnClickListener{
                Log.d("VerRecetas", "Se hizo clic en EditImageView")
            }
        }else{
            deleteImageView.visibility = View.GONE
            editImageView.visibility = View.GONE
        }
    }

}