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
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PantallaGuardarRecetas : Fragment(R.layout.fragment_pantalla_principal) {

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


        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            if (!findNavController().navigateUp()){
                if(isEnabled){
                    isEnabled = false
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MainAdapter(requireContext(), object : MainAdapter.OnRecipeClickListener{
            override fun onRecipeClick(recipeName: String, user: String) {
                obtainRecipeId(recipeName) { recipeId ->
                    if (recipeId != null) {
                        val bundle = bundleOf(
                            "recipeName" to recipeName,
                            "user" to user,
                            "recipeId" to recipeId
                        )

                        findNavController().navigate(R.id.action_pantallaMenuInferior_to_verRecetas, bundle)
                    } else {
                        // Manejar el caso en el que no se encontró ninguna receta con ese nombre
                    }
                }
            }
        })
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recetasRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        recyclerView?.adapter = adapter
        observeData()
    }

    fun observeData(){
        viewModel.fetchStoredRecipeData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun obtainRecipeId(recipeName: String, callback: (String?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes")
        databaseReference.orderByChild("recipeName").equalTo(recipeName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (recipeSnapshot in dataSnapshot.children) {
                            val recipeId = recipeSnapshot.key
                            callback(recipeId)
                            return
                        }
                    } else {
                        callback(null)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
    }

}