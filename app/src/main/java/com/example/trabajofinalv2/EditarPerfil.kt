package com.example.trabajofinalv2

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class EditarPerfil : Fragment(R.layout.fragment_editar_perfil) {
    lateinit var publicButton: Button
    lateinit var privateButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        publicButton = view.findViewById(R.id.publicVisibility)
        privateButton = view.findViewById(R.id.privateVisibility)

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