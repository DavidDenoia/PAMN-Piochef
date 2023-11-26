package com.example.trabajofinalv2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

class PantallaDeInicio : Fragment() {

    lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    lateinit var btnLogin: Button

    //Crea un firebaseAuth object
    lateinit var auth: FirebaseAuth
    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pantalla_de_inicio, container, false)

        btnLogin = view.findViewById(R.id.btniniciar)
        etEmail = view.findViewById(R.id.etEmailAddress)
        etPass = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btniniciar)
        val btnNext = view.findViewById<Button>(R.id.btnregistrar)
        val btnolvidar = view.findViewById<Button>(R.id.olvidar)

        //Inicializa la autentificacion de firebase
        auth = FirebaseAuth.getInstance()

        btnNext.setOnClickListener {

            findNavController().navigate(R.id.action_pantallaDeInicio_to_pantallaDeRegistro)
        }

        btnolvidar.setOnClickListener {

            findNavController().navigate(R.id.action_pantallaDeInicio_to_pantallaCambiarClave1)
        }

        btnLogin.setOnClickListener {
            login()
        }
        return view
    }

    private fun login(){
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                val user = auth.currentUser
                if(user != null && user.isEmailVerified){
                    Toast.makeText(requireActivity(), "Iniciada sesion correctamente", Toast.LENGTH_SHORT).show()
                    //Y aqui iria un navigate hacia la pagina del perfil de usuario
                    findNavController().navigate(R.id.action_pantallaDeInicio_to_pantallaDeMenu)
                }else{
                    Toast.makeText(requireActivity(), "Verifica tu correo electrónico", Toast.LENGTH_SHORT).show()
                }


            } else
                Toast.makeText(requireActivity(), "Fallo en inicio de sesion", Toast.LENGTH_SHORT).show()
        }
    }


}