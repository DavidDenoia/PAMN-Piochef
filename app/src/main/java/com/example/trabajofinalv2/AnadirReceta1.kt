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
import android.window.BackEvent
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider

class AnadirReceta1 : Fragment() {




    private var stepCount = 1
    private var imageUris = mutableListOf<String>()

    val pickMedia = registerForActivityResult(PickMultipleVisualMedia(3)) { uris ->
        if(uris.isNotEmpty()){
            imageUris.clear()

            uris.forEach{ uri ->
                imageUris.add(uri.toString())
            }
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
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            if (!findNavController().navigateUp()){
                if(isEnabled){
                    isEnabled = false
                    findNavController().navigate(R.id.action_pantallaAnadirReceta_to_pantallaMenuInferior)
                }
            }
        }
        return inflater.inflate(R.layout.fragment_anadir_receta1, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

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




            val recipeName = view.findViewById<EditText>(R.id.recipeNameInput).text.toString()


            val imageUriStrings = imageUris.map { it.toString() }

            val recipeDescription = view.findViewById<EditText>(R.id.recipeDescription).text.toString()


            val stepsList = ArrayList<String>()
            for(i in 0 until stepsLayout.childCount){
                val step = stepsLayout.getChildAt(i) as EditText
                stepsList.add(step.text.toString())
            }

            // Guardar los datos en el ViewModel
            sharedViewModel.recipeName.value = recipeName
            sharedViewModel.imageUris.value = imageUris
            sharedViewModel.recipeDescription.value = recipeDescription
            sharedViewModel.steps.value = stepsList



            findNavController().navigate(R.id.action_pantallaAnadirReceta_to_pantallaAnadirReceta2)
        }
    }


}