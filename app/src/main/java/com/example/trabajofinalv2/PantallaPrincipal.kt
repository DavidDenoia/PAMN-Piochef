package com.example.trabajofinalv2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PantallaPrincipal : Fragment(R.layout.fragment_pantalla_principal) {

    private lateinit var adapter:MainAdapter
    private val viewModel by lazy{
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pantalla_principal, container, false)
        //Saltar a la siguiente pagina de introduccion
        val btnInicial = view.findViewById<Button>(R.id.foto)
        btnInicial.setOnClickListener {
            findNavController().navigate(R.id.action_pantallaprincipal_to_pantallaperfil)
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MainAdapter(requireContext(), object : MainAdapter.OnRecipeClickListener{
            override fun onRecipeClick(recipeName: String, user: String) {
                //Log.e("RecipeClick", "Nombre de la receta: $recipeName, Usuario: $user")
                val bundle = bundleOf("recipeName" to recipeName,
                    "user" to user)
                findNavController().navigate(R.id.action_pantallaMenuInferior_to_verRecetas, bundle)
            }
        })
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recetasRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        recyclerView?.adapter = adapter

        observeData()
        /*val dummyList = mutableListOf<Recipe>()
        dummyList.add(Recipe("Paco","https://s3.abcstatics.com/media/gurmesevilla/2012/01/comida-rapida-casera.jpg","Hamburguesa"))
        dummyList.add(Recipe("Pepe","https://s3.abcstatics.com/media/gurmesevilla/2012/01/comida-rapida-casera.jpg","Hamburguesa2"))



        adapter.setListData(dummyList)
        adapter.notifyDataSetChanged()*/
    }

    fun observeData(){
        viewModel.fetchRecipeData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }


}