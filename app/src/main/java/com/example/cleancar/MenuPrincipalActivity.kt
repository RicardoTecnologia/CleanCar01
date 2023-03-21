package com.example.cleancar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.cleancar.databinding.ActivityMenuPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MenuPrincipalActivity : AppCompatActivity() {
    //variables principales para la autenticacion con firebase
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuPrincipalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenuPrincipal.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu_principal)

        // Obtener la referencia de los TextView de la cabecera del menú hamburguesa
        val usuarioTextView = findViewById<TextView>(R.id.usuario)
        val correoTextView = findViewById<TextView>(R.id.tvCorreo)

    // Obtener la ID del usuario actual
        val userID = firebaseAuth.currentUser?.uid

    // Obtener la referencia a la información del usuario en la base de datos de Firebase
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
            .reference.child("Usuarios").child(userID!!)

    // Escuchar cambios en la información del usuario en la base de datos de Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Obtener los datos del usuario
                val nombre = dataSnapshot.child("nombre").value.toString()
                val correoElectronico = dataSnapshot.child("correoElectronico").value.toString()

                // Mostrar los datos del usuario en los TextView correspondientes
                usuarioTextView.text = nombre
                correoTextView.text = correoElectronico
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de la consulta de Firebase
                Toast.makeText(baseContext, "Error al obtener los datos del usuario: " + error.message,
                    Toast.LENGTH_SHORT).show()
            }
        })

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //aqui se agrega los id de nav_cofig
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_notificacion,
                R.id.nav_configuracion
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //inicializamos firebase
        firebaseAuth = Firebase.auth

    }

    //de usuario



    //funcion de Cerrar Sesion
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_Salir ->{
                cerrarSesion()
            }
        }
        return super.onOptionsItemSelected(item)
    } //termina la funcion Cerrar Sesion

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu_principal)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //para que no regrese otra vez a login
    override fun onBackPressed() {
        return
    }//termina

    //cerrar sesion
    private fun cerrarSesion()
    {
        try{
        firebaseAuth.signOut()
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }catch (e: Exception) {
            Toast.makeText(baseContext, "Sesión Cerrada Correctamente", Toast.LENGTH_SHORT).show()
        }

    }//Termina la funcion cerrar sesion
}