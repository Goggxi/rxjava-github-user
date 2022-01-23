package com.goggxi.rxjavagithubuser.di

import com.goggxi.rxjavagithubuser.UserViewModel
import com.goggxi.rxjavagithubuser.data.RemoteDataSource
import com.goggxi.rxjavagithubuser.data.RetrofitClient

object ServiceLocator {
    private fun getRetrofitClient() = RetrofitClient()
    private fun getRemoteDataSource() = RemoteDataSource(getRetrofitClient().instance)
    fun getUserViewModel() = UserViewModel.instance(getRemoteDataSource())
}