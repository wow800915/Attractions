package com.weiyou.attractions.data.repository

import com.weiyou.attractions.data.dataStore.LanguageDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val languageDataStore: LanguageDataStore
) {

    fun getLanguage(): Flow<String?> {
        return languageDataStore.selectedLanguage
    }
}