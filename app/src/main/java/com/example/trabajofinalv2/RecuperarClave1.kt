package com.example.trabajofinalv2


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class RecuperarClave1: Fragment() {
    
    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restablecer_password1, container, false)

        val btnSend = view.findViewById<Button>(R.id.btnEnviar)
        val btnCancel = view.findViewById<Button>(R.id.btnCancelar)
        val inputCorreo = view.findViewById<EditText>(R.id.correoRecuperacion)

        inputCorreo.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val isValid = isValidEmail(s.toString())
                btnSend.isEnabled = !s.isNullOrBlank() && isValid

                if (btnSend.isEnabled) {
                    btnSend.setBackgroundColor(Color.parseColor("#114B8D"))
                } else {
                    btnSend.setBackgroundColor(Color.parseColor("#416FA4"))
                }
            }
        })

        btnSend.setOnClickListener {
            val email: String = inputCorreo.text.toString().trim { it <= ' '}
            if(email.isEmpty()){
                Toast.makeText(requireActivity(), "Introduce un correo", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(requireActivity(), "Email enviado correctamente", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_pantallaClave1_to_pantallaDeInicio)
                    }
                }
            }
            //findNavController().navigate(R.id.action_pantallaClave1_to_pantallaClave2)
        }
        btnCancel.setOnClickListener {

            findNavController().navigate(R.id.action_pantallaClave1_to_pantallaDeInicio)
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            if (!findNavController().navigateUp()){
                if(isEnabled){
                    isEnabled = false
                }
            }
        }
        return view
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}