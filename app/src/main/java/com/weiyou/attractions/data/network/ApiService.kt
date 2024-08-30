package com.weiyou.attractions.data.network

import com.weiyou.attractions.data.models.AttractionsResponse
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
    ): Response<AttractionsResponse>
}