package com.weiyou.attractions.data.repository

import com.weiyou.attractions.utils.LanguageDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val languageDataStore: LanguageDataStore
) {
    // 获取语言设置
    fun getLanguage(): Flow<String?> {
        return languageDataStore.selectedLanguage
    }

    // 定义其他数据操作方法
    fun getExampleData(): String {
        return "這是來自 Repository 的數據"
    }
}