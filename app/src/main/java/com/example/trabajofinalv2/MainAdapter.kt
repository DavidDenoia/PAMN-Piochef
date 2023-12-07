package com.example.trabajofinalv2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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

        }
    }
}