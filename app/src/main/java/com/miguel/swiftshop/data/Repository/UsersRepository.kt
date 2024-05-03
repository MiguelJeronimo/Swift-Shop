package com.miguel.swiftshop.data.Repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.miguel.swiftshop.models.ListData
import com.miguel.swiftshop.models.User
import com.miguel.swiftshop.models.UserData
import com.miguel.swiftshop.models.UserList
import java.sql.Timestamp

class UsersRepository {
   fun getUser(email: String, password: String, user: MutableLiveData<UserData>){
       val db = Firebase.firestore
       val usersData = UserData(null, null, null,null)
       db.collection("users").whereEqualTo("email", email).whereEqualTo("password", password)
           .get()
           .addOnSuccessListener { result ->
               if (result.size()>0){
                   for (document in result){
                      usersData.idCollection = document.id
                      usersData.name = document.toObject<User>().apellidos
                      usersData.apellidos = document.toObject<User>().apellidos
                      usersData.email = document.toObject<User>().email
                   }
                   user.value = usersData
               } else{
                   user.value = null
               }
           }
           .addOnFailureListener { exception ->
               println("Error${exception}")
               user.value = null
           }
   }

    fun getAllUserList(idUserCollection: String, _list: MutableLiveData<ArrayList<UserList>>){
        val db = Firebase.firestore
        val userList = ArrayList<UserList>()
        db.collection("users").document(idUserCollection).collection("list").get()
            .addOnSuccessListener {
                it.forEach {
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
            .addOnFailureListener { exception ->
                _list.value = null
                println("Error${exception}")
            }
    }
}