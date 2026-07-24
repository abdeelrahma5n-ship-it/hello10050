package com.example.data.model

data class User(
    val id: Long = 0,
    val name: String,
    val username: String = "",
    val profilePictureUri: String = "",
    val phone: String,
    val email: String = "",
    val loginMethod: String, // e.g. "رقم الهاتف", "Google (Gmail)", "Facebook", "TikTok", "Instagram", "Apple ID"
    val isBlocked: Boolean = false,
    val cityLocation: String = "أولاد صقر",
    val registeredAt: Long = System.currentTimeMillis(),
    val isNameChanged: Boolean = false,
    val isUsernameChanged: Boolean = false
)
