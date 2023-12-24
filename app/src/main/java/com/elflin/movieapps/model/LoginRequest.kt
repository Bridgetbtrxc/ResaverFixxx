package com.elflin.movieapps.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email_adress")
    val email: String,
    val password: String
)
