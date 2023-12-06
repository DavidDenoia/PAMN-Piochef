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
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.awaitAll
class AnadirReceta2 : Fragment() {


    private var stepCount = 1
    //Crea un firebaseAuth object

    private val editTextsList = mutableListOf<EditText>()

    private lateinit var auth: FirebaseAuth
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




        // Inicializar el ViewModel
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        var observedRecipeName = ""
        var observedImageUris = listOf<String>()
        var observedRecipeDescription = ""
        var observedSteps = listOf<String>()

        // Observar los datos y actuar en consecuencia
        sharedViewModel.recipeName.observe(viewLifecycleOwner, Observer { recipeName ->
            // Haz algo con recipeName, por ejemplo, mostrarlo en un TextView
            observedRecipeName = recipeName
            Log.d("SharedViewModel", "Recipe Name: $observedRecipeName")
        })

        sharedViewModel.imageUris.observe(viewLifecycleOwner, Observer { uris ->
            // Manejar la lista de URIs
            observedImageUris = uris
            Log.d("SharedViewModel", "Image URIs: $observedImageUris")
        })

        sharedViewModel.recipeDescription.observe(viewLifecycleOwner, Observer { description ->
            // Manejar la descripción
            observedRecipeDescription = description
            Log.d("SharedViewModel", "Recipe Description: $observedRecipeDescription")
        })

        sharedViewModel.steps.observe(viewLifecycleOwner, Observer { steps ->
            // Manejar los pasos
            observedSteps = steps
            Log.d("SharedViewModel", "Steps: $observedSteps")
        })









        auth = Firebase.auth
        nextPageButton.setOnClickListener {
            //pag3
            registraValores(observedRecipeName,observedImageUris,observedRecipeDescription,observedSteps)

        }



        //Aqui recogemos los datos que vienen de añadirReceta1



    }
    private fun registraValores(recipeName: String, imageUris: List<String>, recipeDescription: String, steps: List<String>){
        // Obtener la instancia de FirebaseAuth
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if(user != null){
            val uid = user.uid
            Log.d("Firebase", "Usuario autenticado con UID: ${user.uid}")
            val recipeData = hashMapOf(
                "recipeName" to recipeName,
                "imageUris" to imageUris,
                "recipeDescription" to recipeDescription,
                "steps" to steps
            )

            val databaseRef = Firebase.database("https://piochef-effb5-default-rtdb.europe-west1.firebasedatabase.app").getReference("recipes")
            //val databaseRef = FirebaseDatabase.getInstance().getReference("users/$uid/recipes")

            val recipeId = databaseRef.push().key

            recipeId?.let {
                // Guardar los datos en la base de datos
                databaseRef.child(it).setValue(recipeData)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Datos guardados correctamente")
                        // Manejar éxito, por ejemplo, navegando a otro fragmento
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error al guardar datos", e)
                        // Manejar fallo
                    }
            }
            findNavController().navigate(R.id.action_pantallaAnadirReceta2_to_pantallaAnadirReceta3)
        } else {
            Log.e("Firebase", "Usuario no autenticado")
            // Manejar caso de usuario no autenticado
        }
    }


    suspend fun uploadImagesAndGetURLs(imagePaths: ArrayList<String>): List<String> = coroutineScope {
        val storage = FirebaseStorage.getInstance()
        val urls = imagePaths.map { imagePath ->
            async {
                val storageRef = storage.reference.child("imagenesRecetas/${imagePath.substringAfterLast("/")}")
                val uploadTask = storageRef.putFile(Uri.parse(imagePath))
                val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()
                downloadUrl
            }
        }.awaitAll()

        urls
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