package com.goggxi.rxjavagithubuser

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goggxi.rxjavagithubuser.data.RemoteDataSource
import com.goggxi.rxjavagithubuser.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalArgumentException

class UserViewModel(private val remoteDataSource: RemoteDataSource) : ViewModel() {
    sealed class UserUiState {
        data class Success1(val data: User) : UserUiState()
        data class Success2(val data: User) : UserUiState()
        data class Success3(val data: User) : UserUiState()
        data class Failed(val message: String) : UserUiState()
    }

    val state = MutableLiveData<UserUiState>()

    fun getUsers(
        refreshClickStream: Observable<View>,
        closeStream: List<Observable<View>>
    ) {
        val suggestion1 = createSuggestionStream(closeStream[0])
        val suggestion2 = createSuggestionStream(closeStream[1])
        val suggestion3 = createSuggestionStream(closeStream[2])

        refreshClickStream
            .flatMap {
                remoteDataSource.getUser(Math.floor(Math.random() * 500).toInt())
            }
            .map {
                it.take(3)
            }
            .startWith(
                remoteDataSource.getUser(Math.floor(Math.random() * 500).toInt())
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                state.value = UserUiState.Success1(it[0])
                state.value = UserUiState.Success2(it[1])
                state.value = UserUiState.Success3(it[2])
            }

        suggestion1.subscribe {
            state.value = UserUiState.Success1(it)
        }

        suggestion2.subscribe {
            state.value = UserUiState.Success2(it)
        }

        suggestion3.subscribe {
            state.value = UserUiState.Success3(it)
        }

    }

    private fun createSuggestionStream(closeStream: Observable<View>) =
        Observable.combineLatest(
            closeStream,
            remoteDataSource.getUser(Math.floor(Math.random() * 500).toInt()),
            BiFunction { t1, t2 ->
                return@BiFunction t2[Math.floor(Math.random() * t2.size).toInt()]
            }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    companion object {
        fun instance(remoteDataSource: RemoteDataSource) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    return UserViewModel(remoteDataSource) as T
                }
                throw IllegalArgumentException("unknown viewmodel class")
            }
        }
    }
}