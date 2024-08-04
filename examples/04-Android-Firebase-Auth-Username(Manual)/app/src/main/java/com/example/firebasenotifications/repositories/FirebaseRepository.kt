package com.example.firebasenotifications.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

object FirebaseRepository {
    var auth: FirebaseAuth? = null

    fun updateProfile(
        name: String,
        success: (user: FirebaseUser) -> Unit,
        error: () -> Unit
    ): Boolean {
        if (auth == null) {
           error(NullPointerException("Firebase auth not initialized"))
        }

        val user = auth!!.currentUser ?: error(NullPointerException("User not logged in"))

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success(auth?.currentUser!!)
                } else {
                    error(it.exception!!)
                }
            }
        return true
    }

    fun signInWithEmailAndPassword(email: String, password: String,
                                   success: (user: FirebaseUser) -> Unit,
                                   error: (Throwable) -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                success(auth?.currentUser!!)
            } else {
                error(it.exception!!)
            }
        }
    }

    fun registerWithEmailAndPassword(email: String, password: String,
                                   success: (user: FirebaseUser) -> Unit,
                                   error: (Throwable) -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                success(auth?.currentUser!!)
            } else {
                error(it.exception!!)
            }
        }
    }

}