package com.goggxi.rxjavagithubuser.data

import com.goggxi.rxjavagithubuser.model.User
import io.reactivex.rxjava3.core.Observable

class RemoteDataSource(private val apiService: ApiService) {
    fun getUser(since: Int): Observable<List<User>> =
        apiService.getUsers(since).map { users ->
            users.map {
                User(
                    it.login,
                    it.id,
                    it.avatarUrl
                )
            }
        }
}