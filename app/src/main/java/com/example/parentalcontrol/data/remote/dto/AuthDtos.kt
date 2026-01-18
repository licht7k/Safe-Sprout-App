package com.example.parentalcontrol.data.remote.dto

data class RegisterParentRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginParentRequest(
    val email: String,
    val password: String
)

data class UserDto(
    val id: Long,
    val name: String,
    val email: String
)

data class AuthResponse(
    val message: String,
    val user: UserDto,
    val token: String
)

data class MeResponse(
    val user: UserDto
)
