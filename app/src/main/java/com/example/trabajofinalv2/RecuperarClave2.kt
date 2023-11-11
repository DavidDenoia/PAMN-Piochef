package com.example.trabajofinalv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class RecuperarClave2 : Fragment() {

    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restablecer_password2, container, false)

        val btnSend = view.findViewById<Button>(R.id.btnRestContra)

        btnSend.setOnClickListener {

            findNavController().navigate(R.id.action_pantallaClave2_to_pantallaClave3)
        }
        return view
    }
}