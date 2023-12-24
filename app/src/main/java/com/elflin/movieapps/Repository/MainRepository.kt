package com.elflin.movieapps.Repository

import com.elflin.movieapps.data.MainApiService
import com.elflin.movieapps.helper.ApiResponse.Loading.apiRequestFlow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: MainApiService,
) {
    fun getUserInfo() = apiRequestFlow {
        mainApiService.getUserInfo()
    }
}