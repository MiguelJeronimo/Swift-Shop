package com.miguel.swiftshop.data.Repository

import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.miguel.swiftshop.models.ListData
import com.miguel.swiftshop.models.User
import com.miguel.swiftshop.models.UserData
import com.miguel.swiftshop.models.UserDataInsertModel
import com.miguel.swiftshop.models.UserList
import com.miguel.swiftshop.utils.CodeEncode

class UsersRepository {
    val db = Firebase.firestore
   fun Autentication(
       email: String,
       password: String,
       user: MutableLiveData<UserData>,
       privateKey: String?
   ){
       val usersData = UserData(null, null, null,null,null)
       val codeEncode = CodeEncode()
       db.collection("users").whereEqualTo("email", email)
           .get()
           .addOnSuccessListener { result ->
               if (result.size()>0){
                   for (document in result){
                      usersData.idCollection = document.id
                      usersData.name = document.toObject<User>().apellidos
                      usersData.apellidos = document.toObject<User>().apellidos
                      usersData.email = document.toObject<User>().email
                      usersData.password = document.toObject<User>().password
                   }
                   val data: String = usersData.password.toString()
                   val decryptPasswordUser = codeEncode.decrypData(privateKey, Base64.decode(data, Base64.DEFAULT))
                   if (password == decryptPasswordUser){
                       println("son iguales")
                       user.value = usersData
                   } else{
                       println("no son iguales")
                       user.value = null
                   }
               } else{
                   user.value = null
               }
           }
           .addOnFailureListener { exception ->
               println("Error${exception}")
               user.value = null
           }
   }
    fun insertUser(_userData: MutableLiveData<Boolean>, userData: UserDataInsertModel) {
        val user = hashMapOf(
            "apellidos" to userData.apellidos,
            "email" to userData.email,
            "name" to userData.name,
            "password" to userData.password
        )
        val db = Firebase.firestore
        db.collection("users").document(userData.idColecction.toString()).set(user).addOnSuccessListener {
            _userData.value = true
        }.addOnFailureListener {
            println("Error: ${it.message}")
            println("Error: $it")
            _userData.value = false
        }
    }


    fun getAllUserList(idUserCollection: String, _list: MutableLiveData<ArrayList<UserList>>){
        println("IDCOLECFTION: $idUserCollection")
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

//        db.collection("users").document(idUserCollection).collection("list")
//            .addSnapshotListener { value, error ->
//                if (error != null){
//                    println("Ocurrio un error: $error")
//                    _list.value = null
//                }
//                val userList = ArrayList<UserList>()
//                value?.forEach {
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
//                println("MODIFICACIONES: $userList")
//            }
    }
}