package com.example.eltinchopracticas.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eltinchopracticas.domain.Repository
import com.example.eltinchopracticas.models.compras

class ComprasViewModel :ViewModel() {
    val repository= Repository()
    fun fetchComprasData():LiveData<MutableList<compras>>{
        val mutableData=MutableLiveData<MutableList<compras>>()
        repository.getDataComprasData().observeForever{
            mutableData.value=it
        }
        return mutableData
    }
}