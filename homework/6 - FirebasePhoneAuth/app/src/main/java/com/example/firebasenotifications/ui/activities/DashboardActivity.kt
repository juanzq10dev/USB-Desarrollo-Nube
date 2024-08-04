package com.example.firebasenotifications.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasenotifications.Contact
import com.example.firebasenotifications.R
import com.example.firebasenotifications.User
import com.example.firebasenotifications.databinding.ActivityDashboardBinding
import com.example.firebasenotifications.repositories.FirebaseAuthRepository.auth
import com.example.firebasenotifications.repositories.GoogleAuthRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupEventlisteners()
        loadUserInformation()
    }

    private fun loadUserInformation() {
        binding.lblWelcomeText.text =
            getString(R.string.welcome_user, auth?.currentUser?.displayName)
    }

    private fun addContact() {
        val name = binding.contactName.editText?.text ?: ""
        val phoneNumber = binding.contactNumber.editText?.text ?: ""
        val email = auth?.currentUser?.email ?: ""
        val contact = Contact(name.toString(), phoneNumber.toString())

        db.collection("users").document(email)
            .update( "phoneNumbers", FieldValue.arrayUnion(contact)
            )
    }

    private fun setupEventlisteners() {
        binding.apply {
            btnLogout.setOnClickListener {
                auth?.signOut()
                //sign out with google
                GoogleAuthRepository.signOutFromGoogle(this@DashboardActivity)
                finish()
            }
            btnGoToProfile.setOnClickListener {
                val intent = ProfileActivity.newIntent(this@DashboardActivity)
                startActivity(intent)
            }
            addContactButton.setOnClickListener {
                addContact()
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }
}