package com.example.trabajofinalv2

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        val inputCodigo = view.findViewById<EditText>(R.id.TextCodigo)

        inputCodigo.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                btnSend.isEnabled = !s.isNullOrBlank()

                if (btnSend.isEnabled) {
                    btnSend.setBackgroundColor(Color.parseColor("#114B8D"))
                } else {
                    btnSend.setBackgroundColor(Color.parseColor("#416FA4"))
                }
            }
        })
        btnSend.setOnClickListener {

            findNavController().navigate(R.id.action_pantallaClave2_to_pantallaClave3)
        }
        return view
    }
}