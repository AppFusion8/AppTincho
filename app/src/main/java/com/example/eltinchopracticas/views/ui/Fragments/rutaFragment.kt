package com.example.eltinchopracticas.views.ui.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.eltinchopracticas.R
import com.example.eltinchopracticas.views.ui.activities.LoginActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class rutaFragment : Fragment(), OnMapReadyCallback {
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googlemap:GoogleMap
    companion object{
        const val REQUEST_CODE_LOCATION=0
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ruta, container, false)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_navigation_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem):Boolean{
        return when (item.itemId){
            R.id.ayuda->{
                findNavController().navigate(R.id.action_rutaFragment_to_ayudaFragment)
                true
            }
            R.id.pedidos->{
                findNavController().navigate(R.id.action_rutaFragment_to_pedidosFragment)
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
        firebaseAuth=Firebase.auth
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val button= view.findViewById<BottomNavigationView>(R.id.buttonNavigationMenu)
        val mapFragment= this.childFragmentManager.findFragmentById(R.id.map_view)
                as SupportMapFragment
        mapFragment.getMapAsync(this)
        //Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        button.setOnNavigationItemReselectedListener {
            when (it.itemId){
                R.id.home->findNavController().navigate(R.id.action_rutaFragment_to_menuFragment)
                R.id.platillos->findNavController().navigate(R.id.action_rutaFragment_to_comidaFragment)
                R.id.perfil->findNavController().navigate(R.id.action_rutaFragment_to_perfilFragment)
                R.id.favoritos->findNavController().navigate(R.id.action_rutaFragment_to_favoritosFragment)
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(view?.findViewById(R.id.actionbartoolbar))

    }
    override fun onMapReady(map: GoogleMap){
        val colombia= com.google.android.gms.maps.model.LatLng(4.991906628177844, -74.07301928836168)
        map?.let{
            this.googlemap=it
            map.addMarker(MarkerOptions()
                .position(colombia))
        }
        enableLocation()
    }
    private fun isLocationPermissionGranted()=ContextCompat.checkSelfPermission(
        this.requireContext(),
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )==PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableLocation(){
        if(!::googlemap.isInitialized)return
        if(isLocationPermissionGranted()){
            googlemap.isMyLocationEnabled=true
        }else{
            requestLocationPermission()
        }
    }
    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this.requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this.context,"Requiere activar permisos en ajustes",Toast.LENGTH_SHORT).show()
        } else{
            ActivityCompat.requestPermissions(this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                com.example.eltinchopracticas.views.ui.Fragments.rutaFragment.Companion.REQUEST_CODE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            com.example.eltinchopracticas.views.ui.Fragments.rutaFragment.Companion.REQUEST_CODE_LOCATION
            -> if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                googlemap.isMyLocationEnabled=true
            } else{
                Toast.makeText(this.context,"Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT).show()
            }
            else ->{}
        }
    }
}