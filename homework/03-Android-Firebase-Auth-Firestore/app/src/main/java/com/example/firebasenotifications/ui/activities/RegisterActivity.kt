package com.example.firebasenotifications.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasenotifications.R
import com.example.firebasenotifications.databinding.ActivityRegisterBinding
import com.example.firebasenotifications.repositories.FirebaseRepository
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

class RegisterActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupEventListeners()
    }

    private fun createUser() {
        val name = binding.userNameText.editText?.text.toString()
        val lastName = binding.lastNameText.editText?.text.toString()
        val age = binding.ageText.editText?.text.toString()
        val email = binding.emailText.editText?.text.toString()

        val user = hashMapOf(
            "name" to name,
            "lastName" to lastName,
            "age" to age,
            "email" to email
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    private fun setupEventListeners() {
        binding.apply {
            button3.setOnClickListener {
                registerUser()
            }
        }
    }

    private fun updateFullUserName() {
        val fullName = binding.userNameText.editText?.text.toString()
        FirebaseRepository.updateProfile(
            fullName,
            success = {
                Toast.makeText(
                    this@RegisterActivity,
                    "User ${it.email} registered",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            },
            error = {
                Toast.makeText(this@RegisterActivity,
                    "Error",
                    Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun registerUser() {
        val email = binding.emailText.editText?.text.toString()
        val password = binding.passwordText.editText?.text.toString()

        FirebaseRepository.registerWithEmailAndPassword(
            email, password, success = {
                createUser()
                finish()
            }, error = {
                it.printStackTrace()
                Toast.makeText(this@RegisterActivity,
                    "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}