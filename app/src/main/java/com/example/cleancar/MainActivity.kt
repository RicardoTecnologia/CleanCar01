package com.example.cleancar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    //variables principales para la autenticacion con firebase
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var authStateListener:FirebaseAuth.AuthStateListener

    //Atenticacion con google firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    // Termina la Autenticacion con google firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declarar accesos directos
        val btingresar: Button =findViewById(R.id.btnIngresar)
        val txtemail: TextView =findViewById(R.id.edtEmail)
        val txtpass: TextView =findViewById(R.id.edtPassword)
        val btnCrear_CuentaNueva:TextView=findViewById(R.id.btnRegistrarCuenta)
        val btnRecordar: TextView = findViewById(R.id.btnOlvidar)


        //inicializamos firebase
        firebaseAuth = Firebase.auth

        //iniciamos con firebase google


        //hacemos un llamado al boton
        btingresar.setOnClickListener()
        {
            try{
            signIn(txtemail.text.toString(),txtpass.text.toString())
            } catch (e: Exception) {
                Toast.makeText(baseContext,"Ingresa tu Email y Contraseña",
                    Toast.LENGTH_SHORT).show()
            }
        }
        //esta funcion es para crear cuenta nueva
        btnCrear_CuentaNueva.setOnClickListener()
        {

            val i = Intent(this,RegistrarseActivity3::class.java)
            startActivity(i)

        }
        //esta funcion es para regenerar contraseña
        btnRecordar.setOnClickListener()
        {
            val i = Intent(this,RecuperarPassActivity::class.java)
            startActivity(i)
        }
    }
    //para que no regrese otra vez a login
    override fun onBackPressed() {
        return
    }//aqui termina

    //funcion para validar correo y password
    private fun signIn(email:String,password:String)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener (this){ task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    //verificacion de cuenta
                    val verifica = user?.isEmailVerified
                    if (verifica==true) {

                        Toast.makeText(
                            baseContext, "Autenticación Exitosa",
                            Toast.LENGTH_SHORT).show()

                       // aqui vamos a ir a la segunda activity
                        val i = Intent(this, MenuPrincipalActivity::class.java)
                        startActivity(i)
                    }
                    else
                    {
                        Toast.makeText(baseContext,"No ha Verificado su correo",
                        Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(baseContext,"Error de Email o Contraseña",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }

    //mostrar datos del usuario en Google

}