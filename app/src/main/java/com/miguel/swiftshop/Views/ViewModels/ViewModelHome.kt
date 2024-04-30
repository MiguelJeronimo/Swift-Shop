package com.miguel.swiftshop.Views.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miguel.swiftshop.data.Repository.UsersRepository
import com.miguel.swiftshop.models.UserList

class ViewModelHome: ViewModel() {
    private val repository = UsersRepository()
    private val _list = MutableLiveData<ArrayList<UserList>>()
    val list: MutableLiveData<ArrayList<UserList>> get() = _list
    fun userLists(idUserCollection: String){
        repository.getAllUserList(idUserCollection,_list)
    }
}