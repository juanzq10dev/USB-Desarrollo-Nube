package com.example.firebasenotifications

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasenotifications.databinding.ActivityMainBinding
import com.example.firebasenotifications.repositories.FirebaseRepository
import com.example.firebasenotifications.ui.activities.RegisterActivity
import com.example.firebasenotifications.ui.viewmodels.MainViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        FirebaseRepository.auth = Firebase.auth
        model = MainViewModel()
        askNotificationPermission()
        requestFirebaseToken()
        setupEventListener()
    }

    private fun setupEventListener() {
        binding.buttonSignIn.setOnClickListener {
            val email = binding.emailText.editText?.text.toString()
            val password = binding.passwordText.editText?.text.toString()
            FirebaseRepository.signInWithEmailAndPassword(email, password,
                success = {
                    Toast.makeText(this,
                        "User ${it.displayName} signed In", Toast.LENGTH_SHORT).show()
                },
                error = {
                    Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                }
                )
        }

        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = "Token: $token"
            Log.d("TOKEN", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission is enabled from launcher", Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_SHORT)
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Here should be a rationale", Toast.LENGTH_SHORT).show()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "Here should be a rationale", Toast.LENGTH_SHORT).show()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}