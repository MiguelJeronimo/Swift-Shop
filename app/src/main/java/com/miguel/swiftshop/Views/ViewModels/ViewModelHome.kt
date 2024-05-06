package com.miguel.swiftshop.Views.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.miguel.swiftshop.data.Repository.RepositoryShoppingList
import com.miguel.swiftshop.models.UserList

class ViewModelHome: ViewModel() {
    private val repository = RepositoryShoppingList()
    private val _list = MutableLiveData<ArrayList<UserList>>()
    val list: MutableLiveData<ArrayList<UserList>> get() = _list
    private val _insertList = MutableLiveData<Boolean>()
    val insertList: MutableLiveData<Boolean> get() = _insertList
    fun userLists(idUserCollection: String){
        repository.getAllUserList(idUserCollection,_list)
    }
    fun insert(listName: String, date: Timestamp, idCollection: String?){
        repository.insert(listName, date, _insertList, idCollection)
    }
}