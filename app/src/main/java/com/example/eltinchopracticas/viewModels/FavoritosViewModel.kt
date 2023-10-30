package com.example.eltinchopracticas.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eltinchopracticas.domain.Repository
import com.example.eltinchopracticas.models.favoritos

class FavoritosViewModel:ViewModel() {
    val repository= Repository()
    fun fetchFavoritosData(): LiveData<MutableList<favoritos>> {
        val mutableData= MutableLiveData<MutableList<favoritos>>()
        repository.getDataFavoritosData().observeForever{
            mutableData.value=it
        }
        return mutableData
    }
}