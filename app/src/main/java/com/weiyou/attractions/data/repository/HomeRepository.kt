package com.weiyou.attractions.data.repository

import com.weiyou.attractions.data.models.AttractionsResponse
import com.weiyou.attractions.data.models.NetworkResult
import com.weiyou.attractions.data.network.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class HomeRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getAttractions(lang: String): Flow<NetworkResult<AttractionsResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            // Call the API using the remoteDataSource
            val result = remoteDataSource.getAttractions(lang)
            // Emit the result
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}