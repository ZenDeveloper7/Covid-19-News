package com.zen.covid_19news.models

data class Global(
    val NewConfirmed: Int,
    val NewDeaths: Int,
    val NewRecovered: Int,
    val TotalConfirmed: Int,
    val TotalDeaths: Int,
    val TotalRecovered: Int

) {
    override fun toString(): String {
        return "Global(NewConfirmed=$NewConfirmed, NewDeaths=$NewDeaths, NewRecovered=$NewRecovered, TotalConfirmed=$TotalConfirmed, TotalDeaths=$TotalDeaths, TotalRecovered=$TotalRecovered)"
    }
}