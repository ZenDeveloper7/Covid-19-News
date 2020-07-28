package com.zen.covid_19news.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zen.covid_19news.R
import com.zen.covid_19news.adapter.SourceAdapter
import com.zen.covid_19news.models.DataSourceModel
import kotlinx.android.synthetic.main.fragment_source.*

class SourcesFragment : Fragment(R.layout.fragment_source) {

    private lateinit var sourceAdapter: SourceAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sourceAdapter = SourceAdapter()
        sourceRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = sourceAdapter
        }
        sourceAdapter.differ.submitList(
            listOf(
                DataSourceModel(
                    "All World Data",
                    "https://api.covid19api.com/",
                    "https://api.covid19api.com/summary",
                    "https://covid19api.com/"
                ),
                DataSourceModel(
                    "Country Data",
                    "https://api.covid19api.com/",
                    "https://api.covid19api.com/total/country/india",
                    "https://covid19api.com/"
                )
            )
        )


    }
}