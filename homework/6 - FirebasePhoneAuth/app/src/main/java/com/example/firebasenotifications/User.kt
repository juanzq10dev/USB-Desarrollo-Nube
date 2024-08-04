package com.example.firebasenotifications

data class User(
    val name: String = "",
    val lastName: String = "",
    val age: String = "",
    val email: String = "",
    var phoneNumbers: List<Contact> = listOf()
)