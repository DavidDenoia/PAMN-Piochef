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
        viewPager.setPageTransformer(DepthPageTransformer())
        return view
    }


}

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            when {
                position < -1 -> {

                    alpha = 0f
                }
                position <= 0 -> {

                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> {

                    alpha = 1 - position


                    translationX = pageWidth * -position


                    val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]

                    alpha = 0f
                }
            }
        }
    }

    companion object {
        private const val MIN_SCALE = 0.75f
    }
}