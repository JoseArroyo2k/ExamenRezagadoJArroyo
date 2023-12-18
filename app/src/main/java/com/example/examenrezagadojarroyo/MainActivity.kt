package com.example.examenrezagadojarroyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        val txtCorreo = findViewById<EditText>(R.id.txtUsuario)
        val txtContraseña = findViewById<EditText>(R.id.txtClave)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistro = findViewById<Button>(R.id.btnRegister)




        btnLogin.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()
            val contraseña = txtContraseña.text.toString().trim()

            if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Si la autenticación es exitosa, redirige a la siguiente actividad (ProductsActivity en este caso)
                            val intent = Intent(this@MainActivity, APIActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            // Si hay un error en la autenticación, muestra un mensaje al usuario
                            Toast.makeText(this@MainActivity, "Error en la autenticación: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Si los campos están vacíos, muestra un mensaje
                Toast.makeText(this@MainActivity, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }


        }

        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}