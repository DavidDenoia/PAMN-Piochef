package com.example.trabajofinalv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.content.Intent
import androidx.navigation.fragment.findNavController


class AnadirReceta1 : Fragment() {

    private var stepCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anadir_receta1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextPageButton = view.findViewById<ImageButton>(R.id.nextPageButton)

        val stepsLayout = view.findViewById<LinearLayout>(R.id.stepsLayout)
        val addStepButton = view.findViewById<ImageButton>(R.id.addStepButton)

        addStepButton.setOnClickListener {
            stepCount++

            val newStep = EditText(context)
            newStep.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            newStep.hint = "$stepCount. "
            newStep.inputType = android.text.InputType.TYPE_CLASS_TEXT

            // Agrega el nuevo EditText al LinearLayout de los pasos
            stepsLayout.addView(newStep)
        }

        nextPageButton.setOnClickListener {
            findNavController().navigate(R.id.action_pantallaAnadirReceta_to_pantallaAnadirReceta2)
        }
    }


}