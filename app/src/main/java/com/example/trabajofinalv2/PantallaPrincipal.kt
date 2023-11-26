package com.example.trabajofinalv2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController


class PantallaPrincipal : Fragment(R.layout.fragment_pantalla_principal) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pantalla_principal, container, false)
        //Saltar a la siguiente pagina de introduccion
        val btnInicial = view.findViewById<Button>(R.id.foto)
        btnInicial.setOnClickListener {
            findNavController().navigate(R.id.action_pantallaprincipal_to_pantallaperfil)
        }

        return view
    }
}