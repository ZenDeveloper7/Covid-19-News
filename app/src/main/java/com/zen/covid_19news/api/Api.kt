package com.zen.covid_19news.api

import com.zen.covid_19news.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("totals?format=json")
    suspend fun getLatestTotals(): Response<LatestTotalModel>

    @GET("report/totals?date-format=YYYY-MM-DD&format=json")
    suspend fun getDailyReport(
        @Query("date")
        date: String
    ): Response<DailyReportModel>

    @GET("report/country/name?date-format=YYYY-MM-DD&format=json")
    suspend fun getDailyReportByCountry(
        @Query("date")
        date: String,
        @Query("name")
        countryName: String
    ): Response<DailyReportByCountryModel>

    @GET("country?format=json")
    suspend fun getCountryDataByName(
        @Query("name")
        countryName: String
    ): Response<CountryDataByNameModel>


    @GET("summary")
    suspend fun getAllWorldData(): Response<AllWorldDataResponseModel>

    @GET("/total/country/{country}")
    suspend fun getCountryDataAllStatus(@Path("country") country: String): Response<CountryTotalAllStatusResponse>


}