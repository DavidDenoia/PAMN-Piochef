package com.example.trabajofinalv2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class SharedViewModel : ViewModel() {
    val recipeName = MutableLiveData<String>()
    val imageUris = MutableLiveData<List<String>>()
    val recipeDescription = MutableLiveData<String>()
    val steps = MutableLiveData<List<String>>()
    val user = MutableLiveData<List<String>>()
}