package com.example.a6semlast.fragments.api

import com.example.a6semlast.fragments.data.HolidayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApi {
    @GET("holidays")
    suspend fun getHolidays(
        @Query("api_key") apiKey: String,
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("query") query: String // Добавляем параметр для поиска
    ): HolidayResponse
}
