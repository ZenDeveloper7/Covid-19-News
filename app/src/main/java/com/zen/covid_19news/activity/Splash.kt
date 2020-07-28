package com.zen.covid_19news.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.zen.covid_19news.BuildConfig
import com.zen.covid_19news.R
import kotlinx.android.synthetic.main.alert_layout.*


class Splash : AppCompatActivity() {
    private val TAG: String = "SPLASH"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Config params updated: ${remoteConfig["version"].asString()}")
                    if (remoteConfig["version"].asString() == BuildConfig.VERSION_NAME)
                        Handler().postDelayed(
                            {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            },
                            6500
                        )
                    else {
                        val dialog = Dialog(this)
                        dialog.setContentView(R.layout.alert_layout)
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.apply {
                            update.setOnClickListener {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                                    )
                                )
                            }
                            dismiss.setOnClickListener { finish() }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dialog.create()
                        }
                        dialog.show()
                    }

                } else {
                    Log.e(TAG, "onCreate: Failed")
                }
            }

    }
}