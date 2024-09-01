package com.weiyou.attractions.data.repository

import com.weiyou.attractions.data.models.AttractionsOutput
import com.weiyou.attractions.data.models.NetworkResult
import com.weiyou.attractions.data.network.RemoteDataSource
import com.weiyou.attractions.utils.LanguageDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val languageDataStore: LanguageDataStore
) {

    // API 调用获取景点信息
    suspend fun getAttractions(): Flow<NetworkResult<AttractionsOutput>> {
        return languageDataStore.selectedLanguage
            .filterNotNull() // 过滤空值
            .flatMapLatest { lang -> // 使用最新的语言设置请求数据
                flow {
                    emit(NetworkResult.Loading)
                    val result = remoteDataSource.getAttractions(lang)
                    emit(result)
                }.flowOn(Dispatchers.IO)
            }
    }

    // 保存语言设置
    suspend fun saveLanguage(language: String) {
        languageDataStore.saveLanguage(language)
    }

    // 获取保存的语言设置
    fun getSavedLanguage(): Flow<String?> {
        return languageDataStore.selectedLanguage
    }
}