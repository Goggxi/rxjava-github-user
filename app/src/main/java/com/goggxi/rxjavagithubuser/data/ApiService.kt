package com.goggxi.rxjavagithubuser.data

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    fun getUsers(@Query("since") since: Int): Observable<List<UserResponse>>
}