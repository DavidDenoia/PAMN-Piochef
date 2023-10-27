package com.example.trabajofinalv2.ui.theme.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.trabajofinalv2.R
import android.widget.Button


class Introduccion1 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_introduccion1, container, false)



        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        //Saltar a la siguiente pagina de introduccion
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {

            viewPager?.currentItem = viewPager?.currentItem?.plus(1) ?: 0
        }

        //Skipear hasta la ultima pagina
        val btnSaltar = view.findViewById<Button>(R.id.btnSkip)
        btnSaltar.setOnClickListener{
            viewPager?.currentItem = 3
        }

        return view
    }


}