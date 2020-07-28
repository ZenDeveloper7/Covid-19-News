package com.zen.covid_19news.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zen.covid_19news.repo.MainRepo
import com.zen.covid_19news.viewmodels.LatestReportViewModel

class ViewModelFactory(
    private val mainRepo: MainRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LatestReportViewModel(mainRepo) as T
    }
}