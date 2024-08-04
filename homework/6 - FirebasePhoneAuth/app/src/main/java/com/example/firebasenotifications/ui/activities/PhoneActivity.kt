package com.example.firebasenotifications.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasenotifications.R
import com.example.firebasenotifications.databinding.ActivityPhoneBinding
import com.example.firebasenotifications.repositories.FirebaseAuthRepository
import com.example.firebasenotifications.repositories.FirebaseAuthRepository.auth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneActivity : AppCompatActivity() {
    private var verificationId: String? = null
    lateinit var binding: ActivityPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPhoneBinding.inflate(layoutInflater)
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
            btnVerifyPhone.setOnClickListener {
                doPhoneVerification()
            }
            btnVerifyCode.setOnClickListener {
                doCodeVerification()
            }
        }
    }

    private fun doCodeVerification() {
        if (verificationId == null) return
        Log.d("PHONE_AUTH", "doCodeVerification")
        val code = binding.txtVerificationCode.editText?.text.toString()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        auth?.currentUser?.linkWithCredential(credential)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("PHONE_AUTH", "linkWithCredential:success")
                    val intent = DashboardActivity.newIntent(this)
                    startActivity(intent)
                } else {
                    Log.d("PHONE_AUTH", "linkWithCredential:failure")
                }
            }
    }

    private fun doPhoneVerification() {
        val phoneNumber = binding.txtPhone.editText?.text.toString()
        val phoneCodeIndex = binding.spCountryPhone.selectedItemPosition
        val phoneCode = resources.getStringArray(R.array.phone_codes)[phoneCodeIndex]
        val phone = "$phoneCode$phoneNumber"
        FirebaseAuthRepository.verifyPhone(
            phone, this, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    verificationCompleted(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    verificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    codeSent(verificationId, token)
                }
            })
    }

    private fun codeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        this.verificationId = verificationId
        Log.d("PHONE_AUTH", "onCodeSent:$verificationId")
        binding.btnVerifyCode.visibility = View.VISIBLE
        binding.txtVerificationCode.visibility = View.VISIBLE
        binding.btnVerifyPhone.visibility = View.GONE
        binding.txtPhone.visibility = View.GONE
        binding.spCountryPhone.visibility = View.GONE
    }

    private fun verificationFailed(e: FirebaseException) {
        e.printStackTrace()
    }

    private fun verificationCompleted(credential: PhoneAuthCredential) {
        Log.d("PHONE_AUTH", "onVerificationCompleted:$credential")
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PhoneActivity::class.java)
        }
    }
}