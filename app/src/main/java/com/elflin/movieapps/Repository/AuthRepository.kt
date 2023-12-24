package com.elflin.movieapps.Repository

import com.elflin.movieapps.data.Auth
import com.elflin.movieapps.data.AuthApiService
import com.elflin.movieapps.data.EndPointAPI
import com.elflin.movieapps.helper.ApiResponse.Loading.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api : EndPointAPI,
    private val authApiService: AuthApiService
) {
    fun login(auth: Auth) = apiRequestFlow {
        authApiService.login(auth)
    }

    suspend fun registerUser(
        user_username : String,
        user_email:String,
        user_password: String)
    = api.register(user_username,user_email,user_password)

    suspend fun loginUser(

        user_email: String,
        user_password:String)
    =api.login(user_email,user_password)

    suspend fun LogoutUser(user_token: String) = api.logout(user_token)


//    suspend fun getProfile(
//        user_id : String)
//    =api.getLoginInfo(user_id)
//    )

}