package com.example.trabajofinalv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class verRecetas : Fragment() {

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
        return inflater.inflate(R.layout.fragment_verrecetas, container, false)
    }

}