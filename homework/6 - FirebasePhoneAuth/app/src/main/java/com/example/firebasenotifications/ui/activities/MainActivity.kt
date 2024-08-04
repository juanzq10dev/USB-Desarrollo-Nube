package com.example.firebasenotifications.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasenotifications.R
import com.example.firebasenotifications.databinding.ActivityMainBinding
import com.example.firebasenotifications.repositories.FirebaseAuthRepository
import com.example.firebasenotifications.repositories.FirebaseAuthRepository.auth
import com.example.firebasenotifications.ui.viewmodels.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("GOOGLE_SIGNIN", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("GOOGLE_SIGNIN", "Google sign in failed", e)
                }
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        FirebaseAuthRepository.signInWithGoogle(idToken,
            success = {
                onFirebaseAuthSuccess(auth?.currentUser!!)
            },
            error = {
                it.printStackTrace()
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            })
    }

    private val model: MainViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission is enabled from launcher", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_SHORT).show()
        }
    }


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
        askNotificationPermission()
        requestFirebaseToken()
        setupEventListeners()
        auth = Firebase.auth
    }

    private fun setupEventListeners() {
        binding.btnSignIn.setOnClickListener {
            doSignIn()
        }
        binding.btnRegister.setOnClickListener {
            doRegister()
        }
        binding.btnSignInGoogle.setOnClickListener {
            doSignInGoogle()
        }
    }

    private fun doSignInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)

    }

    private fun doRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun doSignIn() {
        val email = binding.txtEmailLogin.editText?.text.toString()
        val password = binding.txtPasswordLogin.editText?.text.toString()
        FirebaseAuthRepository.signInWithEmailAndPassword(email, password,
            success = {
                onFirebaseAuthSuccess(it)
            },
            error = {
                it.printStackTrace()
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            })
    }

    private fun onFirebaseAuthSuccess(user: FirebaseUser) {
        Toast.makeText(this, "User ${user.email} signed in", Toast.LENGTH_SHORT).show()
        Log.d("USER displayname", user.displayName ?: "No name")
        if (!FirebaseAuthRepository.validateUserHasPhone()) {
            val intent = PhoneActivity.newIntent(this)
            startActivity(intent)
        } else {
            val intent = DashboardActivity.newIntent(this)
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


    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Permission is enabled", Toast.LENGTH_SHORT).show()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "Here should be a rationale", Toast.LENGTH_SHORT).show()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}