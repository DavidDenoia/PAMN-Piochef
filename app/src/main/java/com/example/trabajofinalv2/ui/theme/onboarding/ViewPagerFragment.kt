package com.example.trabajofinalv2.ui.theme.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.trabajofinalv2.R
import com.example.trabajofinalv2.ui.theme.onboarding.screens.Introduccion1
import com.example.trabajofinalv2.ui.theme.onboarding.screens.Introduccion2
import com.example.trabajofinalv2.ui.theme.onboarding.screens.Introduccion3
import com.example.trabajofinalv2.ui.theme.onboarding.screens.Introduccion4



class ViewPagerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            Introduccion1(),
            Introduccion2(),
            Introduccion3(),
            Introduccion4()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = adapter

        return view
    }


}