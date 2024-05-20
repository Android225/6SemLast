package com.example.a6semlast.fragments.adapter

import com.example.a6semlast.fragments.api.HolidayApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://calendarific.com/api/v2/"

    val api: HolidayApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(HolidayApi::class.java)
    }
}
