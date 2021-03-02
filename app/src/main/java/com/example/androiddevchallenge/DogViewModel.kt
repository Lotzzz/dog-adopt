package com.example.androiddevchallenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DogViewModel : ViewModel() {
    private val _result = MutableLiveData<List<Dog>>()
    val result: LiveData<List<Dog>> = _result

    fun getDogs() {
        viewModelScope.launch {
            val data = searchDogs()
            Log.d("11111", data?.results?.toString().toString())
            _result.postValue(data?.results ?: emptyList())
        }
    }
}