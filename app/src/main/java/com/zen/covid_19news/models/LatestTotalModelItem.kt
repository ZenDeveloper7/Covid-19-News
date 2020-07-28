package com.zen.covid_19news.models

data class LatestTotalModelItem(
    val confirmed: Int,
    val critical: Int,
    val deaths: Int,
    val lastChange: String,
    val lastUpdate: String,
    val recovered: Int
)