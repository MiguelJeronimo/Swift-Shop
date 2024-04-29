package com.miguel.swiftshop.Views.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miguel.swiftshop.data.Repository.UsersRepository
import com.miguel.swiftshop.models.UserData

class ViewModelLogin(): ViewModel() {
    private val repository = UsersRepository()
    private val _login = MutableLiveData<Int>()
    val login: MutableLiveData<Int> get() = _login
    private val _user = MutableLiveData<UserData>()
    val user: MutableLiveData<UserData>get() = _user
    init {
        _login.value = 0
    }

    fun stateLogin(state:Int){
        _login.value = state
    }

    fun user(email: String, password: String){
        repository.getUser(email, password, user)
    }

}