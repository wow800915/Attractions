package com.weiyou.attractions.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class LanguageDataStore(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    val selectedLanguage: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] // 设置默认语言为英语
        }

    suspend fun saveLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
}