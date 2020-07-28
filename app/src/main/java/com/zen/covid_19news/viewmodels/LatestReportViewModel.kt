package com.zen.covid_19news.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.zen.covid_19news.repo.MainRepo
import com.zen.covid_19news.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.lang.Exception

class LatestReportViewModel(private val mainRepo: MainRepo) : ViewModel() {

    fun getWorldData() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = mainRepo.getAllWorldData()))
        } catch (exception: Exception) {
            Log.e("TAG", "getCountryData: $exception")
            emit(exception.message?.let { Resource.Failure(data = null, message = it) })
        }
    }

    fun getCountryDataAllStatus(country: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = mainRepo.getCountryDataAllStatus(country)))
        } catch (exception: Exception) {
            Log.e("TAG", "getCountryData: $exception")
            emit(exception.message?.let { Resource.Failure(data = null, message = it) })
        }
    }


}