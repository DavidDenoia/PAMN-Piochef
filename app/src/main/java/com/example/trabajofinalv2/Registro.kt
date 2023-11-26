package com.example.trabajofinalv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.IgnoreExtraProperties
import android.util.Log
@IgnoreExtraProperties
data class User(val username: String? = null, val email: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

class Registro : Fragment()
{
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etConfPass: EditText
    private lateinit var  etPass: EditText
    private lateinit var btnSignUp: Button

    //Objeto de autentificacion en Firebase
    private lateinit var auth: FirebaseAuth
    //Objeto de referencia a la base de datos en Firebase
    private  lateinit var database:DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pantalla_de_registro, container, false)

        // View Bindings
        etName = view.findViewById<EditText>(R.id.setName)
        etEmail = view.findViewById(R.id.setEmailAddress)
        etConfPass = view.findViewById(R.id.setConfPassword)
        etPass = view.findViewById(R.id.setPassword)
        btnSignUp = view.findViewById(R.id.btnregistrar)



        // Initialising auth object
        auth = Firebase.auth
        database = Firebase.database("https://piochef-effb5-default-rtdb.europe-west1.firebasedatabase.app").reference

        btnSignUp.setOnClickListener {
            registrarUsuario()
        }


        val btnNext = view.findViewById<Button>(R.id.btniniciar)

        btnNext.setOnClickListener {

            findNavController().navigate(R.id.action_pantallaDeRegistro_to_pantallaDeInicio)
        }


        return view
    }
    private fun registrarUsuario(){
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()
        val name= etName.text.toString()

        //Comprobar campos
        if(name.isBlank() || email.isBlank() || pass.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(requireActivity(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if(pass != confirmPassword){
            Toast.makeText(requireActivity(), "Contraseña y confirmar contraseña no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Usuario creado exitosamente
                val user = auth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { verifTask ->
                    if (verifTask.isSuccessful) {
                        // Correo de verificación enviado
                        Toast.makeText(requireActivity(), "Verifica tu correo electrónico", Toast.LENGTH_SHORT).show()
                    } else {
                        // Error al enviar correo de verificación
                        Toast.makeText(requireActivity(), "Error al enviar correo de verificación: ${verifTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                // Guardar información adicional del usuario en la base de datos
                val newUser = User(name, email)
                val userId = user?.uid
                userId?.let { uid ->
                    database.child("users").child(uid).setValue(newUser)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                Toast.makeText(requireActivity(), "Nombre y email guardados en la base de datos", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_pantallaDeRegistro_to_pantallaDeMenu)
                            } else {
                                // Error al guardar en la base de datos
                                dbTask.exception?.let {
                                    Log.e("RegistroFragment", "Falló al guardar el usuario en la base de datos", it)
                                    Toast.makeText(requireActivity(), "Error al guardar usuario: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
            } else {
                // Error al crear usuario
                Toast.makeText(requireActivity(), "Registro fallido: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }
}

