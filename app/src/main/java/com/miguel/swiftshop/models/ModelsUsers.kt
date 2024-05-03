package com.miguel.swiftshop.models


data class UserData(
    var name: String?,
    var apellidos: String?,
    var email: String?,
    var password: String?,
    var idCollection:String?
)
data class UserList(
    var idDocument: String?,
    var name: String?,
    var date:  com.google.firebase.Timestamp?
)

//models to firebase data
data class ListData(
    var name: String?=null,
    var date: com.google.firebase.Timestamp?=null
)

data class User(
    val apellidos: String? = null,
    val email: String?=  null,
    val name: String? = null,
    val password: String?= null
)

data class UserDataInsertModel(
    val idColecction: String?=null,
    val name: String?=null,
    val apellidos: String?=null,
    val email: String?=null,
    val password: String?=null
)