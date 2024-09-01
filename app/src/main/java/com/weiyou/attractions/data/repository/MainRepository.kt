package com.weiyou.attractions.data.repository

import com.weiyou.attractions.data.dataStore.LanguageDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val languageDataStore: LanguageDataStore
) {
    // 获取语言设置
    fun getLanguage(): Flow<String?> {
        return languageDataStore.selectedLanguage
    }
}