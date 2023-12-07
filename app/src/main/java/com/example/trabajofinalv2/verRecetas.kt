package com.example.trabajofinalv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.checkerframework.common.subtyping.qual.Bottom

class verRecetas : Fragment() {
    private var botonPreparacion: Button? = null
    private var botonIngredientes: Button? = null
    private var contenedorPreparacion: FrameLayout? = null
    private var contenedorIngredientes: FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tv = view?.findViewById<TextView>(R.id.nombre)
        tv?.text = arguments?.getString("user")
        val user = arguments?.getString("user")
        val recipeName = arguments?.getString("recipeName")
        Log.e("RecipeClick", "Pepito: $recipeName, Usuario: $user")
        // Inflate the layout for this fragment




        /*botonPreparacion = view?.findViewById<Button>(R.id.botonPreparacion)
        botonIngredientes = view?.findViewById<Button>(R.id.botonIngredientes)
        contenedorPreparacion = view?.findViewById<FrameLayout>(R.id.contenedorPreparacion)
        contenedorIngredientes = view?.findViewById<FrameLayout>(R.id.contenedorIngredientes)*/

        /*mostrarPreparacion()
        botonPreparacion?.setOnClickListener {
            mostrarPreparacion()
            botonPreparacion?.apply {
                setBackgroundColor(resources.getColor(R.color.custom_color_primary))
                setTextColor(resources.getColor(R.color.white))
            }
            botonIngredientes?.apply {
                setBackgroundColor(resources.getColor(R.color.white))
                setTextColor(resources.getColor(R.color.black))
            }
        }
        botonIngredientes?.setOnClickListener {
            mostrarIngredientes()
            botonIngredientes?.apply {
                setBackgroundColor(resources.getColor(R.color.custom_color_primary))
                setTextColor(resources.getColor(R.color.white))
            }
            botonPreparacion?.apply {
                setBackgroundColor(resources.getColor(R.color.white))
                setTextColor(resources.getColor(R.color.black))
            }
        }*/

        /*botonPreparacion?.setOnClickListener {
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

        // Muestra la preparación por defecto
        mostrarPreparacion()*/



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

}