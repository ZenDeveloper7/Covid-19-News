package com.zen.covid_19news.models

data class Country(
    val Country: String,
    val CountryCode: String,
    val Date: String,
    val NewConfirmed: Int,
    val NewDeaths: Int,
    val NewRecovered: Int,
    val Premium: Premium,
    val Slug: String,
    val TotalConfirmed: Int,
    val TotalDeaths: Int,
    val TotalRecovered: Int

) {
    override fun toString(): String {
        return "Country(Country='$Country', CountryCode='$CountryCode', Date='$Date', NewConfirmed=$NewConfirmed, NewDeaths=$NewDeaths, NewRecovered=$NewRecovered, Premium=$Premium, Slug='$Slug', TotalConfirmed=$TotalConfirmed, TotalDeaths=$TotalDeaths, TotalRecovered=$TotalRecovered)"
    }
}