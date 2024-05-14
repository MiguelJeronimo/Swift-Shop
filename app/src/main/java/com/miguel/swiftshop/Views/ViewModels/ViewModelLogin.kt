package com.miguel.swiftshop.Views.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miguel.swiftshop.data.Repository.UsersRepository
import com.miguel.swiftshop.models.UserData
import com.miguel.swiftshop.models.UserDataInsertModel

class ViewModelLogin(): ViewModel() {
    private val repository = UsersRepository()
    private val _login = MutableLiveData<Int>()
    val login: MutableLiveData<Int> get() = _login

    private val _user = MutableLiveData<UserData>()
    val user: MutableLiveData<UserData>get() = _user

    private val _userData = MutableLiveData<Boolean>()
    val userData: MutableLiveData<Boolean> get() = _userData
    init {
        _login.value = 0
    }

    fun stateLogin(state:Int){
        _login.value = state
    }

    fun user(email: String, password: String, privateKey: String?){
        repository.Autentication(email, password, user, privateKey)
    }

    fun userRegistry(userData: UserDataInsertModel){
        repository.insertUser(_userData, userData)
    }

}