package com.elflin.movieapps.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import com.elflin.movieapps.data.TokenManager
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getTokenSynchronously() // Synchronously get the token

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
