package com.example.trabajofinalv2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Repo {

    /*fun getUserData():LiveData<MutableList<Recipe>>{
        val mutableData = MutableLiveData<MutableList<Recipe>>()
        FirebaseFirestore.getInstance().collection("recipes").get().addOnSuccessListener {result ->
            val listData = mutableListOf<Recipe>()
            for(recipe in result){

                val imageUrls = recipe.get("imageUrls") as? List<String>
                val imageUrl = imageUrls?.firstOrNull() ?: "https://i.postimg.cc/d3DwfWKn/circulo-Pio-Chef.png"

                val user = recipe.getString("user")
                val recipeName = recipe.getString("recipeName")
                val recipe = Recipe(user!!,imageUrl,recipeName!!)
                listData.add(recipe)
            }
            mutableData.value = listData
        }
        return mutableData
    }*/

    fun getUserData(): LiveData<MutableList<Recipe>> {
        val mutableData = MutableLiveData<MutableList<Recipe>>()
        val databaseReference = Firebase.database("https://piochef-effb5-default-rtdb.europe-west1.firebasedatabase.app").getReference("recipes")
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Log.e("Entre en getUserData", "TRUE")
        if(user != null){
            Log.e("Entre en user!=null", "TRUE")
            databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var listData = mutableListOf<Recipe>()
                    for(recipeSnapshot in snapshot.children){
                        val imageUrls = recipeSnapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>(){})
                        val imageUrl = imageUrls?.firstOrNull() ?: "https://i.postimg.cc/d3DwfWKn/circulo-Pio-Chef.png"

                        val user = recipeSnapshot.child("userName").getValue(String::class.java)
                        val recipeName = recipeSnapshot.child("recipeName").getValue(String::class.java)
                        if (user != null && recipeName != null) {
                            val recipe = Recipe(user, imageUrl, recipeName)
                            listData.add(recipe)
                            Log.d("RecipeCreation", "Recipe created: User = $user, ImageUrl = $imageUrl, RecipeName = $recipeName")
                        }
                    }
                    mutableData.value = listData
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                    Log.e("Firebase", "Error al leer datos", error.toException())
                }
            })
        }
        /*databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var listData = mutableListOf<Recipe>()
                for(recipeSnapshot in snapshot.children){
                    val imageUrls = recipeSnapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>(){})
                    val imageUrl = imageUrls?.firstOrNull() ?: "https://i.postimg.cc/d3DwfWKn/circulo-Pio-Chef.png"

                    val user = recipeSnapshot.child("user").getValue(String::class.java)
                    val recipeName = recipeSnapshot.child("recipeName").getValue(String::class.java)
                    if (user != null && recipeName != null) {
                        val recipe = Recipe(user, imageUrl, recipeName)
                        listData.add(recipe)
                    }
                }
                mutableData.value = listData
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Log.e("Firebase", "Error al leer datos", error.toException())
            }
        })*/
        return mutableData

    }
    fun getStorageUserData(): LiveData<MutableList<Recipe>>{
        val mutableData = MutableLiveData<MutableList<Recipe>>()
        val databaseReference = Firebase.database("https://piochef-effb5-default-rtdb.europe-west1.firebasedatabase.app").getReference("recipes")
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        var dataReference = ArrayList<String>()
        Log.e("Entre en getUserData", "TRUE")
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val descripcion = snapshot.child("recetasGuardadas").value as? ArrayList<String>
                    if (descripcion != null) {
                        if (snapshot.exists() && snapshot.hasChild("recetasGuardadas")) {
                            for (value in descripcion) {
                                dataReference.add(value)
                            }
                            val databaseReferenceRecipes = Firebase.database("https://piochef-effb5-default-rtdb.europe-west1.firebasedatabase.app").getReference("recipes")
                            databaseReferenceRecipes.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var listData = mutableListOf<Recipe>()
                                    for(recipeSnapshot in snapshot.children){
                                        val imageUrls = recipeSnapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>(){})
                                        val imageUrl = imageUrls?.firstOrNull() ?: "https://i.postimg.cc/d3DwfWKn/circulo-Pio-Chef.png"

                                        val user = recipeSnapshot.child("user").getValue(String::class.java)
                                        val recipeName = recipeSnapshot.child("recipeName").getValue(String::class.java)
                                        if (user != null && recipeName != null) {
                                            Log.e("a",recipeName)
                                            for(a in dataReference){
                                                Log.e("a",a)
                                            }
                                            if (recipeName in dataReference){

                                                val recipe = Recipe(user, imageUrl, recipeName)
                                                listData.add(recipe)
                                                Log.d("RecipeCreation", "Recipe created: User = $user, ImageUrl = $imageUrl, RecipeName = $recipeName")
                                            }
                                        }
                                    }
                                    mutableData.value = listData
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                    Log.e("Firebase", "Error al leer datos", error.toException())
                                }
                            })
                        }
                    }else {

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        return mutableData
    }
}