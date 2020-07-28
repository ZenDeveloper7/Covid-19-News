package com.zen.covid_19news.models

data class DailyReportByCountryModelItem(
    val country: String,
    val date: String,
    val latitude: Double,
    val longitude: Double,
    val provinces: List<Province>
)