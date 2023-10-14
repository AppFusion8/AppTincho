package com.example.eltinchopracticas.views.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eltinchopracticas.R
import com.example.eltinchopracticas.views.ui.Fragments.perfilFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var googleSignInClient: GoogleSignInClient
    //private lateinit var authStateListener: AuthStateListener
    @SuppressLint("MissingInflateId")
    lateinit var registrobutton:TextView
    lateinit var iniciobutton:Button
    lateinit var recuperarbutton: TextView
    lateinit var pruebabutton:Button
    lateinit var googleButton: Button
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = Firebase.auth
        database=FirebaseDatabase.getInstance()
        dbReference=database.reference.child("User")
        //auth con Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        FirebaseApp.initializeApp(this)

        val email: EditText = findViewById(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)
        googleButton=findViewById(R.id.signInGoogle)
        iniciobutton = findViewById(R.id.BotonInicio)
        recuperarbutton = findViewById(R.id.BotonRecuperar)
        registrobutton = findViewById(R.id.BotonRegistroLogin)
        pruebabutton = findViewById(R.id.masterKey)

        //auth con correo y constraseña
        iniciobutton.setOnClickListener {
            login(email.text.toString(), password.text.toString())
        }
        recuperarbutton.setOnClickListener {
            startActivity(Intent(this, RecuperarActivity::class.java))
        }
        registrobutton.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
        pruebabutton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        googleButton.setOnClickListener{
            signInGoogle()
        }
    }

    private fun login(email:String,password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task->
                if(task.isSuccessful){
                    val user=firebaseAuth.currentUser
                    Toast.makeText(baseContext,user?.uid.toString(),Toast.LENGTH_SHORT).show()
                    val i=Intent(this, HomeActivity::class.java)
                    startActivity(i)
                } else{
                    Toast.makeText(baseContext,"Revise que los datos sean correctos",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun signInGoogle(){
        val signInIntent= googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
            if(result.resultCode==Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount? = task.result
            if(account!=null){
                updateUI(account)
            }
        } else{
            Toast.makeText(this, task.exception.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val intent: Intent = Intent(this,HomeActivity::class.java)
                //intent.putExtra("email",account.email)
                //intent.putExtra("perfil", account.photoUrl.toString())
                //intent.putExtra("google", true)
                try {
                    val user = firebaseAuth.currentUser
                    val userdb = dbReference.child(user?.uid.toString())
                    userdb.child("name").setValue(account.displayName)
                    userdb.child("email").setValue(account.email)
                    userdb.child("foto").setValue(account.photoUrl.toString())
                }catch (ex:Exception){
                    Toast.makeText(this,ex.message, Toast.LENGTH_SHORT).show()
                }
                startActivity(intent)
            }else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

}