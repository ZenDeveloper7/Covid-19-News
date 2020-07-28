package com.zen.covid_19news.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.zen.covid_19news.BuildConfig
import com.zen.covid_19news.R
import com.zen.covid_19news.fragments.ContactFragment
import com.zen.covid_19news.fragments.MainFragment
import com.zen.covid_19news.fragments.SourcesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var TAG: String = "MAINACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.frameContainter, MainFragment())
            .commit()


        setUpNavDrawer()


        searchButton.setOnClickListener {
            val i = Intent(this, SearchActivity::class.java)
            startActivity(i)
            drawerLayout.closeDrawer(GravityCompat.START)
        }



        versionName.text = "v${BuildConfig.VERSION_NAME}"
    }

    private fun setUpNavDrawer() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)

        supportActionBar?.title = ""
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_nav_menu_icon)
        toggle.setToolbarNavigationClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }

        navContact.setOnClickListener {
            addFragment(ContactFragment(), "Contact")
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        navShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share App")
            var shareMessage =
                "Covid-19 News. Daily updates with the statistics all around the world. Download it from playstore link - "
            shareMessage =
                """${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}""".trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Choose one"))
            drawerLayout.closeDrawer(GravityCompat.START)

        }
        navSource.setOnClickListener {
            addFragment(SourcesFragment(), "Source")
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun addFragment(fragment: Fragment, string: String) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(string)
            .replace(R.id.frameContainter, fragment)
            .commit()
    }
}