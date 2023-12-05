package com.example.trabajofinalv2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AnadirReceta3 : Fragment() {
    private var stepCount = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anadir_receta_3, container, false)
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nextPageButton = view.findViewById<ImageButton>(R.id.nextPageButton)
        nextPageButton.setOnClickListener {
            //pag3
            findNavController().navigate(R.id.action_pantallaAnadirReceta3_to_pantallaMenuInferior)
        }
    }



}