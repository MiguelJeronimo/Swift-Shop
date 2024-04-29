package com.miguel.swiftshop.Views.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miguel.swiftshop.data.Repository.UsersRepository

class ViewModelHome: ViewModel() {
    private val repository = UsersRepository()
    private val _list = MutableLiveData<String>()
    val list: MutableLiveData<String> get() = _list
    fun userLists(idUserCollection: String){
        repository.getAllUserList(idUserCollection)
    }
}