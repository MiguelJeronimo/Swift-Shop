package com.miguel.swiftshop.utils

class ValidatePassword(private val password: String) {
    fun isValid(): Boolean {
        println(password)
        return password.length >= 8 && hasUppercase() && hasLowercase() && hasDigit() && hasAllowedSpecialChars() && !hasMoreThan2IdenticalChars()
    }

    private fun hasUppercase(): Boolean {
        val regex = Regex("[A-Z]")
        return regex.containsMatchIn(password)
    }

    private fun hasLowercase(): Boolean {
        val regex = Regex("[a-z]")
        return regex.containsMatchIn(password)
    }

    private fun hasDigit(): Boolean {
        val regex = Regex("[0-9]")
        return regex.containsMatchIn(password)
    }

    private fun hasAllowedSpecialChars(): Boolean {
        val regex = Regex("[= / _ < > ¡ ! @ # $ % & * ( ) - ' \" : ; , ¿ ?.]")
        return regex.containsMatchIn(password)
    }

    private fun hasMoreThan2IdenticalChars(): Boolean {
        val regex = Regex("(.)\\1{2,}")
        return regex.containsMatchIn(password)
    }
}