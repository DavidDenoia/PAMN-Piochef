package com.example.trabajofinalv2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.view.Gravity
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController


class PantallaPerfil : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pantalla_perfil, container, false)

        val menuButton = view.findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener { v ->
            showPopupMenu(v)
        }


        return view
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_desplegable, popup.menu)
        popup.setOnMenuItemClickListener{ item: MenuItem? ->

            when (item!!.itemId) {
                R.id.añadirreceta -> {
                    //perfil a añadir receta
                    findNavController().navigate(R.id.action_pantallaMenuInferior_to_pantallaAnadirReceta)
                }
                R.id.recetaguradada -> {
                    //perfil a receta guardada
                }
                R.id.editarperfil -> {
                    findNavController().navigate(R.id.action_pantallaMenuInferior_to_pantallaEditarPefil)
                }
            }

            true
        }
        popup.show()
    }




}