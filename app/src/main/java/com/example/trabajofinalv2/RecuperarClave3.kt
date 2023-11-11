package com.example.trabajofinalv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class RecuperarClave3: Fragment() {
    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restablecer_password3, container, false)

        val btnSend = view.findViewById<Button>(R.id.btnRestablecer)

        btnSend.setOnClickListener {

            findNavController().navigate(R.id.action_pantallaClave3_to_pantallaDeInicio)
        }
        return view
    }
}