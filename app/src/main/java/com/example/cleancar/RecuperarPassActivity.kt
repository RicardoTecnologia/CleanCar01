package com.example.cleancar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperarPassActivity : AppCompatActivity() {
    //variables principales
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_pass)

        val txtmail : TextView = findViewById(R.id.txtEmailCambio)
        val btnCambiar : TextView = findViewById(R.id.btnCambiar)

        //inicializamos firebase
        firebaseAuth = Firebase.auth

        btnCambiar.setOnClickListener()
        {
            //sendPasswordReset(txtmail.text.toString())
            if(txtmail.text.isNotEmpty()) {
                sendPasswordReset(txtmail.text.toString())
            } else {
                Toast.makeText(this, "Por favor ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Creamos esta funcion para recuperar nuestra contraseña
    private fun sendPasswordReset(email:String)
    {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful)
                {
                    botonRecuperarPass()
                    Toast.makeText(baseContext,"Correo de Cambio Enviado",
                        Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(baseContext,"Error: no se pudo completar el proceso",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }
    //funcion para el boton recuperar y me regrese a login
    private fun botonRecuperarPass()
    {
        firebaseAuth.signOut()
        Toast.makeText(baseContext,"Correo de Cambio Enviado",
            Toast.LENGTH_SHORT).show()
        val i= Intent(this,MainActivity::class.java)
        startActivity(i)
    }
}