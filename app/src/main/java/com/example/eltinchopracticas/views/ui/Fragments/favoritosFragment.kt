package com.example.eltinchopracticas.views.ui.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eltinchopracticas.R
import com.example.eltinchopracticas.models.favoritos
import com.example.eltinchopracticas.viewModels.FavoritosViewModel
import com.example.eltinchopracticas.views.adapter.FavoritosAdapter
import com.example.eltinchopracticas.views.ui.activities.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class favoritosFragment : Fragment(),FavoritosAdapter.OnDeseosItemClickLitener {
    val data:FirebaseFirestore=FirebaseFirestore.getInstance()
    lateinit var recyclerFav: RecyclerView
    lateinit var adapter: FavoritosAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(FavoritosViewModel::class.java) }
    lateinit var firebaseAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favoritos, container, false)
        recyclerFav=view.findViewById(R.id.recyclerviewfavoritos)
        adapter= FavoritosAdapter(requireContext(),this)
        recyclerFav.layoutManager=LinearLayoutManager(context)
        recyclerFav.adapter=adapter
        observeData()
        return view
    }
    fun observeData(){
        viewModel.fetchFavoritosData().observe(viewLifecycleOwner,{
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_navigation_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem):Boolean{

        return when (item.itemId){
            R.id.ayuda->{
                findNavController().navigate(R.id.action_favoritosFragment_to_ayudaFragment)
                true
            }
            R.id.pedidos->{
                findNavController().navigate(R.id.action_favoritosFragment_to_pedidosFragment)
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
                R.id.home->findNavController().navigate(R.id.action_favoritosFragment_to_menuFragment)
                R.id.platillos->findNavController().navigate(R.id.action_favoritosFragment_to_comidaFragment)
                R.id.contactanos->findNavController().navigate(R.id.action_favoritosFragment_to_rutaFragment)
                R.id.perfil->findNavController().navigate(R.id.action_favoritosFragment_to_perfilFragment)

            }
        }
        (activity as AppCompatActivity).setSupportActionBar(view?.findViewById(R.id.actionbartoolbar))
    }

    override fun onItemClick(favorito: favoritos, position: Int) {
        data.collection("favoritos")
            .document(favorito.titulo)
            .delete()
        observeData()
    }
}