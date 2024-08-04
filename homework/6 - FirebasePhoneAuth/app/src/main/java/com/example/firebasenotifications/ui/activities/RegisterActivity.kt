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
import com.example.firebasenotifications.repositories.FirebaseAuthRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private val db = Firebase.firestore

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

    private fun setupEventListeners() {
        binding.apply {
            btnRegisterUser.setOnClickListener { registerUser() }
        }
    }

    private fun createUser() {
        val name = binding.txtFullNameRegister.editText?.text.toString()
        val lastName = binding.textLastNameRegister.editText?.text.toString()
        val age = binding.textAgeRegister.editText?.text.toString()
        val email = binding.txtEmailRegister.editText?.text.toString()

        val user = hashMapOf(
            "name" to name,
            "lastName" to lastName,
            "age" to age,
            "email" to email
        )

        db.collection("users")
            .document(email)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "User ${name} ${lastName} added")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    private fun registerUser() {
        val email = binding.txtEmailRegister.editText?.text.toString()
        val password = binding.txtPasswordRegister.editText?.text.toString()
        FirebaseAuthRepository.registerWithEmailAndPassword(
            email,
            password,
            success = {
                createUser()
            },
            error = {
                it.printStackTrace()
                Toast.makeText(
                    this@RegisterActivity,
                    "Error: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun updateUserFullname() {
        val fullname = binding.txtFullNameRegister.editText?.text.toString()
        FirebaseAuthRepository.updateProfile(
            fullname,
            success = {
                Toast.makeText(
                    this@RegisterActivity,
                    "User ${it.email} registered",
                    Toast.LENGTH_SHORT
                ).show()
                goToPhoneActivity()
            },
            error = {
                it.printStackTrace()
                Toast.makeText(
                    this@RegisterActivity,
                    "Error: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun goToPhoneActivity() {
        val intent = PhoneActivity.newIntent(this)
        startActivity(intent)
    }
}