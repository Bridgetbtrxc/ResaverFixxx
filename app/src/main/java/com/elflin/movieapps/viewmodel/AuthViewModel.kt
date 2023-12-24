package com.elflin.movieapps.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elflin.movieapps.Repository.AuthRepository
import com.elflin.movieapps.data.Auth
import com.elflin.movieapps.helper.ApiResponse
import com.elflin.movieapps.helper.CoroutinesErrorHandler
import com.elflin.movieapps.model.LoginRequest
import com.elflin.movieapps.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AuthPreference", Context.MODE_PRIVATE)

    private val _register: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _login: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _loginResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val loginResponse = _loginResponse



    val register: LiveData<String>
        get() = _register

    val login: LiveData<String>
        get() = _login

    fun registerUser(user_username: String, user_email: String, user_password: String) = viewModelScope.launch {
        try {
            val response = repo.registerUser(user_username, user_email, user_password)
            if (response.isSuccessful) {
                _register.value = "User successfully registered"
                Log.d("Register User", response.body()?.toString() ?: "Successful but no response body")
            } else {
                _register.value = response.message()
                Log.e("Register User", response.message())
            }
        } catch (e: Exception) {
            _register.value = e.message
            Log.e("Register User Exception", e.message ?: "Unknown Exception")
        }
    }

    fun loginUser(user_email: String, user_password: String) = viewModelScope.launch {
        try {
            val response = repo.loginUser(user_email, user_password)
            if (response.isSuccessful && response.body() != null) {
//                val token = response.body()? // Replace with the actual token field
                if (token != null) {
                    val editor = sharedPreferences.edit()
//                    editor.putString("token")
                    editor.apply()

                    _login.value = "User successfully logged in"
                    Log.d("Login User", "Token stored: $token")
                } else {
                    _login.value = "Login successful but token missing"
                    Log.e("Login User Error", "Token missing in response")
                }
            } else {
                _login.value = "Login error: " + response.errorBody()?.string()
                Log.e("Login User Error", response.errorBody()?.string() ?: "Unknown Error")
            }
        } catch (e: Exception) {
            _login.value = "Login exception: " + e.message
            Log.e("Login User Exception", e.message ?: "Unknown Exception")
        }
    }

    fun logoutUser() = viewModelScope.launch {
        val token = sharedPreferences.getString("token", null)
        if (!token.isNullOrEmpty()) {
            try {
                val response = repo.logoutUser(token)
                if (response.isSuccessful) {
                    sharedPreferences.edit().remove("token").apply()
                    _register.value = "User successfully logged out"
                    Log.d("Logout User", "Token removed: $token")
                } else {
                    _register.value = "Logout error: " + response.message()
                    Log.e("Logout User Error", response.message())
                }
            } catch (e: Exception) {
                _register.value = "Logout exception: " + e.message
                Log.e("Logout User Exception", e.message ?: "Unknown Exception")
            }
        } else {
            _register.value = "No token found, user not logged in"
            Log.e("Logout User", "No token found")
        }
    }
}
