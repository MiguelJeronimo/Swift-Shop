package com.miguel.swiftshop.models

import java.sql.Timestamp

data class UserData(
    var name: String?,
    var apellidos: String?,
    var email: String?,
    var idCollection:String?
)

data class UserList(
    val idDocument: String?,
    val name: String?,
    val date: Timestamp?
)

data class User(
    val apellidos: String? = null,
    val email: String?=  null,
    val name: String? = null,
    val password: String?= null
)