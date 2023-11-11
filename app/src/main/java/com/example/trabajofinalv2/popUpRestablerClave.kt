package com.example.trabajofinalv2

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class popUpRestablerClave: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle("")
            .setMessage("Contraseña restablecida con éxito")
            .setPositiveButton("Aceptar") { dialog, _ ->
                // Acción cuando se hace clic en Aceptar
                dialog.dismiss()
            }
            .create()
    }
}