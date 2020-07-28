package com.zen.covid_19news.models

data class AllWorldDataResponseModel(
    val Countries: List<Country>,
    val Date: String,
    val Global: Global
) {
    override fun toString(): String {
        return "AllWorldDataResponseModel(Countries=$Countries, Date='$Date', Global=$Global)"
    }
}