package com.example.trabajofinalv2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val repo = Repo()
    fun fetchRecipeData(): LiveData<MutableList<Recipe>>{
        val mutableData = MutableLiveData<MutableList<Recipe>>()
        repo.getUserData().observeForever{
            mutableData.value = it
        }

        return mutableData

    }
}