package com.example.eltinchopracticas.views.ui.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.eltinchopracticas.R
import com.example.eltinchopracticas.views.ui.activities.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


@Suppress("DEPRECATION")
class perfilFragment : Fragment() {
    lateinit var firebaseAuth:FirebaseAuth
    val database:DatabaseReference= FirebaseDatabase.getInstance().getReference("User")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_perfil, container, false)
        val  nombreCompleto=view.findViewById<EditText>(R.id.nombrecompleto)
        val correoCompleto=view.findViewById<EditText>(R.id.correoelectronico)
        val celular=view.findViewById<EditText>(R.id.celularcompleto)
        val foto=view?.findViewById<ImageView>(R.id.fotoperfilgeneral)
        firebaseAuth= Firebase.auth


        //traigo info de la base de datos y la pongo en los edit text
        val user=firebaseAuth.currentUser
        correoCompleto.setText(user?.email.toString())
        database.child(user?.uid.toString()).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nombreCompleto.setText(snapshot.child("name").value.toString())
                celular.setText(snapshot.child("celular").value.toString())
                //val fotoUsuario=snapshot.child("foto").value.toString()
                try{
                    val fotoUsuario=snapshot.child("foto").value.toString()
                    if(fotoUsuario!=null){
                        Picasso.with(context).load(fotoUsuario).into(foto)
                    }
                }catch (ex:Exception){
                    Toast.makeText(activity,ex.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        val btneditnombre=view.findViewById<ImageButton>(R.id.nombreedit)
        val btneditcorrreo=view.findViewById<ImageButton>(R.id.correoedit)
        val btneditcelular=view.findViewById<ImageButton>(R.id.celularedit)

        nombreCompleto.isEnabled=false
        correoCompleto.isEnabled=false
        celular.isEnabled=false
        btneditnombre.setOnClickListener{
            if(nombreCompleto.isEnabled==false){
                nombreCompleto.isEnabled=true
            }else if(nombreCompleto.isEnabled==true){
                nombreCompleto.isEnabled=false
            }
        }
        btneditcorrreo.setOnClickListener{
            if(correoCompleto.isEnabled==false){
                correoCompleto.isEnabled=true
            }else if(correoCompleto.isEnabled==true){
                correoCompleto.isEnabled=false
            }
        }
        btneditcelular.setOnClickListener{
            if(celular.isEnabled==false){
                celular.isEnabled=true
            }else if(celular.isEnabled==true){
                celular.isEnabled=false
            }
        }
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_navigation_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem):Boolean{

        return when (item.itemId){
            R.id.ayuda->{
                findNavController().navigate(R.id.action_perfilFragment_to_ayudaFragment)
                true
            }
            R.id.pedidos->{
                findNavController().navigate(R.id.action_perfilFragment_to_pedidosFragment)
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

    }
    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val button=view.findViewById<BottomNavigationView>(R.id.buttonNavigationMenu)
        button.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.home->findNavController().navigate(R.id.action_perfilFragment_to_menuFragment)
                R.id.platillos->findNavController().navigate(R.id.action_perfilFragment_to_comidaFragment)
                R.id.contactanos->findNavController().navigate(R.id.action_perfilFragment_to_rutaFragment)
                R.id.favoritos->findNavController().navigate(R.id.action_perfilFragment_to_favoritosFragment)
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(view?.findViewById(R.id.actionbartoolbar))

        val btmcamara=view.findViewById<ImageButton>(R.id.btncamara)
        btmcamara.setOnClickListener{
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 123)
        }
        val btmgaleria=view.findViewById<ImageButton>(R.id.btngaleria)
        btmgaleria.setOnClickListener{
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent, 456)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        //val imageView=view?.findViewById<ImageView>(R.id.fotoperfil)
        val imageView2=view?.findViewById<ImageView>(R.id.fotoperfilgeneral)
        //camara
        if(requestCode==123){
            var bitmap=data?.extras?.get("data") as Bitmap
            //imageView?.setImageBitmap(bitmap)
            imageView2?.setImageBitmap(bitmap)
        } else if (requestCode==456){
            //imageView?.setImageURI(data?.data)
            imageView2?.setImageURI(data?.data)
        }
    }

}