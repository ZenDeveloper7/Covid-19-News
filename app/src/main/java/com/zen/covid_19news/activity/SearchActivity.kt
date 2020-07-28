package com.zen.covid_19news.activity

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zen.covid_19news.R
import com.zen.covid_19news.adapter.DayByDayAdapter
import com.zen.covid_19news.models.AllWorldDataResponseModel
import com.zen.covid_19news.models.Country
import com.zen.covid_19news.repo.MainRepo
import com.zen.covid_19news.utils.Constants
import com.zen.covid_19news.utils.Resource
import com.zen.covid_19news.utils.UIHelper
import com.zen.covid_19news.viewmodels.LatestReportViewModel
import com.zen.covid_19news.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_search.*
import java.lang.reflect.Type


class SearchActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var worldData: AllWorldDataResponseModel
    private lateinit var countryAdapter: ArrayAdapter<String>
    private var countriesToId: HashMap<String, Int> = HashMap()
    private lateinit var latestReportViewModel: LatestReportViewModel
    private var countries: ArrayList<String> = ArrayList()
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var type: Type
    private lateinit var dayAdapter: DayByDayAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setUpUI()
        setUpViewModel()
        setUpToolbar()

        sharedPreferences = getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        type = object : TypeToken<AllWorldDataResponseModel?>() {}.type
        worldData = Gson().fromJson<AllWorldDataResponseModel>(
            sharedPreferences.getString(
                Constants.WORLD_DATA,
                null
            ), type
        )

        for ((i, e) in worldData.Countries.withIndex()) {
            countries.add(e.Country)
            countriesToId[e.Country.toLowerCase()] = i
        }

        countryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, countries
        )

        searchBar.threshold = 1
        searchBar.setAdapter(countryAdapter)
        searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                showResult()
                true
            } else false
        }

        searchButton.setOnClickListener {
            showResult()
        }
    }

    private fun showResult() {
        if (searchBar.text.toString() == "")
            Toast.makeText(
                applicationContext,
                "Please. Enter country name to get data",
                Toast.LENGTH_SHORT
            ).show()
        else {
            resultCard.visibility = View.VISIBLE
            hideKeyboard(this)
            getCountryData(
                worldData.Countries[countriesToId[searchBar.text.toString()
                    .toLowerCase()]!!].CountryCode
            )
            if (countriesToId.containsKey(searchBar.text.toString().toLowerCase()))
                setData(
                    worldData.Countries[countriesToId[searchBar.text.toString()
                        .toLowerCase()]!!]
                )
            else
                Toast.makeText(
                    applicationContext,
                    "Sorry! There is no data available for the country - ${searchBar.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun setUpUI() {
        dayAdapter = DayByDayAdapter()
        dayByDayRecycler.apply {
            layoutManager = LinearLayoutManager(application)
            adapter = dayAdapter
        }
    }

    private fun getCountryData(country: String) {
        latestReportViewModel.getCountryDataAllStatus(country)
            .observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        dataLoader.stopShimmerAnimation()
                        dataLoader.visibility = View.GONE
                        dayByDayRecycler.visibility = View.VISIBLE

                        dayAdapter.differ.submitList(
                            response.data?.body()?.asReversed()
                        )
                    }
                    is Resource.Failure -> {
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        dataLoader.startShimmerAnimation()
                        dataLoader.visibility = View.VISIBLE
                        dayByDayRecycler.visibility = View.GONE
                    }
                }
            })
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setData(country: Country) {
        lastUpdated.text = "Last Updated ${UIHelper.dateToTimeFormat(country.Date)}"

        UIHelper.valueAnimator(country.TotalConfirmed, confirmedCases, null)
        UIHelper.valueAnimator(country.TotalRecovered, recoveredCases, null)
        UIHelper.valueAnimator(country.TotalDeaths, deathCases, null)
        UIHelper.valueAnimator(country.NewConfirmed, newConfirmedCases, null)
        UIHelper.valueAnimator(country.NewRecovered, newRecoveredCases, null)
        UIHelper.valueAnimator(country.NewDeaths, newDeathCases, null)

        countryDetails.text = country.Country + "(${country.CountryCode})"
    }


    private fun setUpViewModel() {
        latestReportViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(LatestReportViewModel::class.java)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}