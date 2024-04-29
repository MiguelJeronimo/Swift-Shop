package com.miguel.swiftshop.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.miguel.swiftshop.models.UserData
import kotlinx.coroutines.flow.map
import java.util.concurrent.Flow

private val IS_LOGIN_LAYOUT = booleanPreferencesKey("isLogin")
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = IS_LOGIN_LAYOUT.toString()
)

private val USER_DATA_LOGIN = stringPreferencesKey("emailUser")
private val Context.dataStoreUser : DataStore<Preferences> by preferencesDataStore(
    name = USER_DATA_LOGIN.toString()
)
class SettingsDataStore (context:Context){
    val preferencesFlow = context.dataStore.data.map{
        it[IS_LOGIN_LAYOUT]?: true
    }

    val preferencesFlowUsers = context.dataStoreUser.data.map{
        it[USER_DATA_LOGIN]?: true
    }

    suspend fun saveStatusloginPreferences(isLinearLayoutLogin:Boolean, context: Context){
        context.dataStore.edit {
            it[IS_LOGIN_LAYOUT] = isLinearLayoutLogin
        }
    }

    suspend fun saveUserPreferences(idCollectionList: String?, context: Context){
        context.dataStoreUser.edit {
            it[USER_DATA_LOGIN] = idCollectionList?:""
        }
    }
}