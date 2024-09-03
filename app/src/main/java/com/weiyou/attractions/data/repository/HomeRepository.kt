package com.weiyou.attractions.data.repository

import com.weiyou.attractions.data.models.api.attractions.AttractionsOutput
import com.weiyou.attractions.data.models.NetworkResult
import com.weiyou.attractions.data.models.api.news.NewsOutput
import com.weiyou.attractions.data.network.RemoteDataSource
import com.weiyou.attractions.data.dataStore.LanguageDataStore
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

    suspend fun getAttractions(page: Int): Flow<NetworkResult<AttractionsOutput>> {
        return languageDataStore.selectedLanguage
            .filterNotNull()
            .flatMapLatest { lang ->
                flow {
                    val result = remoteDataSource.getAttractions(lang, page)
                    emit(result)
                }.flowOn(Dispatchers.IO)
            }
    }

    suspend fun getNews(): Flow<NetworkResult<NewsOutput>> {
        return languageDataStore.selectedLanguage
            .filterNotNull()
            .flatMapLatest { lang ->
                flow {
                    val result = remoteDataSource.getNews(lang)
                    emit(result)
                }.flowOn(Dispatchers.IO)
            }
    }

    suspend fun saveLanguage(language: String) {
        languageDataStore.saveLanguage(language)
    }

    fun getSavedLanguage(): Flow<String?> {
        return languageDataStore.selectedLanguage
    }
}