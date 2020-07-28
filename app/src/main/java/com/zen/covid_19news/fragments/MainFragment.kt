package com.zen.covid_19news.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zen.covid_19news.R
import com.zen.covid_19news.adapter.ConfirmedRateAdapter
import com.zen.covid_19news.adapter.DeathRateAdapter
import com.zen.covid_19news.adapter.RecoveredRateAdapter
import com.zen.covid_19news.models.AllWorldDataResponseModel
import com.zen.covid_19news.models.Global
import com.zen.covid_19news.repo.MainRepo
import com.zen.covid_19news.utils.Constants
import com.zen.covid_19news.utils.Organiser
import com.zen.covid_19news.utils.Resource
import com.zen.covid_19news.utils.UIHelper
import com.zen.covid_19news.viewmodels.LatestReportViewModel
import com.zen.covid_19news.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.reflect.Type
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var recoveredRateAdapter: RecoveredRateAdapter
    private lateinit var confirmedRateAdapter: ConfirmedRateAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var type: Type
    private lateinit var worldData: AllWorldDataResponseModel
    private lateinit var deathRateAdapter: DeathRateAdapter
    private lateinit var latestReportViewModel: LatestReportViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpUI()

        if (sharedPreferences.getString(Constants.WORLD_DATA, null) == null)
            setUpRecyclerData(true)
        else
            setUpRecyclerData(false)

        swipeToRefresh.setOnRefreshListener {
            setUpRecyclerData(true)
            swipeToRefresh.isRefreshing = false
        }


    }


    private fun setUpViewModel() {
        latestReportViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(LatestReportViewModel::class.java)
    }

    private fun setUpUI() {
        sharedPreferences =
            activity?.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        recoveredRateAdapter = RecoveredRateAdapter()
        confirmedRateAdapter = ConfirmedRateAdapter()
        deathRateAdapter = DeathRateAdapter()

        recoveredRateRecycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recoveredRateAdapter
        }
        confirmedRateRecycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = confirmedRateAdapter
        }
        deathRateRecycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = deathRateAdapter
        }

    }

    private fun setUpRecyclerData(value: Boolean) {
        if (value) {
            latestReportViewModel.getWorldData()
                .observe(viewLifecycleOwner, Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            shimmerLoader.stopShimmerAnimation()
                            shimmerLoader2.stopShimmerAnimation()
                            shimmerLoader3.stopShimmerAnimation()
                            shimmerLoader.visibility = View.GONE
                            shimmerLoader2.visibility = View.GONE
                            shimmerLoader3.visibility = View.GONE

                            confirmedRateRecycler.visibility = View.VISIBLE
                            recoveredRateRecycler.visibility = View.VISIBLE
                            deathRateRecycler.visibility = View.VISIBLE

                            editor.putString(
                                Constants.WORLD_DATA,
                                Gson().toJson(response.data?.body())
                            )
                            editor.commit()

                            response.data?.body()?.let {
                                recoveredRateAdapter.differ.submitList(
                                    Organiser.organiseRecoveredRate(
                                        it.Countries
                                    )
                                )
                                confirmedRateAdapter.differ.submitList(
                                    Organiser.organiseConfirmedRate(
                                        it.Countries
                                    )
                                )
                                deathRateAdapter.differ.submitList(
                                    Organiser.organiseDeathRate(
                                        it.Countries
                                    )
                                )
                                lastUpdated.text =
                                    "Last Updated ${UIHelper.dateToTimeFormat(it.Date)}"
                                Log.e("TAG", "setUpRecyclerData: ${it.Date}")
                                setPieChart(it.Global)
                            }

                        }
                        is Resource.Failure -> {
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            shimmerLoader.startShimmerAnimation()
                            shimmerLoader2.startShimmerAnimation()
                            shimmerLoader3.startShimmerAnimation()
                            shimmerLoader.visibility = View.VISIBLE
                            shimmerLoader2.visibility = View.VISIBLE
                            shimmerLoader3.visibility = View.VISIBLE

                            confirmedRateRecycler.visibility = View.GONE
                            recoveredRateRecycler.visibility = View.GONE
                            deathRateRecycler.visibility = View.GONE
                        }
                    }
                })

        } else {
            shimmerLoader.visibility = View.GONE
            shimmerLoader2.visibility = View.GONE
            shimmerLoader3.visibility = View.GONE

            type = object : TypeToken<AllWorldDataResponseModel?>() {}.type
            worldData = Gson().fromJson<AllWorldDataResponseModel>(
                sharedPreferences.getString(
                    Constants.WORLD_DATA,
                    null
                ), type
            )

            Log.e("TAG", "setUpRecyclerData: $worldData")
            recoveredRateAdapter.differ.submitList(
                Organiser.organiseRecoveredRate(
                    worldData.Countries
                )
            )
            confirmedRateAdapter.differ.submitList(
                Organiser.organiseConfirmedRate(
                    worldData.Countries
                )
            )
            deathRateAdapter.differ.submitList(
                Organiser.organiseDeathRate(
                    worldData.Countries
                )
            )
            lastUpdated.text =
                "Last Updated ${UIHelper.dateToTimeFormat(worldData.Date)}"

            setPieChart(worldData.Global)
        }
    }

    private fun setPieChart(body: Global) {
        val confirmed: Float = body.TotalConfirmed.toFloat()
        val deaths: Float = body.TotalDeaths.toFloat()
        val recovered: Float = body.TotalRecovered.toFloat()
        val colors = ArrayList<Int>()
        val entries: List<PieEntry> = listOf(
            PieEntry(confirmed, ""),
            PieEntry(deaths, ""),
            PieEntry(recovered, "")
        )

        colors.add(Color.rgb(255, 136, 0))
        colors.add(Color.rgb(249, 41, 69))
        colors.add(Color.rgb(0, 136, 255))

        val dataSet = PieDataSet(entries, "Global Report")
        dataSet.colors = colors
        dataSet.sliceSpace = 2f
        dataSet.selectionShift = 5f
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f

        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        chart.data = data
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                Log.e("TAG", "onNothingSelected: ")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.e("TAG", "onValueSelected: ${e.toString()}")
            }

        })
        chart.highlightValues(null)
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.setExtraOffsets(20f, 0f, 20f, 0f)

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true


        chart.animateY(1400, Easing.EaseInOutQuad)
        val l: Legend = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = false
    }


}