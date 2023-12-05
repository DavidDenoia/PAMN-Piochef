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
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.navigation.fragment.findNavController

class AnadirReceta1 : Fragment() {

    private var stepCount = 1

    val pickMedia = registerForActivityResult(PickMultipleVisualMedia(3)) { uris ->
        if(uris.isNotEmpty()){
            val layoutIamgenes = view?.findViewById<LinearLayout>(R.id.layoutImagenes)
            layoutIamgenes?.removeAllViews()
            btnImage.setImageURI(uris.get(0))
            for (uri in uris) {
                val newbtnImage = ImageButton(context)
                newbtnImage.layoutParams = btnImage.layoutParams
                newbtnImage.setImageURI(uri)
                layoutIamgenes?.addView(newbtnImage)
                Log.i(tag,"creado")
            }

        }else{

        }
    }
    lateinit var btnImage: ImageButton
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
        btnImage = view.findViewById(R.id.recipeImage)
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

        btnImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        nextPageButton.setOnClickListener {
            findNavController().navigate(R.id.action_pantallaAnadirReceta_to_pantallaAnadirReceta2)
        }
    }


}