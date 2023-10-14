package com.example.eltinchopracticas.views.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.eltinchopracticas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegistroActivity : AppCompatActivity() {
    lateinit var buttonregistro: Button
    lateinit var buttonlogin: Button
    private lateinit var nombre:EditText
    private lateinit var celular:EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var database:FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        firebaseAuth= Firebase.auth
        database=FirebaseDatabase.getInstance()
        dbReference=database.reference.child("User")
        buttonregistro=findViewById(R.id.registro)
        buttonlogin=findViewById(R.id.loginRegistro)
        nombre=findViewById(R.id.signUpName)
        val correo=findViewById<EditText>(R.id.signUpEmail)
        celular=findViewById(R.id.signUpPhone)
        val password=findViewById<EditText>(R.id.signUpPassword)
        buttonregistro.setOnClickListener{
            createUser(correo.text.toString(),password.text.toString())
        }
        buttonlogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }
    private fun createUser(email:String, password:String) {
        val name: String=nombre.text.toString()
        val cel:String=celular.text.toString()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                    Task ->if (Task.isSuccessful) {
                        val user=firebaseAuth.currentUser
                        val userdb=dbReference.child(user?.uid.toString())
                        userdb.child("name").setValue(name)
                        userdb.child("celular").setValue(cel)
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        Toast.makeText(applicationContext,Task.exception.toString(),Toast.LENGTH_LONG).show()

                    }
            }
    }
}