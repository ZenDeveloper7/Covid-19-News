package com.zen.covid_19news.models

data class DailyReportModelItem(
    val active: Int,
    val confirmed: Int,
    val date: String,
    val deaths: Int,
    val recovered: Int
)