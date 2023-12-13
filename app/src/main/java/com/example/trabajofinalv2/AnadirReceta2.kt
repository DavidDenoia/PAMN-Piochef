package com.example.trabajofinalv2

import android.graphics.Color
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
import androidx.activity.addCallback
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class AnadirReceta2() : Fragment() {


    private var stepCount = 1
    //Crea un firebaseAuth object

    private val editTextsList = mutableListOf<EditText>()
    private var isEditing = false
    private var recipeId: String? = null
    private  var nrac: String?= null
    private var tprep: String?= null
    private var ingList: ArrayList<IngredientesData> = ArrayList<IngredientesData>()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isEditing = arguments?.getBoolean("isEditing") ?: false
        recipeId = arguments?.getString("recipeId")
        // Inflate the layout for this fragment
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            if (!findNavController().navigateUp()){
                if(isEnabled){
                    isEnabled = false
                    findNavController().navigate(R.id.action_pantallaAnadirReceta2_to_pantallaAnadirReceta)
                }
            }
        }

        if(isEditing){
            loadExistingRecipeData()
        }

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
        var observedUserName = ""
        var observedUserImage = ""

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

        sharedViewModel.userName.observe(viewLifecycleOwner, Observer { userName ->
            // Manejar los pasos
            observedUserName = userName
        })

        sharedViewModel.userImage.observe(viewLifecycleOwner, Observer { userImage ->
            // Manejar los pasos
            observedUserImage = userImage
        })









        auth = Firebase.auth
        nextPageButton.setOnClickListener {

            tprep = view.findViewById<EditText>(R.id.tiempoInput).text.toString()
            nrac = view.findViewById<EditText>(R.id.racionesInput).text.toString()
            //ingList = ArrayList<ArrayList<String>>()
            var ingn = ArrayList<String>()
            ingList = ArrayList<IngredientesData>()
            for(i in 0 until stepsLayout.childCount){
                val step = stepsLayout.getChildAt(i) as LinearLayout
                val noming = step.getChildAt(0) as? EditText
                val caning = step.getChildAt(1) as? EditText
                val mling = step.getChildAt(2) as? EditText
                val dataIng = IngredientesData(noming?.text.toString(), caning?.text.toString(), mling?.text.toString())
                ingn.add(noming?.text.toString())
                ingn.add(caning?.text.toString())
                ingn.add(mling?.text.toString())
                ingList.add(dataIng)
                Log.i(tag,caning?.text.toString())
            }
            //pag3
            lifecycleScope.launch{
                registraValores(observedRecipeName,observedImageUris,observedRecipeDescription,observedSteps, observedUserName, observedUserImage)
            }
        }



        //Aqui recogemos los datos que vienen de añadirReceta1



    }
    private suspend fun registraValores(recipeName: String, imageUris: List<String>, recipeDescription: String, steps: List<String>, userName: String, userImage: String){
        // Obtener la instancia de FirebaseAuth
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val urls = uploadImagesAndGetURLs(imageUris)
        if(user != null){
            val uid = user.uid
            Log.d("Firebase", "Usuario autenticado con UID: ${user.uid}")
            val databaseUserRef = Firebase.database("https://piochef-effb5-default-rtdb.europe-west1.firebasedatabase.app").getReference("users")
            val recipeData = hashMapOf(
                "recipeName" to recipeName,
                "imageUrls" to urls,
                "recipeDescription" to recipeDescription,
                "steps" to steps,
                "preparationTime" to tprep,
                "numRations" to nrac,
                "ingredients" to ingList,
                "user" to user.email,
                "userName" to userName,
                "userImage" to userImage
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


    suspend fun uploadImagesAndGetURLs(imagePaths: List<String>): List<String> = coroutineScope {
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

    private fun loadExistingRecipeData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId ?: "")
        Log.d("AnadirReceta1", "Se carga la receta ${recipeId} ${isEditing}")
        databaseReference!!.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                nrac = dataSnapshot.child("numRations").getValue(String::class.java)
                tprep = dataSnapshot.child("preparationTime").getValue(String::class.java)
                ingList = dataSnapshot.child("ingredients").getValue(object : GenericTypeIndicator<ArrayList<IngredientesData>>() {})?: ArrayList()

                updateUIWithData()
            }
        }.addOnFailureListener { exception ->
            Log.e("AnadirReceta1", "Error al cargar datos de la receta: ${exception.message}")
        }
    }

    private fun updateUIWithData() {
        view?.findViewById<EditText>(R.id.tiempoInput)?.setText(tprep)
        view?.findViewById<EditText>(R.id.racionesInput)?.setText(nrac)

        Log.d("AnadirReceta1", "Error al cargar datos de la receta: ${ingList}")
        val ingredientsLayout = view?.findViewById<LinearLayout>(R.id.stepsLayout)
        ingredientsLayout?.removeAllViews()

        if (ingList != null) {
            for (ingredient in ingList!!) {
                val newIngredient = LinearLayout(context)
                newIngredient.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                ingredientsLayout?.orientation = LinearLayout.VERTICAL

                val ingredientNameEditText = EditText(context)
                ingredientNameEditText.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
                )
                ingredientNameEditText.hint = "Nombre"
                ingredientNameEditText.setText(ingredient.ingrediente)
                newIngredient.addView(ingredientNameEditText)

                val ingredientQuantityEditText = EditText(context)
                ingredientQuantityEditText.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
                )
                ingredientQuantityEditText.hint = "Cantidad"
                ingredientQuantityEditText.setText(ingredient.cantidad)
                newIngredient.addView(ingredientQuantityEditText)

                val ingredientUnitEditText = EditText(context)
                ingredientUnitEditText.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
                )
                ingredientUnitEditText.hint = "Unidad"
                ingredientUnitEditText.setText(ingredient.unidad)
                newIngredient.addView(ingredientUnitEditText)

                ingredientsLayout?.addView(newIngredient)
            }
        }
    }
}