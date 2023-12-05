package com.example.trabajofinalv2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.allViews
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import java.util.Vector

class AnadirReceta2 : Fragment() {
    private var stepCount = 1
    //Crea un firebaseAuth object
    lateinit var auth: FirebaseAuth
    private val editTextsList = mutableListOf<EditText>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anadir_receta_2, container, false)
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextPageButton = view.findViewById<ImageButton>(R.id.nextPageButton)

        val stepsLayout = view.findViewById<LinearLayout>(R.id.colstepsLayout)
        val addStepButton = view.findViewById<ImageButton>(R.id.addStepButton)

        addStepButton.setOnClickListener {
            stepCount++

            val layout = LinearLayout(context)
            val nom = EditText(context)
            val can = EditText(context)
            val uni = EditText(context)
            layout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                nom.id = View.generateViewId()
                can.id = View.generateViewId()
                uni.id = View.generateViewId()
            }

            nom.layoutParams = LinearLayout.LayoutParams(460, LinearLayout.LayoutParams.WRAP_CONTENT)

            val param = nom.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(50,0,0,0);
            nom.layoutParams = param // Tested!! - You need this line for the params to be applied.

            nom.hint = "Nombre"
            nom.inputType = android.text.InputType.TYPE_CLASS_TEXT


            can.layoutParams = LinearLayout.LayoutParams(250, LinearLayout.LayoutParams.WRAP_CONTENT)

            val param2 = can.layoutParams as ViewGroup.MarginLayoutParams
            param2.setMargins(50,0,0,0);
            can.layoutParams = param2

            can.hint = "Cantidad"
            can.inputType = android.text.InputType.TYPE_CLASS_TEXT

            uni.layoutParams = LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT)

            val param3 = uni.layoutParams as ViewGroup.MarginLayoutParams
            param3.setMargins(50,0,0,0);
            uni.layoutParams = param3


            uni.hint = "ml"
            uni.inputType = android.text.InputType.TYPE_CLASS_TEXT

            editTextsList.add(nom)
            editTextsList.add(can)
            editTextsList.add(uni)
            // Agrega el nuevo EditText al LinearLayout de los pasos
            layout.addView(nom)
            layout.addView(can)
            layout.addView(uni)
            stepsLayout.addView(layout)
        }

        nextPageButton.setOnClickListener {
            //pag3
            registraValores()
        }
    }
    fun registraValores(){
        //se autentifica
        auth = FirebaseAuth.getInstance()
        //funcion pilla todos los datos
        val datosinicial = recogeDatos()
        val ingredientes = obtenerTextosDePasos()
        //los mete en database

        //una vez metidos navega al siguiente
        findNavController().navigate(R.id.action_pantallaAnadirReceta2_to_pantallaAnadirReceta3)
    }

    fun recogeDatos(): List<String>{
        var datos = Vector<String>();
        //pilla todos los datos de la pantalla
        val prepT = view?.findViewById<EditText>(R.id.tiempoInput)
        val raciones = view?.findViewById<EditText>(R.id.racionesInput)

        datos.add(prepT?.text.toString())
        datos.add(raciones?.text.toString())
        return datos
    }
    fun obtenerTextosDePasos(): List<String> {
        val textosDePasos = mutableListOf<String>()

        for (editText in editTextsList) {
            val texto = editText.text.toString()
            textosDePasos.add(texto)
        }

        return textosDePasos
    }
}