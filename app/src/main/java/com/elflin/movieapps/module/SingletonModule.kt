package com.elflin.movieapps.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.elflin.movieapps.authenticator.AuthAuthenticator
import com.elflin.movieapps.data.AuthApiService
import com.elflin.movieapps.data.MainApiService
import com.elflin.movieapps.data.TokenManager
import com.elflin.movieapps.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.prefs.Preferences
import javax.inject.Singleton

class SingletonModule {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

    @Module
    @InstallIn(SingletonComponent::class)
    class SingletonModule {

        @Singleton
        @Provides
        fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

        @Singleton
        @Provides
        fun provideOkHttpClient(
            authInterceptor: AuthInterceptor,
            authAuthenticator: AuthAuthenticator,
        ): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .authenticator(authAuthenticator)
                .build()
        }

        @Singleton
        @Provides
        fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
            AuthInterceptor(tokenManager)

        @Singleton
        @Provides
        fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
            AuthAuthenticator(tokenManager)

        @Singleton
        @Provides
        fun provideRetrofitBuilder(): Retrofit.Builder =
            Retrofit.Builder()
                .baseUrl("https://jwt-test-api.onrender.com/api/")
                .addConverterFactory(GsonConverterFactory.create())

        @Singleton
        @Provides
        fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthApiService =
            retrofit
                .build()
                .create(AuthApiService::class.java)

        @Singleton
        @Provides
        fun provideMainAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): MainApiService =
            retrofit
                .client(okHttpClient)
                .build()
                .create(MainApiService::class.java)
    }
}