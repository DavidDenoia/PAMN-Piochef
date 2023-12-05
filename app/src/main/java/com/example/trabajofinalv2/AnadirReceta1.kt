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
            // Crear una instancia del nuevo fragmento
            val newFragment = AnadirReceta2()

            // Obtener el FragmentManager y empezar una transacción
            val transaction = fragmentManager?.beginTransaction()

            // Reemplazar el fragmento actual por el nuevo
            transaction?.replace(R.id.fragmentContainer, newFragment) // Reemplaza 'R.id.container' con el ID del contenedor de tu fragmento en el layout
            transaction?.addToBackStack(null) // Opcional, si deseas agregarlo al back stack

            // Completar la transacción
            transaction?.commit()
        }
    }


}