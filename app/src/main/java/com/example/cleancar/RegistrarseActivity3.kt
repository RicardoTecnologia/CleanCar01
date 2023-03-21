package com.example.cleancar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegistrarseActivity3 : AppCompatActivity() {
    //variables principales
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse3)


        //Variables fijas para Crear Cuenta
        val txtnombre_nuevo: TextView =findViewById(R.id.edtNombre)
        val txtemail_nuevo: TextView =findViewById(R.id.edtEmailNuevo)
        val txtpassword1: TextView =findViewById(R.id.edtPasswordNuevo1)
        val txtpassword2: TextView =findViewById(R.id.edtPasswordNuevo2)
        val btnCrear: Button =findViewById(R.id.btnRegistrarCuentaNueva)


        //inicializamos firebase
        firebaseAuth = Firebase.auth

        //boton para crear cuenta
        btnCrear.setOnClickListener()
        {
            var pass1 = txtpassword1.text.toString()
            var pass2 = txtpassword2.text.toString()
            val email = txtemail_nuevo.text.toString()
            val nombre = txtnombre_nuevo.text.toString()

            if(nombre.isEmpty()){
                txtnombre_nuevo.error = "Este campo es obligatorio"
                txtnombre_nuevo.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()){
                txtemail_nuevo.error = "Este campo es obligatorio"
                txtemail_nuevo.requestFocus()
                return@setOnClickListener
            }

            if (pass1.isEmpty() || pass2.isEmpty()){
                Toast.makeText(baseContext,"Error: Debe llenar ambos campos de contraseña",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass1.equals(pass2))
            {
                createAccount(email, pass1,nombre)
            }
            else
            {
                Toast.makeText(baseContext,"Error: el password no coinciden",
                    Toast.LENGTH_SHORT).show()
                txtpassword1.requestFocus()
            }
        } //aqui acaba la funcion del boton crear
    }



    //funcion para el boton crear y me regrese a login
    private fun botonCrear()
    {
        firebaseAuth.signOut()
        val i= Intent(this,MainActivity::class.java)
        startActivity(i)
    }

    //creamos una funcion para crear la cuenta
    private fun createAccount(email:String, password:String,nombre:String)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful)
                {
                    // Crear instancia de la referencia de la base de datos
                    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
                        .reference.child("Usuarios")

            // Obtener la ID del usuario recién creado
                    val userID = firebaseAuth.currentUser?.uid

            // Crear mapa con los datos del usuario a guardar
                    val userMap = HashMap<String, Any>()
                    userMap["nombre"] = nombre
                    userMap["correoElectronico"] = email

            // Guardar los datos del usuario en la base de datos
                    databaseReference.child(userID!!).setValue(userMap)


                    sendEmailVerification()//mando a llamar la funcion de verificacion
                    botonCrear()//me crea la cuenta y me regresa a login principal
                    Toast.makeText(baseContext,"Cuenta Creada Correctamente, se requiere Verificación",
                        Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(baseContext,"Algo Salio mal, Error: "+task.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    //Hacemos la funcion para la verificacion de cuenta
    private fun sendEmailVerification()
    {
        val user = firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this) {task ->
            if (task.isSuccessful)
            {

            }
            else
            {

            }
        }
    }

}