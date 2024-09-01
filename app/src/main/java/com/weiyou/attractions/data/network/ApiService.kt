package com.weiyou.attractions.data.network

import com.weiyou.attractions.data.models.api.attractions.AttractionsOutput
import com.weiyou.attractions.data.models.api.news.NewsOutput
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Accept: application/json")
    @GET("{lang}/Attractions/All")
    suspend fun getAttractions(
        @Path("lang") lang: String,
        @Query("categoryIds") categoryIds: String? = null,
        @Query("nlat") nlat: Double? = null,
        @Query("elong") elong: Double? = null,
        @Query("page") page: Int = 1
    ): Response<AttractionsOutput>

    @Headers("Accept: application/json")
    @GET("{lang}/Events/News")
    suspend fun getNews(
        @Path("lang") lang: String,
        @Query("begin") begin: String? = null,
        @Query("end") end: String? = null,
        @Query("page") page: Int = 1
    ): Response<NewsOutput>
}