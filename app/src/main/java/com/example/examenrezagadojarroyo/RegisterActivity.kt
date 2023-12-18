package com.example.examenrezagadojarroyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examenrezagadojarroyo.model.UsuarioModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtNombre: EditText = findViewById(R.id.txtNombre)
        val txtCorreo: EditText = findViewById(R.id.txtCorreo)
        val txtClave: EditText = findViewById(R.id.txtContraseña)
        val txtDNI: EditText = findViewById(R.id.txtDni)
        val txtdireccion: EditText = findViewById(R.id.txtDireccion)
        val txtfecha: EditText = findViewById(R.id.txtFechaNac)
        val btnRegistro: Button = findViewById(R.id.btnRegistrar)


        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val collectionRef = db.collection("Usuario")  // Asegúrate de cambiar "Usuarios" al nombre correcto de tu colección

        btnRegistro.setOnClickListener {
            val correo = txtCorreo.text.toString()
            val clave = txtClave.text.toString()
            val nombreCompleto = txtNombre.text.toString()
            val dni = txtDNI.text.toString()
            val direccion = txtdireccion.text.toString()
            val fecha = txtfecha.text.toString()

            auth.createUserWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Se registró en Firebase Auth y deberá registrarse en Firestore
                        val user = auth.currentUser
                        val uid = user?.uid

                        // Utilizamos el modelo para representar los datos del usuario
                        val usuarioModel = UsuarioModel(fecha = fecha, contraseña = clave, correo = correo, nombre = nombreCompleto, direccion = direccion, dni = dni)

                        // Agregamos el modelo a Firestore
                        collectionRef.document(uid ?: "").set(usuarioModel)
                            .addOnCompleteListener {
                                // Manejar la operación completa si es necesario
                                Snackbar
                                    .make(
                                        findViewById(android.R.id.content),
                                        "Registro exitoso del usuario",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                startActivity(Intent(this, MainActivity::class.java))
                            }.addOnFailureListener { error ->
                                // Manejo de error en caso de fallo al agregar datos a Firestore
                                Snackbar
                                    .make(
                                        findViewById(android.R.id.content),
                                        "Error al registrar en Firestore: ${error.message}",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                            }
                    } else {
                        // Manejo de error al registrar en Firebase Auth
                        Snackbar
                            .make(
                                findViewById(android.R.id.content),
                                "Error al registrar en Firebase Auth: ${task.exception?.message}",
                                Snackbar.LENGTH_LONG
                            ).show()
                    }
                }

        }

    }
}