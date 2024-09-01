package com.weiyou.attractions.data.network

import android.util.Log
import com.weiyou.attractions.data.models.api.attractions.AttractionsOutput
import com.weiyou.attractions.data.models.ErrorResponse
import com.weiyou.attractions.data.models.NetworkResult
import com.weiyou.attractions.data.models.api.news.NewsOutput
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun <T> getResponse(request: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val result = request.invoke()
            Log.d("RemoteDataSource", result.toString())
            if (result.isSuccessful) {
                // 200 ~ 300
                return NetworkResult.Success(result.body())
            } else {
                // 400 ~ 500
                NetworkResult.Error(
                    ErrorResponse(
                        result.code().toString(),
                        result.message()
                    )
                )
            }
        } catch (e: Throwable) {
            Log.d("RemoteDataSource", "call api error exception ==== ${e.message}")
            NetworkResult.Error(ErrorResponse(message = e.message))
        }
    }

    suspend fun getAttractions(lang: String): NetworkResult<AttractionsOutput> {
        return getResponse(request = { apiService.getAttractions(lang) })
    }

    suspend fun getNews(lang: String): NetworkResult<NewsOutput> {
        return getResponse(request = { apiService.getNews(lang) })
    }
}