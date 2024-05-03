package com.miguel.swiftshop.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.PublicKey
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import android.util.Base64

class CodeEncode {
    fun readKey(document: InputStream): String? {
        val bufferedReader = BufferedReader(InputStreamReader(document))
        var data: String? = null
        try {
            data = bufferedReader.readText()
            document.close()
        } catch (e: IOException){
            e.printStackTrace()
        }
        return data
    }

    fun stringToPublicKey(publicKeyString: String?): PublicKey? {
        val keyBytes = Base64.decode(publicKeyString?.toByteArray(), Base64.DEFAULT)  // Deserializar a bytes
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    fun stringToPrivateKey(privateKeyString: String?): PrivateKey? {
        val keyBytes = Base64.decode(privateKeyString,Base64.DEFAULT)  // Deserializar a bytes
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    fun generateKeyEncrypt(): KeyPair? {
        val keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair()
        return keyPair
    }

    fun encryptData(public_key: String?, users: Any): ByteArray? {
        val tranformData = users.toString().toByteArray()
        val cipher =  Cipher.getInstance("RSA/ECB/OAEPPadding")
        cipher.init(Cipher.ENCRYPT_MODE,stringToPublicKey(public_key))
        val encrypData = cipher.doFinal(tranformData)
        return encrypData
    }

    fun decrypData(private_key: String?, users:   ByteArray?): String {
        val cipher =  Cipher.getInstance("RSA/ECB/OAEPPadding")
        cipher.init(Cipher.DECRYPT_MODE, stringToPrivateKey(private_key))
        val decrypData = cipher.doFinal(users)
        return String(decrypData)
    }
}