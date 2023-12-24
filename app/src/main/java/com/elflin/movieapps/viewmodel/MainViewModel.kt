package com.elflin.movieapps.viewmodel

import androidx.lifecycle.MutableLiveData
import com.elflin.movieapps.Repository.MainRepository
import com.elflin.movieapps.helper.ApiResponse
import com.elflin.movieapps.helper.BaseViewModel
import com.elflin.movieapps.helper.CoroutinesErrorHandler
import com.elflin.movieapps.model.UserInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _userInfoResponse = MutableLiveData<ApiResponse<UserInfoResponse>>()
    val userInfoResponse = _userInfoResponse

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userInfoResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getUserInfo()
    }
}