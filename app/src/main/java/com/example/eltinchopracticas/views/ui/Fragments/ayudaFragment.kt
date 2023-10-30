package com.example.eltinchopracticas.views.ui.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.eltinchopracticas.R
import com.example.eltinchopracticas.views.ui.activities.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class ayudaFragment : Fragment() {
    lateinit var firebaseAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ayuda, container, false)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_navigation_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem):Boolean{

        return when (item.itemId){
            R.id.pedidos->{
                findNavController().navigate(R.id.action_ayudaFragment_to_pedidosFragment)
                true
            }
            R.id.cerrar->{
                firebaseAuth.signOut()
                //findNavController().navigate(R.id.action_rutaFragment_to_loginActivity)
                val intent= Intent(activity, LoginActivity::class.java)
                startActivity (intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override  fun onCreate (savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        firebaseAuth= Firebase.auth
    }
    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val button=view.findViewById<BottomNavigationView>(R.id.buttonNavigationMenu)
        button.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.home->findNavController().navigate(R.id.action_ayudaFragment_to_menuFragment)
                R.id.platillos->findNavController().navigate(R.id.action_ayudaFragment_to_comidaFragment)
                R.id.contactanos->findNavController().navigate(R.id.action_ayudaFragment_to_rutaFragment)
                R.id.favoritos->findNavController().navigate(R.id.action_ayudaFragment_to_favoritosFragment)
                R.id.perfil->findNavController().navigate(R.id.action_ayudaFragment_to_perfilFragment)
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(view?.findViewById(R.id.actionbartoolbar))
    }
}