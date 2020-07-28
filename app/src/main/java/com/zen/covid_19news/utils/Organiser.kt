package com.zen.covid_19news.utils

import com.zen.covid_19news.models.Country

object Organiser {

    fun organiseRecoveredRate(model: List<Country>): List<Country> {
        return model.sortedBy { it.TotalRecovered }.asReversed()
    }
    fun organiseConfirmedRate(model: List<Country>): List<Country> {
        return model.sortedBy { it.TotalConfirmed }.asReversed()
    }
    fun organiseDeathRate(model: List<Country>): List<Country> {
        return model.sortedBy { it.TotalDeaths }.asReversed()
    }



}