package com.zen.covid_19news.repo

import com.zen.covid_19news.api.RetrofitClient

class MainRepo {
    suspend fun getDailyReports(date: String) = RetrofitClient.api.getDailyReport(date)
    suspend fun getLatestReports() = RetrofitClient.api.getLatestTotals()
    suspend fun getDailyByCountry(date: String, countryName: String) =
        RetrofitClient.api.getDailyReportByCountry(date, countryName)

    suspend fun getCountryData(countryName: String) =
        RetrofitClient.api.getCountryDataByName(countryName)

    suspend fun getAllWorldData() = RetrofitClient.api2.getAllWorldData()
    suspend fun getCountryDataAllStatus(country: String) =
        RetrofitClient.api2.getCountryDataAllStatus(country)
}