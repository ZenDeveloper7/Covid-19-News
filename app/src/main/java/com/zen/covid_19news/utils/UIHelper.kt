package com.zen.covid_19news.utils

import android.animation.ValueAnimator
import android.icu.util.ULocale.getCountry
import android.util.Log
import android.view.View
import android.widget.TextView
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow


object UIHelper {

    fun withSuffix(count: Long): String? {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        return String.format(
            "%.2f%c",
            count / 1000.0.pow(exp.toDouble()),
            "kMGTPE"[exp - 1]
        )
    }

    fun getWordsLength(string: String): Int? {
        val token = StringTokenizer(string)
        return token.countTokens()
    }

    fun dateToTimeFormat(date: String?): String? {
        Log.e("TAG", "dateToTimeFormat: ${Locale.getDefault().displayCountry}")
        val p = PrettyTime(Locale.getDefault().country)
        var isTime: String? = null
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH
            )
            val d: Date = sdf.parse(date)
            isTime = p.format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isTime
    }

    fun formatDate(string: String): String? {
        var date: String? = null
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH
            )
            val back = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val d: Date = sdf.parse(string)
            date = back.format(d).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun valueAnimator(value: Int, view: TextView, extra: String?) {
        val animator = ValueAnimator.ofInt(0, value)
        animator.duration = 1500
        if (extra == null)
            animator.addUpdateListener {
                view.text = it.animatedValue.toString()
            }
        else
            animator.addUpdateListener {
                view.text = extra + it.animatedValue.toString()
            }
        animator.start()
    }
}