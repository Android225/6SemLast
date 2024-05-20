package com.example.a6semlast.fragments.data

import com.google.gson.annotations.SerializedName

data class HolidayResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("response") val response: Response
)

data class Meta(
    @SerializedName("code") val code: Int
)

data class Response(
    @SerializedName("holidays") val holidays: List<Holiday>
)

data class Holiday(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: Date,
    @SerializedName("type") val type: List<String>
)

data class Date(
    @SerializedName("iso") val iso: String,
    @SerializedName("datetime") val datetime: Datetime
)

data class Datetime(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("day") val day: Int
)
