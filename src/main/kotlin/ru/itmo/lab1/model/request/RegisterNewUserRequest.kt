package ru.itmo.lab1.model.request

data class RegisterNewUserRequest(
    val login: String,
    val password: String,
    val fullname: String,
    val phoneNumber: String,
    val email: String,
)
