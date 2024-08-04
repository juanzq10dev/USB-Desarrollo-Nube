package com.example.firebasenotifications.repositories

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

object FirebaseAuthRepository {

    var auth: FirebaseAuth? = null

    fun updateProfile(
        fullname: String,
        success: (user: FirebaseUser) -> Unit,
        error: (Throwable) -> Unit
    ) {
        if (auth == null) {
            error(NullPointerException("FirebaseAuth is not initialized"))
            return
        }
        val user = auth?.currentUser
        if (user == null) {
            error(NullPointerException("User is not logged in"))
            return
        }
        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(fullname)
            .build()
        user.updateProfile(profileUpdates).addOnCompleteListener {
            if (it.isSuccessful) {
                success(auth?.currentUser!!)
            } else {
                error(it.exception!!)
            }
        }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        success: (user: FirebaseUser) -> Unit,
        error: (Throwable) -> Unit
    ) {
        auth = FirebaseAuth.getInstance()
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    success(auth?.currentUser!!)
                } else {
                    error(it.exception!!)
                }
            }
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        success: (user: FirebaseUser) -> Unit,
        error: (Throwable) -> Unit
    ) {
        auth = FirebaseAuth.getInstance()
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    success(auth?.currentUser!!)
                } else {
                    error(it.exception!!)
                }
            }
    }

    fun verifyPhone(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        if (auth == null) {
            return
        }
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        success: (user: FirebaseUser) -> Unit,
        error: (Throwable) -> Unit,
        codeInvalid: () -> Unit
    ) {
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("PHONE_AUTH", "signInWithCredential:success")

                    val user = it.result?.user
                    success(user!!)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("PHONE_AUTH", "signInWithCredential:failure", it.exception)
                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        codeInvalid()
                        return@addOnCompleteListener
                    }
                    error(it.exception!!)
                    // Update UI
                }
            }
    }

    fun validateUserHasPhone(
    ): Boolean {
        val user = auth?.currentUser ?: throw NullPointerException("User is not logged in")
        return user.phoneNumber != null
    }

    fun updatePhoneNumber(
        credential: PhoneAuthCredential,
        success: () -> Unit,
        error: (Throwable) -> Unit
    ) {
        val user = auth?.currentUser ?: throw NullPointerException("User is not logged in")
        user.updatePhoneNumber(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success()
                } else {
                    error(it.exception!!)
                }
            }
    }

    fun signInWithGoogle(
        idToken: String,
        success: () -> Unit,
        error: (Throwable) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("GOOGLE_AUTH", "signInWithCredential:success")
                    success()
                } else {
                    Log.w("GOOGLE_AUTH", "signInWithCredential:failure", it.exception)
                    error(it.exception!!)
                }
            }
    }

}