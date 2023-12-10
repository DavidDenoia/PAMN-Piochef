package com.example.trabajofinalv2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainAdapter(
    private val context: Context,
    private val itemClickListener: OnRecipeClickListener
): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    interface OnRecipeClickListener{
        fun onRecipeClick(recipeName: String, user: String)
    }
    private var dataList = mutableListOf<Recipe>()

    fun setListData(data: MutableList<Recipe>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.receta_item, parent,false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(dataList.size > 0){
            return dataList.size
        }else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val recipe = dataList[position]
        holder.bindView(recipe)
    }

    inner class MainViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(receta: Recipe){
            val imageView = itemView.findViewById<ImageView>(R.id.recipe_image)
            Glide.with(context).load(receta.imageUrl).into(imageView)
            val nombreUsuario = itemView.findViewById<TextView>(R.id.user_name)
            nombreUsuario.text = receta.user
            val nombreReceta = itemView.findViewById<TextView>(R.id.recipe_name)
            nombreReceta.text = receta.recipeName

            /*itemView.setOnClickListener{itemClickListener.onRecipeClick(receta.recipeName,receta.user)}*/
            val cardView = itemView.findViewById<CardView>(R.id.cardViewId)
            cardView.setOnClickListener {
                itemClickListener.onRecipeClick(receta.recipeName, receta.user)
            }
            val guardar = itemView.findViewById<ImageButton>(R.id.guardarboton)
            guardar.setOnClickListener {
                guardarDatosRecetas(receta.user, receta.recipeName)
            }
            val checkBorrar = checkErase(itemView,receta.user,receta.recipeName)
            val borrar = itemView.findViewById<ImageButton>(R.id.borrar)
            borrar.setOnClickListener {
                borrarDatosRecetas(receta.user, receta.recipeName)
            }
        }
    }
    private fun guardarDatosRecetas(usuario: String,nombreReceta:String ){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val datosReceta = ArrayList<String>()
        datosReceta.add(usuario)
        datosReceta.add(nombreReceta)
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("recetasGuardadas")) {
                        val descripcion = snapshot.child("recetasGuardadas").value as ArrayList<String>
                        for (value in descripcion){
                            datosReceta.add(value)
                        }
                        if(datosReceta.get(0) in descripcion && datosReceta.get(1) in descripcion){
                            datosReceta.removeAt(0)
                            datosReceta.removeAt(0)
                        }
                        val updates = hashMapOf<String, Any>(
                            "recetasGuardadas" to datosReceta
                        )
                        databaseReference.updateChildren(updates)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Receta guardada", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val updates = hashMapOf<String, Any>(
                            "recetasGuardadas" to datosReceta
                        )
                        databaseReference.updateChildren(updates)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Receta guardada", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


        }
    }
    private fun borrarDatosRecetas(usuario: String,nombreReceta:String ){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("recetasGuardadas")) {
                        val descripcion = snapshot.child("recetasGuardadas").value as ArrayList<String>
                        descripcion.remove(usuario)
                        descripcion.remove(nombreReceta)
                        val updates = hashMapOf<String, Any>(
                            "recetasGuardadas" to descripcion
                        )
                        databaseReference.updateChildren(updates)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Receta borrada", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


        }
    }
    private fun checkErase(itemView: View,usuario: String,nombreReceta:String){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("recetasGuardadas")) {
                        val receta = snapshot.child("recetasGuardadas").value as ArrayList<String>
                        val borrar = itemView.findViewById<ImageButton>(R.id.borrar)
                        val guardar = itemView.findViewById<ImageButton>(R.id.guardarboton)
                        if(usuario in receta && nombreReceta in receta){
                            borrar.visibility = View.VISIBLE
                            guardar.visibility = View.GONE
                        } else{
                            borrar.visibility = View.GONE
                            guardar.visibility = View.VISIBLE
                        }
                    } else {
                        val borrar = itemView.findViewById<ImageButton>(R.id.borrar)
                        val guardar = itemView.findViewById<ImageButton>(R.id.guardarboton)

                        borrar.visibility = View.GONE
                        guardar.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


        }
    }
}