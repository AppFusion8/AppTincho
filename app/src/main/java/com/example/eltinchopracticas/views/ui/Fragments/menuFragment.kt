package com.example.eltinchopracticas.views.ui.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.eltinchopracticas.R

class menuFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_menu, container, false)
        return view
    }
    override fun onViewCreated(view:View,  savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val cardPlatillo=view.findViewById<ImageView>(R.id.cardPlatillos)
        cardPlatillo.setOnClickListener{
            findNavController().navigate(R.id.action_menuFragment_to_comidaFragment)
        }
        
        val cardPerfil=view.findViewById<ImageView>(R.id.cardMiCuenta)
        cardPerfil.setOnClickListener(){
            findNavController().navigate(R.id.action_menuFragment_to_perfilFragment)
        }
    }
}