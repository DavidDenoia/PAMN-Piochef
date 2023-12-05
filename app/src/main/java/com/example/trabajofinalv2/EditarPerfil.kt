package com.example.trabajofinalv2

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class EditarPerfil : Fragment(R.layout.fragment_editar_perfil) {
    lateinit var publicButton: Button
    lateinit var privateButton: Button
    lateinit var backButton: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        publicButton = view.findViewById(R.id.publicVisibility)
        privateButton = view.findViewById(R.id.privateVisibility)

        backButton = view.findViewById(R.id.flecha_atras)

        backButton.setOnClickListener{
            findNavController().navigate(R.id.action_pantallaEditarPefil_to_pantallaMenuInferior)
        }

        // Cuando el botón público se selecciona
        publicButton.setOnClickListener {
            publicButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorSelected))
            privateButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorUnselected))
        }

        // Cuando el botón privado se selecciona
        privateButton.setOnClickListener {
            publicButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorUnselected))
            privateButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorSelected))
        }
    }
}