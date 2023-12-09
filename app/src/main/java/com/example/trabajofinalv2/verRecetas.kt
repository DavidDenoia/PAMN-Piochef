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
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.checkerframework.common.subtyping.qual.Bottom

class verRecetas : Fragment(R.layout.fragment_verrecetas) {
    private var botonPreparacion: Button? = null
    private var botonIngredientes: Button? = null
    private var contenedorPreparacion: FrameLayout? = null
    private var contenedorIngredientes: FrameLayout? = null
    private var user: String? = null
    private lateinit var deleteImageView: ImageView
    private lateinit var editImageView: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val tv = view?.findViewById<TextView>(R.id.nombre)
        tv?.text = arguments?.getString("user")
        user = arguments?.getString("user")
        val recipeName = arguments?.getString("recipeName")
        Log.e("RecipeClick", "Pepito: $recipeName, Usuario: $user")
        // Inflate the layout for this fragment
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            if (!findNavController().navigateUp()){
                if(isEnabled){
                    isEnabled = false
                    findNavController().navigate(R.id.action_verRecetas_to_pantallaMenuInferior)
                }
            }
        }
        return inflater.inflate(R.layout.fragment_verrecetas, container, false)
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
    private fun setCurrentFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.containerView, fragment)
        transaction?.commit()
    }
}