package com.miguel.swiftshop.data.Repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.miguel.swiftshop.models.ListData
import com.miguel.swiftshop.models.UserList
import java.util.UUID

class RepositoryShoppingList {
    private val db = Firebase.firestore
    fun getAllUserList(idUserCollection: String, _list: MutableLiveData<ArrayList<UserList>>){
//        db.collection("users").document(idUserCollection).collection("list").get()
//            .addOnSuccessListener {
//                it.forEach {
//                    val documentsData = it.toObject<ListData>()
//                    userList.add(
//                        UserList(
//                            it.id,
//                            documentsData.name,
//                            documentsData.date
//                        )
//                    )
//                }
//                _list.value = userList
//            }
//            .addOnFailureListener { exception ->
//                _list.value = null
//                println("Error${exception}")
//            }

        db.collection("users").document(idUserCollection).collection("list")
           .orderBy(FieldPath.of("date"),Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null){
                    println("Ocurrio un error: $error")
                    _list.value = null
                }
                val userList = ArrayList<UserList>()
                value?.forEach {
                    val documentsData = it.toObject<ListData>()
                    userList.add(
                        UserList(
                            it.id,
                            documentsData.name,
                            documentsData.date
                        )
                    )
                }
                _list.value = userList
            }
    }

    fun insert(
        listName: String,
        date: Timestamp,
        _insertList: MutableLiveData<Boolean>,
        idCollection: String?
    ) {
        var uuii = UUID.randomUUID()
        val list = hashMapOf(
            "name" to listName,
            "date" to date,
        )
        db.collection("users").document(idCollection!!).collection("list")
            .document(uuii.toString()).set(list).addOnSuccessListener {
                println("Inserto? $it")
                _insertList.value = true
            }.addOnFailureListener {
                _insertList.value = false
            }
    }
    fun delete(idDocumentUser: String?, idDocumentList: String?, _delete: MutableLiveData<Boolean>){
        db.collection("users").document(idDocumentUser!!).collection("list")
            .document(idDocumentList.toString()).delete()
            .addOnSuccessListener {_delete.value = true}.addOnFailureListener { _delete.value = false }
    }
}