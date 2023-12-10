package com.example.trabajofinalv2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController

class BottomNavigation : Fragment(R.layout.fragment_menu_inferior) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(requireContext(), R.layout.fragment_menu_inferior, null)

        //establece el fragmento en la pantalla principal al inicio + el fragmento
        val newFragment = PantallaPrincipal()
        setCurrentFragment(newFragment)

        val bottomNavigationView: BottomNavigationView =
            view.findViewById(R.id.bottomNavigationView)
//        bottomNavigationView.inflateMenu(R.menu.bottom_menu)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            handleBottonNavigation(item)
            false
        }
        return view
    }

    private fun handleBottonNavigation(item: MenuItem):Boolean {
        when (item.itemId) {
            R.id.nav_cuenta -> {
                val newFragment = PantallaPerfil()
                setCurrentFragment(newFragment)
                return true
            }

            R.id.nav_add_recetas -> {
                val newFragment = AnadirReceta1()
                setCurrentFragment(newFragment)
                return true
            }

            R.id.nav_inicio -> {
                val newFragment = PantallaPrincipal()
                setCurrentFragment(newFragment)
                return true
            }

            else -> return false
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.containerView, fragment)
        transaction?.commit()
    }

}