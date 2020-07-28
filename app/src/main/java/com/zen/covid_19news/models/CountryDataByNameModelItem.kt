package com.zen.covid_19news.models

data class CountryDataByNameModelItem(
    val code: String,
    val confirmed: Int,
    val country: String,
    val critical: Int,
    val deaths: Int,
    val lastChange: String,
    val lastUpdate: String,
    val latitude: Double,
    val longitude: Double,
    val recovered: Int
)