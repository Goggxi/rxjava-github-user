package com.goggxi.rxjavagithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.goggxi.rxjavagithubuser.di.ServiceLocator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import rxjavagithubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Observer<UserViewModel.UserUiState> {

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, ServiceLocator.getUserViewModel()).get(UserViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this, this)

        val refreshClickStream = Observable.create(object : ObservableOnSubscribe<View> {
            override fun subscribe(emitter: ObservableEmitter<View>) {
                binding.btnRefresh.setOnClickListener {
                    emitter.onNext(it)
                }
            }
        })

        // Lambda Expression
        val randomClickStream1 = Observable.create(ObservableOnSubscribe<View> { emitter ->
            binding.recommend1.btnRandom.setOnClickListener {
                emitter.onNext(it)
            }
        })

        val randomClickStream2 = Observable.create(ObservableOnSubscribe<View> { emitter ->
            binding.recommend2.btnRandom.setOnClickListener {
                emitter.onNext(it)
            }
        })

        val randomClickStream3 = Observable.create(ObservableOnSubscribe<View> { emitter ->
            binding.recommend3.btnRandom.setOnClickListener {
                emitter.onNext(it)
            }
        })

        viewModel.getUsers(
            refreshClickStream,
            listOf(randomClickStream1, randomClickStream2, randomClickStream3)
        )
    }

    override fun onChanged(t: UserViewModel.UserUiState?) {
        when (t) {
            is UserViewModel.UserUiState.Failed -> TODO()
            is UserViewModel.UserUiState.Success1 -> {
                with(binding.recommend1) {
                    tvUsername.text = t.data.username
                    Glide.with(this@MainActivity)
                        .load(t.data.avatarUrl)
                        .into(avatarUser)
                }
            }
            is UserViewModel.UserUiState.Success2 -> {
                with(binding.recommend2) {
                    tvUsername.text = t.data.username
                    Glide.with(this@MainActivity)
                        .load(t.data.avatarUrl)
                        .into(avatarUser)
                }
            }
            is UserViewModel.UserUiState.Success3 -> {
                with(binding.recommend3) {
                    tvUsername.text = t.data.username
                    Glide.with(this@MainActivity)
                        .load(t.data.avatarUrl)
                        .into(avatarUser)
                }
            }
        }
    }
}