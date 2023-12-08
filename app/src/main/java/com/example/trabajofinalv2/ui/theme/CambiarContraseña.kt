package com.example.trabajofinalv2.ui.theme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.trabajofinalv2.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class CambiarContraseña : Fragment() {

    lateinit var cancelBtn: Button
    lateinit var backBtn: ImageView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var editTextActualPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextRepeatNewPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.cambiar_contrasena, container, false)

        cancelBtn = view.findViewById(R.id.buttonCancel)
        backBtn = view.findViewById(R.id.flecha_atras)

        mAuth = FirebaseAuth.getInstance()
        editTextActualPassword = view.findViewById(R.id.editTextActualPassword)
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword)
        editTextRepeatNewPassword = view.findViewById(R.id.editTextRepeatNewPassword)

        val buttonChangePassword: Button = view.findViewById(R.id.buttonChangePassword)
        buttonChangePassword.setOnClickListener {
            handleChangePassword()
        }

        cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cambiarContraseña_to_pantallaEditarPefil)
        }
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cambiarContraseña_to_pantallaEditarPefil)
        }
        return view
    }

    private fun handleChangePassword() {
        val currentPassword = editTextActualPassword.text.toString()
        val newPassword = editTextNewPassword.text.toString()
        val repeatNewPassword = editTextRepeatNewPassword.text.toString()

        if (newPassword == repeatNewPassword) {
            val user = mAuth.currentUser

            if (user != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

                user.reauthenticate(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            if (newPassword.length >= 6) {
                                user.updatePassword(newPassword)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            Toast.makeText(context,"Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Error al cambiar contraseña", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "La nueva contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }
    }




}