package com.example.firebasenotifications.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasenotifications.R
import com.example.firebasenotifications.databinding.ActivityProfileBinding
import com.example.firebasenotifications.repositories.FirebaseAuthRepository.auth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadFirebaseInfo()
        setupEventListeners()
    }

    private fun setupEventListeners() {

    }

    private fun loadFirebaseInfo() {
        binding.lblFullName.text = auth?.currentUser?.displayName
        binding.lblProfileEmail.text = auth?.currentUser?.email
        binding.lblPhone.text = auth?.currentUser?.phoneNumber

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }
}