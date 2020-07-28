package com.zen.covid_19news.models

data class Province(
    val active: Int,
    val confirmed: Int,
    val deaths: Int,
    val province: String,
    val recovered: Int
)