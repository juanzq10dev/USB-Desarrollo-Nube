package com.example.firebasenotifications.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseRepository {
    var auth: FirebaseAuth? = null

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