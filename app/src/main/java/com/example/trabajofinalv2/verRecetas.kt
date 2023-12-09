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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

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

        // Obtener datos de la receta
        user = arguments?.getString("user")
        val recipeName = arguments?.getString("recipeName")
        val recipeId = arguments?.getString("recipeId")

        // Referencia a la base de datos
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId ?: "")

        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Obtener la información del tiempo de preparación desde la base de datos
                val tiempoPreparacion = dataSnapshot.child("preparationTime").getValue(String::class.java)
                val tiempoMin = tiempoPreparacion + "min."
                tiempoPreparacionTextView?.text = tiempoMin
                nombreUsuario?.text = user

                val recipeDescription = dataSnapshot.child("recipeDescription").getValue(String::class.java)
                descripcion?.append(recipeDescription)

                val stepsList = dataSnapshot.child("steps").getValue(object : GenericTypeIndicator<List<String>>() {})
                val stringBuilder = StringBuilder()
                if (stepsList != null) {
                    for (step in stepsList) {
                        stringBuilder.append("- $step\n")
                    }
                }
                steps?.append(stringBuilder.toString())

                val ingredientList = dataSnapshot.child("ingredients").getValue(object : GenericTypeIndicator<ArrayList<ArrayList<String>>>() {})
                val stringBuilderIngre = StringBuilder()
                if (ingredientList != null) {
                    for (step in ingredientList) {
                        for (ingredient in step){
                            stringBuilderIngre.append("- $ingredient\n")
                        }
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